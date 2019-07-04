package com.serviceimple;

import com.alibaba.fastjson.JSON;
import com.config.RedisConfig;
import com.dto.redis.SchoolAddMoneyDTO;
import com.dto.redis.SenderAddMoneyDTO;
import com.dto.redis.WxUserAddSourceDTO;
import com.dto.wxgzh.Message;
import com.redis.message.RedisUtil;
import com.service.SchoolService;
import com.service.SenderService;
import com.service.WxUserService;
import com.util.LoggerUtil;
import com.util.SpringUtil;
import com.wx.towallet.WeChatPayUtil;
import ops.school.api.dao.*;
import ops.school.api.dto.SenderTj;
import ops.school.api.entity.*;
import ops.school.api.exception.YWException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SenderServiceImple implements SenderService {

	@Autowired
	private SenderMapper senderMapper;
	@Autowired
    private WxUserService wxUserService;
	@Autowired
	private OrdersMapper ordersMapper;
	@Autowired
	private RunOrdersMapper runordersMapper;
	@Autowired
	private WxUserBellMapper wxUserBellMapper;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	@Autowired
    private SchoolService schoolService;
	@Autowired
	private TxLogMapper txLogMapper;
	@Autowired
	private ShopMapper shopMapper;
	@Autowired
	private OrdersCompleteMapper ocm;
	@Autowired
	private RedisUtil cache;

	@Override
	public Sender findById(Integer id) {
		if (SpringUtil.redisCache()) {
			Object rs = stringRedisTemplate.boundHashOps("SENDER_LIST").get(id.toString());
			if (rs != null) {
				return JSON.parseObject(rs.toString(), Sender.class);
			} else {
				Sender sender = senderMapper.selectByPrimaryKey(id);
				stringRedisTemplate.boundHashOps("SENDER_LIST").put(id.toString(), JSON.toJSONString(sender));
				return sender;
			}
		}
		return senderMapper.selectByPrimaryKey(id);
	}

	@Override
	public void add(Sender sender) {
		if (senderMapper.check(sender.getOpenId()) == null) {
			senderMapper.insert(sender);
		} else {
			throw new YWException("请勿重复申请");
		}
	}

	@Override
	public List<Sender> find(Sender sender) {
		switch (sender.getQueryType()) {
		case "wxuser":
			sender.setOpenId(sender.getQuery());
			break;
		case "school":
			sender.setSchoolId(Integer.valueOf(sender.getQuery()));
			break;
		case "admin":
			break;
		}
		return senderMapper.find(sender);
	}

	@Override
	public int update(Sender sender) {
		if (SpringUtil.redisCache()) {
			stringRedisTemplate.boundHashOps("SENDER_LIST").delete(sender.getId().toString());
		}
		return senderMapper.updateByPrimaryKeySelective(sender);
	}

	@Override
	public Sender findByPhone(String phone) {
		Sender query = new Sender();
		query.setPhone(phone);
		List<Sender> list = senderMapper.find(query);
		if (list.size() > 0) {
			/* if(list.get(0).getExam().equals("审核通过")) */
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<Orders> findorderbydjs(int senderId, int page, int size, String status) {
		Sender sender = findById(senderId);
		sender.setPage(page);
		sender.setSize(size);
		sender.setOrderBy(status);
		if (sender.getTakeoutFlag() == 1) {
			return ordersMapper.findBySenderTakeout(sender);
		} else {
			return null;
		}
	}

	@Override
	public int acceptOrder(int senderId, String orderId) {
		Sender sender = findById(senderId);
		Orders orders = ordersMapper.selectByPrimaryKey(orderId);
		orders.setSenderId(senderId);
		orders.setSenderName(sender.getName());
		orders.setSenderPhone(sender.getPhone());
		int rs=0;
		if((rs=ordersMapper.SenderAccept(orders))==1){
            WxUser wxUser = wxUserService.findById(orders.getOpenId());
            School school = schoolService.findById(wxUser.getSchoolId());
			wxUserService.sendWXGZHM(wxUser.getPhone(), new Message(null,
					"dVHcAp-Bc2ATpgYe09-5D7n50hjLshju8Zl6GGoyB7M",
					school.getWxAppId(), "pages/order/orderDetail/orderDetail?orderId="
					+ orders.getId() + "&typ=" + orders.getTyp(),
					"您的订单已被配送员接手！", sender.getName(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
					null, null, null, null, null, null,
					null, " 配送员正火速配送中，请耐心等待！"));
		}
		return rs;
	}

	@Override
	public int sendergetorder(String orderId) {
		return ordersMapper.getorder(orderId);
	}

	@Transactional
	@Override
	public void end(String orderId, boolean end) {
		Orders orders = ordersMapper.selectByPrimaryKey(orderId);
		Sender sender = findById(orders.getSenderId());
        WxUser wxUser = wxUserService.findById(orders.getOpenId());
		if (end) {
			orders.setDestination(1);
			if (ordersMapper.end(orders) == 1) {
				BigDecimal senderGet = new BigDecimal(0);
				if (orders.getSenderId() != 0) {
					senderGet = orders.getSendPrice().multiply(new BigDecimal(1).subtract(sender.getRate()));
					stringRedisTemplate.convertAndSend(RedisConfig.SENDERBELL,
							new SenderAddMoneyDTO(sender.getOpenId(), senderGet).toJsonString()
					);
				}
				stringRedisTemplate.convertAndSend(RedisConfig.SCHOOLBELL,
						new SchoolAddMoneyDTO(orders.getSchoolId(), orders.getPayPrice().subtract(senderGet), senderGet).toJsonString()
				);
				// 增加积分
				stringRedisTemplate.convertAndSend(RedisConfig.SENDERBELL,
						new WxUserAddSourceDTO(orders.getOpenId(), orders.getPayPrice().intValue()).toJsonString()
				);
				
			} else {
				return;
			}
		} else {
			orders.setDestination(0);
			BigDecimal returnPrice;
			if (orders.getSendPrice().compareTo(orders.getSchoolTopDownPrice()) == -1) {
				orders.setPayPrice(orders.getPayPrice().subtract(orders.getSendPrice()));
				orders.setSendPrice(new BigDecimal(0));
				returnPrice = orders.getSendPrice();
			} else {
				orders.setPayPrice(orders.getPayPrice().subtract(orders.getSchoolTopDownPrice()));
				orders.setSendPrice(orders.getSendPrice().subtract(orders.getSchoolTopDownPrice()));
				returnPrice = orders.getSchoolTopDownPrice();
			}
				Map<String, Object> map = new HashMap<>();
				map.put("phone", wxUser.getOpenId() + "-" + wxUser.getPhone());
				map.put("amount", returnPrice);
				if (ordersMapper.end(orders) == 1) {
					if (wxUserBellMapper.charge(map) == 1) {
						BigDecimal senderGet = orders.getSendPrice().multiply(new BigDecimal(1).subtract(sender.getRate()));
						stringRedisTemplate.convertAndSend(RedisConfig.SENDERBELL,
								new SenderAddMoneyDTO(sender.getOpenId(), senderGet).toJsonString()
						);
						stringRedisTemplate.convertAndSend(RedisConfig.SCHOOLBELL,
								new SchoolAddMoneyDTO(orders.getSchoolId(), orders.getPayPrice().subtract(senderGet), senderGet).toJsonString()
						);
						// 增加积分
						stringRedisTemplate.convertAndSend(RedisConfig.SENDERBELL,
								new WxUserAddSourceDTO(orders.getOpenId(), orders.getPayPrice().intValue()).toJsonString()
						);
					}
				}
			else {
				return;
			}
		}
		// 对订单进行结算
        OrdersComplete oc = new OrdersComplete(orders, schoolService.findById(orders.getSchoolId()),
				shopMapper.selectByPrimaryKey(orders.getShopId()), sender);
		ocm.insert(oc);
		cache.takeoutCountSuccessadd(orders.getSchoolId());
		stringRedisTemplate.convertAndSend(RedisConfig.PRODUCTADD, orderId);
		if(orders.getTyp().equals("外卖订单")){
			School school = schoolService.findById(wxUser.getSchoolId());
			Message message = new Message(null,
					"8Qy7KQRt2upGjwmhp7yYaR2ycfKkXNI8gqRvGBnovsk",
					school.getWxAppId(), "pages/mine/integral/integral",
					null, orderId, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
					null, null, null, null, null, null,
					null, "成功获得" + orders.getPayPrice().intValue() + "积分，可以前往积分商城兑换哟！");
			if (orders.getDestination() == 1) {
				message.setDataFirst("您的订单已送达到寝。");
			} else {
				message.setDataFirst("您的订单已送达楼下，请下楼自取。系统已返还" + orders.getSchoolTopDownPrice() + "元至您钱包余额内，请注意查收！");
			}
			wxUserService.sendWXGZHM(wxUser.getPhone(), message);

		}
	}

	@Override
	public void endRun(String orderId) {
		if (runordersMapper.end(orderId) == 1) {
			RunOrders orders = runordersMapper.selectByPrimaryKey(orderId);
			Sender sender = findById(orders.getSenderId());
			BigDecimal senderGet = orders.getTotalPrice().multiply(new BigDecimal(1).subtract(sender.getRate()));
			stringRedisTemplate.convertAndSend(RedisConfig.SENDERBELL,
					new SenderAddMoneyDTO(sender.getOpenId(), senderGet).toJsonString()
			);
			stringRedisTemplate.convertAndSend(RedisConfig.SCHOOLBELL,
					new SchoolAddMoneyDTO(orders.getSchoolId(), orders.getTotalPrice().subtract(senderGet), senderGet).toJsonString()
			);
			// senderAddMoney(orders.getTotalPrice(),orders.getSenderId());
			// 增加积分
			// addsource(orders.getOpenId(), orders.getTotalPrice().intValue());
			stringRedisTemplate.convertAndSend(RedisConfig.SENDERBELL,
					new WxUserAddSourceDTO(orders.getOpenId(), orders.getTotalPrice().intValue()).toJsonString()
			);
			cache.runCountSuccessadd(orders.getSchoolId());
            WxUser wxUser = wxUserService.findById(orders.getOpenId());
            School school = schoolService.findById(wxUser.getSchoolId());
			wxUserService.sendWXGZHM(wxUser.getPhone(), new Message(null,
					"8Qy7KQRt2upGjwmhp7yYaR2ycfKkXNI8gqRvGBnovsk",
					school.getWxAppId(), "pages/mine/integral/integral",
					"您的跑腿订单已经完成!", orderId, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
					null, null, null, null, null, null,
					null, "成功获得" + orders.getTotalPrice().intValue() + "积分，可以前往积分商城兑换哟！"));

		}
	}

	/*
	 * public void addsource(String openid,int source){ //增加积分 WxUser
	 * wxUser=wxUserMapper.selectByPrimaryKey(openid); Map<String,Object>
	 * map2=new HashMap<>(); map2.put("phone",
	 * wxUser.getOpenId()+"-"+wxUser.getPhone()); map2.put("source", source);
	 * wxUserBellMapper.addSource(map2); }
	 */

	public void senderAddMoney(BigDecimal amount, Integer senderId) {
		if (senderId == 0) {
			return;
		}
		Sender sender = findById(senderId);
		amount = amount.multiply(new BigDecimal(1).subtract(sender.getRate())).setScale(2, BigDecimal.ROUND_HALF_DOWN);
        WxUser wxUser = wxUserService.findById(sender.getOpenId());
		Map<String, Object> map = new HashMap<>();
		map.put("phone", wxUser.getOpenId() + "-" + wxUser.getPhone());
		map.put("amount", amount);
		if (wxUserBellMapper.charge(map) == 0) {
			LoggerUtil.log("配送员送达订单增加余额失败：" + amount.toString());
		}
	}

	@Override
	public List<RunOrders> findorderbyrundjs(int senderId, int page, int size, String status) {
		Sender sender = findById(senderId);
		sender.setPage(page);
		sender.setSize(size);
		sender.setOrderBy(status);
		if (sender.getRunFlag() == 1) {
			return runordersMapper.findBySenderRun(sender);
		} else {
			return null;
		}
	}

	@Override
	public int acceptOrderRun(int senderId, String orderId) {
		Sender sender = findById(senderId);
		RunOrders orders = runordersMapper.selectByPrimaryKey(orderId);
		orders.setSenderId(senderId);
		orders.setSenderName(sender.getName());
		orders.setSenderPhone(sender.getPhone());
		int rs=0;
		if((rs=runordersMapper.SenderAccept(orders))==1){
            WxUser wxUser = wxUserService.findById(orders.getOpenId());
            School school = schoolService.findById(wxUser.getSchoolId());
			wxUserService.sendWXGZHM(wxUser.getPhone(), new Message(null,
					"dVHcAp-Bc2ATpgYe09-5D7n50hjLshju8Zl6GGoyB7M",
					school.getWxAppId(), "pages/order/orderDetail/orderDetail?orderId="
					+ orders.getId() + "&typ=" + orders.getTyp(),
					"您的订单已被配送员接手！", sender.getName(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
					null, null, null, null, null, null,
					null, " 配送员正火速配送中，请耐心等待！"));
		}
		return rs;
	}

	@Override
	public SenderTj statistics(int senderId, String beginTime, String endTime) {
		SenderTj rs = new SenderTj();
		Map<String, Object> map = new HashMap<>();
		map.put("senderId", senderId);
		map.put("beginTime", beginTime);
		map.put("endTime", endTime);
		List<Orders> result = ordersMapper.senderStatistics(map);
		List<RunOrders> result2 = runordersMapper.senderStatistics(map);
		for (Orders temp : result) {
			if (temp.getStatus().equals("配送员已接手")) {
				rs.setTakeoutNosuccess(temp.getFloorId());
			}
			if (temp.getStatus().equals("已完成")) {
				rs.setTakeoutSuccess(temp.getFloorId());
			}
			if (temp.getSendPrice() != null)
				rs.setTakeout_Price(rs.getTakeout_Price().add(temp.getSendPrice()));
		}
		for (RunOrders temp : result2) {
			if (temp.getStatus().equals("配送员已接手")) {
				rs.setRunNosuccess(temp.getFloorId());
			}
			if (temp.getStatus().equals("已完成")) {
				rs.setRunSuccess(temp.getFloorId());
			}
			if (temp.getTotalPrice() != null)
				rs.setRun_price(rs.getRun_price().add(temp.getTotalPrice()));
		}
		return rs;
	}

	@Override
	public int count(Sender sender) {
		return senderMapper.count(sender);
	}

	@Transactional
	public int tx(String senderId, BigDecimal amount) {
		Sender sender = senderMapper.check(senderId);
		WxUser query = new WxUser();
		query.setSchoolId(sender.getSchoolId());
		query.setPhone(sender.getPhone());
        WxUser user = wxUserService.findByschoolAndPhone(query);
        School school = schoolService.findById(sender.getSchoolId());
		if (user == null) {
			throw new YWException("提现失败，请返回" + school.getName() + "绑定该手机号码");
		}
		Map<String, Object> map = new HashMap();
		map.put("phone", senderId + "-" + sender.getPhone());
		map.put("amount", amount);
		map.put("schoolId", sender.getSchoolId());
		String payId = "tx" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		if (this.wxUserBellMapper.pay(map) == 1) {
			try {
				TxLog log = new TxLog(sender.getId(), "配送员提现", null, amount, "", sender.getSchoolId(), user.getAppId());
				if (WeChatPayUtil.transfers(school.getWxAppId(), school.getMchId(), school.getWxPayId(),
						school.getCertPath(), payId, "127.0.0.1", amount, user.getOpenId(), log) == 1) {
					txLogMapper.insert(log);
                    if (schoolService.sendertx(map) == 0) {
						LoggerUtil.log("配送员提现学校减少金额失败:" + senderId + ":" + amount);
					}
					return 1;
				}
			} catch (Exception e) {
				LoggerUtil.log(senderId + ":" + amount + e.getMessage());
			}
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return 2;
		}
		throw new YWException("余额不足");
	}

	@Override
	public int finddsh(int schoolId) {
		return senderMapper.finddsh(schoolId);
	}

	@Transactional
	@Override
	public int tx2(String senderId, String userId) {
		Sender sender = senderMapper.check(senderId);
        WxUser wxUser = wxUserService.findById(userId);
        WxUser senderUser = wxUserService.findById(senderId);
        School school = schoolService.findById(sender.getSchoolId());
		WxUserBell wxUserBell = wxUserBellMapper
				.selectByPrimaryKey(senderUser.getOpenId() + "-" + senderUser.getPhone());
		Map<String, Object> map = new HashMap();
		map.put("phone", senderId + "-" + sender.getPhone());
		map.put("amount", wxUserBell.getMoney());
		map.put("schoolId", sender.getSchoolId());
		if (this.wxUserBellMapper.pay(map) == 1) {
			try {
				String payId = "tx" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
				TxLog log = new TxLog(sender.getId(), "配送员提现", null, wxUserBell.getMoney(), "", sender.getSchoolId(),
						wxUser.getAppId());
				if (WeChatPayUtil.transfers(school.getWxAppId(), school.getMchId(), school.getWxPayId(),
						school.getCertPath(), payId, "127.0.0.1", wxUserBell.getMoney(), wxUser.getOpenId(),
						log) == 1) {
					txLogMapper.insert(log);
                    if (schoolService.sendertx(map) == 0) {
						LoggerUtil.log("配送员提现学校减少金额失败:" + senderId + ":" + wxUserBell.getMoney());
					}
					return 1;
				}
			} catch (Exception e) {
				LoggerUtil.log(senderId + ":" + wxUserBell.getMoney() + e.getMessage());
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			}
			return 2;
		}
		throw new YWException("余额不足");
	}

}
