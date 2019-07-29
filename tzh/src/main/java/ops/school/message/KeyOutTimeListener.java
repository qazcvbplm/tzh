package ops.school.message;

import com.alibaba.fastjson.JSON;
import ops.school.api.entity.Orders;
import ops.school.api.service.OrdersService;
import ops.school.api.service.SenderService;
import ops.school.api.service.WxUserService;
import ops.school.api.util.LoggerUtil;
import ops.school.config.RabbitMQConfig;
import ops.school.message.dto.WxUserAddSourceDTO;
import ops.school.service.TOrdersService;
import ops.school.service.TSenderService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
public class KeyOutTimeListener extends KeyExpirationEventMessageListener{

	public KeyOutTimeListener(RedisMessageListenerContainer listenerContainer) {
		super(listenerContainer);
	}
	
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	@Autowired
	private SenderService senderService;
	@Autowired
    private OrdersService ordersService;
	@Autowired
	private WxUserService wxUserService;
    @Autowired
    private TSenderService tSenderService;
    @Autowired
	private TOrdersService tOrdersService;
    @Autowired
    private RabbitTemplate rabbitTemplate;
	
	@Override
	public void onMessage(Message key, byte[] arg1) {
		if(key.toString().startsWith("tsout")){
            Orders orders = ordersService.findById(key.toString().split(",")[1]);
			try {
                tSenderService.end(key.toString().split(",")[1], true);
                rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_WX_USER_BELL, new WxUserAddSourceDTO(orders.getOpenId(), orders.getPayPrice().intValue()).toJsonString());
                // 自取堂食结算
				tOrdersService.orderSettlement(orders.getId());
                orders.setStatus("已完成");
                rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_MIN_PROGRAM_MESSAGE, JSON.toJSONString(orders));
			} catch (Exception e) {
				LoggerUtil.log("堂食完成失败:"+e.getMessage());
			}
					 //增加积分
		}
	}

}
