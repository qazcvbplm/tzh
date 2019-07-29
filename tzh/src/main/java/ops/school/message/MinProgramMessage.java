package ops.school.message;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rabbitmq.client.Channel;
import ops.school.api.entity.OrderProduct;
import ops.school.api.entity.Orders;
import ops.school.api.service.OrderProductService;
import ops.school.config.RabbitMQConfig;
import ops.school.util.WxMessageUtil;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;


@Component
@RabbitListener(queues = RabbitMQConfig.QUEUE_MIN_PROGRAM_MESSAGE)
public class MinProgramMessage {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private OrderProductService orderProductService;

    @RabbitHandler
    public void receiveMessage(String obj, Channel channel, Message message) {
        Orders orders = JSON.parseObject(obj, Orders.class);
        // 微信小程序推送消息
        // 从redis中获取的formId,判断是否为空
        if (stringRedisTemplate.boundListOps("FORMID" + orders.getId()).size() > 0) {
            orders.setOp(orderProductService.list(new QueryWrapper<OrderProduct>()
                    .lambda().eq(OrderProduct::getOrderId, orders.getId())));
            WxMessageUtil.wxSendMsg(orders, stringRedisTemplate.boundListOps("FORMID" + orders.getId()).rightPop());
        }
        if (orders.getStatus().equals("已完成")) {
            stringRedisTemplate.delete("FORMID" + orders.getId());
        }
    }
}
