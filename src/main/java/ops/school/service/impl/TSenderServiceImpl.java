package ops.school.service.impl;

import ops.school.api.config.Server;
import ops.school.api.dto.SenderTj;
import ops.school.api.dto.redis.SchoolAddMoneyDTO;
import ops.school.api.dto.redis.SenderAddMoneyDTO;
import ops.school.api.dto.redis.WxUserAddSourceDTO;
import ops.school.api.dto.wxgzh.Message;
import ops.school.api.entity.*;
import ops.school.api.exception.YWException;
import ops.school.api.service.*;
import ops.school.api.util.LoggerUtil;
import ops.school.api.util.RedisUtil;
import ops.school.api.wx.towallet.WeChatPayUtil;
import ops.school.service.TSenderService;
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
public class TSenderServiceImpl implements TSenderService {

    @Autowired
    private SenderService senderService;
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private WxUserService wxUserService;
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private WxUserBellService wxUserBellService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private ShopService shopService;
    @Autowired
    private RunOrdersService runOrdersService;
    @Autowired
    private OrderCompleteService orderCompleteService;
    @Autowired
    private TxLogService txLogService;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public List<Orders> findorderbydjs(Integer senderId, Integer page, Integer size, String status) {
        Sender sender = senderService.findById(senderId);
        sender.setPage(page);
        sender.setSize(size);
        sender.setOrderBy(status);
        if (sender.getTakeoutFlag() == 1) {
            return ordersService.findBySenderTakeout(sender);
        } else {
            return null;
        }
    }

    @Override
    public Integer sendergetorder(String orderId) {
        return ordersService.getorder(orderId);
    }

