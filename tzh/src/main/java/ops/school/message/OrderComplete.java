package ops.school.message;

import com.rabbitmq.client.Channel;
import ops.school.config.RabbitMQConfig;
import ops.school.service.TOrdersService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RabbitListener(queues = RabbitMQConfig.QUEUE_ORDERS_COMPLETE)
public class OrderComplete {

    @Autowired
    private TOrdersService tOrdersService;

    @RabbitHandler
    public void orderSettlement(String msg, Channel channel, Message message) throws IOException {
        tOrdersService.orderSettlement(msg);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
