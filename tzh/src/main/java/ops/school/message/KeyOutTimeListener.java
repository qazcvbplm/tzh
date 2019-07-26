package ops.school.message;

import com.alibaba.fastjson.JSONArray;
import ops.school.api.entity.Orders;
import ops.school.api.entity.WxUser;
import ops.school.api.service.OrdersService;
import ops.school.api.service.SenderService;
import ops.school.api.service.WxUserService;
import ops.school.api.util.LoggerUtil;
import ops.school.message.dto.WxUserAddSourceDTO;
import ops.school.service.TOrdersService;
import ops.school.service.TSenderService;
import ops.school.util.WxMessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.util.List;

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
	
	@Override
	public void onMessage(Message key, byte[] arg1) {
		if(key.toString().startsWith("tsout")){
            Orders orders = ordersService.findById(key.toString().split(",")[1]);
			try {
                tSenderService.end(key.toString().split(",")[1], true);
                stringRedisTemplate.convertAndSend("bell", new WxUserAddSourceDTO(orders.getOpenId(), orders.getPayPrice().intValue()).toJsonString());
				WxUser wxUser = wxUserService.findById(orders.getOpenId());
                List<String> formIds = JSONArray.parseArray(stringRedisTemplate.boundHashOps("FORMID" + orders.getId()).values().toString(),String.class);
                if (formIds.size() > 0){
					// 自取或堂食订单完成，发送消息
					WxMessageUtil.wxSendMsg(orders,wxUser.getOpenId(),formIds.get(0));
					// 删除redis缓存
					stringRedisTemplate.boundHashOps("FORMID" + orders.getId()).delete(orders.getId());
				}
                // 自取堂食结算
				tOrdersService.orderSettlement(orders.getId());
			} catch (Exception e) {
				LoggerUtil.log("堂食完成失败:"+e.getMessage());
			}
					 //增加积分
		}
	}

}
