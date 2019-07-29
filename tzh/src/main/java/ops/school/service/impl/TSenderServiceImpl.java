package ops.school.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import ops.school.api.dto.SenderTj;
import ops.school.api.entity.*;
import ops.school.api.enums.ResponseViewEnums;
import ops.school.api.exception.Assertions;
import ops.school.api.service.*;
import ops.school.api.util.RedisUtil;
import ops.school.config.RabbitMQConfig;
import ops.school.message.dto.SchoolAddMoneyDTO;
import ops.school.message.dto.SenderAddMoneyDTO;
import ops.school.message.dto.WxUserAddSourceDTO;
import ops.school.service.TSenderService;
import ops.school.util.WxMessageUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
    private OrderProductService orderProductService;
    @Autowired
    private WxUserBellService wxUserBellService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RunOrdersService runOrdersService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private RabbitTemplate rabbitTemplate;


    @Override
    public List<Orders> findorderbydjs(Integer senderId, Integer page, Integer size, String status) {
        Sender sender = senderService.findById(senderId);
        sender.setPage((page-1)*size);
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

    @Transactional
    @Override
    public Integer acceptOrder(Integer senderId, String orderId) {
        Sender sender = senderService.findById(senderId);
        Orders orders = ordersService.findById(orderId);
        orders.setSenderId(senderId);
        orders.setSenderName(sender.getName());
        orders.setSenderPhone(sender.getPhone());
        int rs = 0;
        if ((rs = ordersService.senderAccept(orders)) == 1) {
            rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_MIN_PROGRAM_MESSAGE, JSON.toJSONString(orders));
        }
        return rs;
    }

    @Transactional
    @Override
    public void end(String orderId, boolean end) {
        Orders orders = ordersService.findById(orderId);
        Sender sender = senderService.findById(orders.getSenderId());
        WxUser wxUser = wxUserService.findById(orders.getOpenId());
        BigDecimal returnPrice = new BigDecimal("0");
        if (end) {
            // 已送达到楼上
            orders.setDestination(1);
            if (ordersService.end(orders) == 1) {
//                BigDecimal senderGet = new BigDecimal(0);
//                if (orders.getSenderId() != 0) {
//                    // 配送员获得金额
//                    senderGet = orders.getSendPrice().multiply(new BigDecimal(1).subtract(sender.getRate()));
//                    stringRedisTemplate.convertAndSend(Server.SENDERBELL,
//                            new SenderAddMoneyDTO(sender.getOpenId(), senderGet).toJsonString()
//                    );
//                }
//                stringRedisTemplate.convertAndSend(Server.SCHOOLBELL,
//                        new SchoolAddMoneyDTO(orders.getSchoolId(), orders.getPayPrice().subtract(senderGet), senderGet).toJsonString()
//                );
//                // 增加积分
//                stringRedisTemplate.convertAndSend(Server.SENDERBELL,
//                        new WxUserAddSourceDTO(orders.getOpenId(), orders.getPayPrice().intValue()).toJsonString()
//                );

            } else {
                return;
            }
        } else {
            // 放置楼下
            orders.setDestination(0);
            // 配送费 < 楼上楼下差价
            if (orders.getSendPrice().compareTo(orders.getSchoolTopDownPrice()) == -1) {
                /*orders.setPayPrice(orders.getPayPrice().subtract(orders.getSendPrice()));
                // 配送费设为0，返还粮票为0*/
                orders.setSendPrice(new BigDecimal(0));
                returnPrice = orders.getSendPrice();
            } else {
                /*orders.setPayPrice(orders.getPayPrice().subtract(orders.getSchoolTopDownPrice()));
                orders.setSendPrice(orders.getSendPrice().subtract(orders.getSchoolTopDownPrice()));*/
                returnPrice = orders.getSchoolTopDownPrice();
            }
            // 楼下取餐会获得楼下返还
            if (ordersService.end(orders) == 1) {
                // 将楼下返还金额充值到用户的粮票内
                if (wxUserBellService.addFoodCoupon(wxUser.getOpenId() + "-" + wxUser.getPhone(), returnPrice)) {
                    // 将用户楼下返还金额添加到学校剩余粮票总额内
                    School school = schoolService.findById(orders.getSchoolId());
                    school.setUserChargeSend(school.getUserChargeSend().add(returnPrice));
                    schoolService.updateById(school);
//                    BigDecimal senderGet = orders.getSendPrice().multiply(new BigDecimal(1).subtract(sender.getRate()));
//                    stringRedisTemplate.convertAndSend(Server.SENDERBELL,
//                            new SenderAddMoneyDTO(sender.getOpenId(), senderGet).toJsonString()
//                    );
//                    stringRedisTemplate.convertAndSend(Server.SCHOOLBELL,
//                            new SchoolAddMoneyDTO(orders.getSchoolId(), orders.getPayPrice().subtract(senderGet), senderGet).toJsonString()
//                    );
//                    // 增加积分
//                    stringRedisTemplate.convertAndSend(Server.SENDERBELL,
//                            new WxUserAddSourceDTO(orders.getOpenId(), orders.getPayPrice().intValue()).toJsonString()
//                    );
                }
            } else {
                return;
            }
        }
        redisUtil.takeoutCountSuccessadd(orders.getSchoolId());
        rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_PRODUCT_ADD, orderId);
        if (orders.getTyp().equals("外卖订单")) {
            orders.setStatus("已完成");
            //发送模板消息
            rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_MIN_PROGRAM_MESSAGE, JSON.toJSONString(orders));
        }
        //结算
        rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_ORDERS_COMPLETE, orders.getId());
    }

    @Override
    public List<RunOrders> findorderbyrundjs(int senderId, int page, int size, String status) {
        Sender sender = senderService.findById(senderId);
        sender.setPage((page-1)*size);
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
            List<String> formIds = JSONArray.parseArray(stringRedisTemplate.boundHashOps("FORMID" + orders.getId()).values().toString(),String.class);
            if (formIds.size() > 0){
                WxMessageUtil.wxRunOrderSendMsg(orders,wxUser.getOpenId(),formIds.get(0));
            }
        }
        return rs;
    }


    @Override
    public void endRun(String orderId) {
        if (runOrdersService.end(orderId) == 1) {
            RunOrders orders = runOrdersService.getById(orderId);
            Sender sender = senderService.findById(orders.getSenderId());
            // 对配送员信息进行校验
            Assertions.notNull(sender, ResponseViewEnums.SCHOOL_HAD_CHANGE);
            School school = schoolService.findById(sender.getSchoolId());
            // 对学校信息进行校验
            Assertions.notNull(school, ResponseViewEnums.SCHOOL_HAD_CHANGE);
            /**
             * 超级后台所得
             */
            // 超级后台所得对应金额 --> 支付价格＊  比例
            BigDecimal appGetTotal = orders.getTotalPrice().multiply(school.getRate());
            // 配送员所得 -->
            BigDecimal senderGet = orders.getTotalPrice().multiply(new BigDecimal(1).subtract(sender.getRate()));
            /**
             * 负责人所得
             */
            BigDecimal schoolGet = orders.getTotalPrice().subtract(senderGet).subtract(appGetTotal);
            /**
             * 将配送员所得存进redis缓存
             */
            rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_SENDER_BELL,
                    new SenderAddMoneyDTO(sender.getOpenId(), senderGet).toJsonString()
            );
            /**
             * 将负责人所得存进redis缓存
             */
            rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_SCHOOL_BELL,
                    new SchoolAddMoneyDTO(orders.getSchoolId(), schoolGet, senderGet).toJsonString()
            );
            // senderAddMoney(orders.getTotalPrice(),orders.getSenderId());
            // 增加积分
            // addsource(orders.getOpenId(), orders.getTotalPrice().intValue());
            rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_WX_USER_BELL,
                    new WxUserAddSourceDTO(orders.getOpenId(), orders.getTotalPrice().intValue()).toJsonString()
            );
            redisUtil.runCountSuccessadd(orders.getSchoolId());
            WxUser wxUser = wxUserService.findById(orders.getOpenId());
            // 从Redis里面获取
            List<String> formIds = JSONArray.parseArray(stringRedisTemplate.boundHashOps("FORMID" + orders.getId()).values().toString(),String.class);
            if (formIds.size() > 0){
                // 发送微信信息模板
                WxMessageUtil.wxRunOrderSendMsg(orders,wxUser.getOpenId(),formIds.get(1));
                // 删除redis缓存
                stringRedisTemplate.boundHashOps("FORMID" + orders.getId()).delete(orders.getId());
            }
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
}