    @Override
    public Integer acceptOrder(Integer senderId, String orderId) {
        Sender sender = senderService.findById(senderId);
        Orders orders = ordersService.findById(orderId);
        orders.setSenderId(senderId);
        orders.setSenderName(sender.getName());
        orders.setSenderPhone(sender.getPhone());
        int rs = 0;
        if ((rs = ordersService.senderAccept(orders)) == 1) {
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

    @Transactional
    @Override
    public void end(String orderId, boolean end) {
        Orders orders = ordersService.findById(orderId);
        Sender sender = senderService.findById(orders.getSenderId());
        WxUser wxUser = wxUserService.findById(orders.getOpenId());
        if (end) {
            orders.setDestination(1);
            if (ordersService.end(orders) == 1) {
                BigDecimal senderGet = new BigDecimal(0);
                if (orders.getSenderId() != 0) {
                    senderGet = orders.getSendPrice().multiply(new BigDecimal(1).subtract(sender.getRate()));
                    stringRedisTemplate.convertAndSend(Server.SENDERBELL,
                            new SenderAddMoneyDTO(sender.getOpenId(), senderGet).toJsonString()
                    );
                }
                stringRedisTemplate.convertAndSend(Server.SCHOOLBELL,
                        new SchoolAddMoneyDTO(orders.getSchoolId(), orders.getPayPrice().subtract(senderGet), senderGet).toJsonString()
                );
                // 增加积分
                stringRedisTemplate.convertAndSend(Server.SENDERBELL,
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
            if (ordersService.end(orders) == 1) {
                if (wxUserBellService.charge(map) == 1) {
                    BigDecimal senderGet = orders.getSendPrice().multiply(new BigDecimal(1).subtract(sender.getRate()));
                    stringRedisTemplate.convertAndSend(Server.SENDERBELL,
                            new SenderAddMoneyDTO(sender.getOpenId(), senderGet).toJsonString()
                    );
                    stringRedisTemplate.convertAndSend(Server.SCHOOLBELL,
                            new SchoolAddMoneyDTO(orders.getSchoolId(), orders.getPayPrice().subtract(senderGet), senderGet).toJsonString()
                    );
                    // 增加积分
                    stringRedisTemplate.convertAndSend(Server.SENDERBELL,
                            new WxUserAddSourceDTO(orders.getOpenId(), orders.getPayPrice().intValue()).toJsonString()
                    );
                }
            } else {
                return;
            }
        }
        // 对订单进行结算
        OrdersComplete oc = new OrdersComplete(orders, schoolService.findById(orders.getSchoolId()),
                shopService.getById(orders.getShopId()), sender);
        orderCompleteService.save(oc);
        redisUtil.takeoutCountSuccessadd(orders.getSchoolId());
        stringRedisTemplate.convertAndSend(Server.PRODUCTADD, orderId);
        if (orders.getTyp().equals("外卖订单")) {
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
    public List<RunOrders> findorderbyrundjs(int senderId, int page, int size, String status) {
        Sender sender = senderService.findById(senderId);
        sender.setPage(page);
        sender.setSize(size);
        sender.setOrderBy(status);
        if (sender.getRunFlag() == 1) {
            return runOrdersService.findBySenderRun(sender);
        } else {
            return null;
        }
    }

    @Override
    public int acceptOrderRun(int senderId, String orderId) {
        Sender sender = senderService.findById(senderId);
        RunOrders orders = runOrdersService.getById(orderId);
        orders.setSenderId(senderId);
        orders.setSenderName(sender.getName());
        orders.setSenderPhone(sender.getPhone());
        int rs = 0;
        if ((rs = runOrdersService.senderAccept(orders)) == 1) {
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
    public void endRun(String orderId) {
        if (runOrdersService.end(orderId) == 1) {
            RunOrders orders = runOrdersService.getById(orderId);
            Sender sender = senderService.findById(orders.getSenderId());
            BigDecimal senderGet = orders.getTotalPrice().multiply(new BigDecimal(1).subtract(sender.getRate()));
            stringRedisTemplate.convertAndSend(Server.SENDERBELL,
                    new SenderAddMoneyDTO(sender.getOpenId(), senderGet).toJsonString()
            );
            stringRedisTemplate.convertAndSend(Server.SCHOOLBELL,
                    new SchoolAddMoneyDTO(orders.getSchoolId(), orders.getTotalPrice().subtract(senderGet), senderGet).toJsonString()
            );
            // senderAddMoney(orders.getTotalPrice(),orders.getSenderId());
            // 增加积分
            // addsource(orders.getOpenId(), orders.getTotalPrice().intValue());
            stringRedisTemplate.convertAndSend(Server.SENDERBELL,
                    new WxUserAddSourceDTO(orders.getOpenId(), orders.getTotalPrice().intValue()).toJsonString()
            );
            redisUtil.runCountSuccessadd(orders.getSchoolId());
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

    @Override
    public SenderTj statistics(int senderId, String beginTime, String endTime) {
        SenderTj rs = new SenderTj();
        Map<String, Object> map = new HashMap<>();
        map.put("senderId", senderId);
        map.put("beginTime", beginTime);
        map.put("endTime", endTime);
        List<Orders> result = ordersService.senderStatistics(map);
        List<RunOrders> result2 = runOrdersService.senderStatistics(map);
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

    @Transactional
    @Override
    public int tx2(String senderId, String userId) {
        Sender sender = senderService.check(senderId);
        WxUser wxUser = wxUserService.findById(userId);
        WxUser senderUser = wxUserService.findById(senderId);
        School school = schoolService.findById(sender.getSchoolId());
        WxUserBell wxUserBell = wxUserBellService
                .getById(senderUser.getOpenId() + "-" + senderUser.getPhone());
        Map<String, Object> map = new HashMap();
        map.put("phone", senderId + "-" + sender.getPhone());
        map.put("amount", wxUserBell.getMoney());
        map.put("schoolId", sender.getSchoolId());
        if (wxUserBellService.pay(map) == 1) {
            try {
                String payId = "tx" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                TxLog log = new TxLog(sender.getId(), "配送员提现", null, wxUserBell.getMoney(), "", sender.getSchoolId(),
                        wxUser.getAppId());
                if (WeChatPayUtil.transfers(school.getWxAppId(), school.getMchId(), school.getWxPayId(),
                        school.getCertPath(), payId, "127.0.0.1", wxUserBell.getMoney(), wxUser.getOpenId(),
                        log) == 1) {
                    txLogService.save(log);
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
