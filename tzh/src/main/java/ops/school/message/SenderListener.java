//package ops.school.message;
//
//import com.alibaba.fastjson.JSON;
//import com.rabbitmq.client.Channel;
//import ops.school.api.entity.Logs;
//import ops.school.api.entity.WxUser;
//import ops.school.api.service.LogsService;
//import ops.school.api.service.WxUserBellService;
//import ops.school.api.service.WxUserService;
//import ops.school.api.util.LoggerUtil;
//import ops.school.config.RabbitMQConfig;
//import ops.school.message.dto.SenderAddMoneyDTO;
//import org.springframework.amqp.core.Message;
//import org.springframework.amqp.rabbit.annotation.RabbitHandler;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.math.BigDecimal;
//import java.util.HashMap;
//import java.util.Map;
//
//@Component
//@RabbitListener(queues = RabbitMQConfig.QUEUE_SENDER_BELL)
//public class SenderListener {
//
//    @Autowired
//    private WxUserService wxUserService;
//    @Autowired
//    private WxUserBellService wxUserBellService;
//    @Autowired
//    private LogsService logsService;
//
//    @RabbitHandler
//    public void senderAddMoney(String obj, Channel channel, Message message) {
//        SenderAddMoneyDTO senderAddMoneyDTO = JSON.parseObject(obj, SenderAddMoneyDTO.class);
//        BigDecimal amount = senderAddMoneyDTO.getAmount().setScale(2, BigDecimal.ROUND_HALF_DOWN);
//        WxUser wxUser = wxUserService.findById(senderAddMoneyDTO.getOpenId());
//        Map<String, Object> map = new HashMap<>();
//        map.put("phone", wxUser.getOpenId() + "-" + wxUser.getPhone());
//        map.put("amount", amount);
//        if (wxUserBellService.charge(map) == 0) {
//            logsService.save(new Logs("配送员送达订单增加余额失败：" + wxUser.getOpenId() + "-" + wxUser.getPhone() + "," + amount.toString()));
//        }
//        try {
//            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
//        } catch (Exception ex) {
//            LoggerUtil.logError("rabbitMQ手动确定消息失败："+SenderListener.class.getName());
//        }
//
//    }
//}
