//package ops.school.message;
//
//import com.alibaba.fastjson.JSON;
//import com.rabbitmq.client.Channel;
//import ops.school.api.util.LoggerUtil;
//import ops.school.config.RabbitMQConfig;
//import ops.school.message.dto.SenderAddMoneyDTO;
//import org.springframework.amqp.core.Message;
//import org.springframework.amqp.rabbit.annotation.RabbitHandler;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.stereotype.Service;
//
//import java.math.BigDecimal;
//
///**
// * CreatebyFang
// * fangfor@outlook.com
// * 2019/8/3
// * 16:17
// * # 下单之后配送员加配送费
// */
//
//@Service
//@RabbitListener(queues = RabbitMQConfig.QUEUE_ORDER_END_SENDER_COUNT)
//public class OrderEndSenderCountListener {
//
//
//    @RabbitHandler
//    public void senderAddMoney(String obj, Channel channel, Message message) {
//        SenderAddMoneyDTO senderAddMoneyDTO = JSON.parseObject(obj, SenderAddMoneyDTO.class);
//        BigDecimal amount = senderAddMoneyDTO.getAmount().setScale(2, BigDecimal.ROUND_HALF_DOWN);
//        String openId = senderAddMoneyDTO.getOpenId();
//        try {
//            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
//        } catch (Exception ex) {
//            LoggerUtil.logError("rabbitMQ手动确定消息失败："+OrderEndSenderCountListener.class.getName());
//        }
//
//    }
//
//
//}
