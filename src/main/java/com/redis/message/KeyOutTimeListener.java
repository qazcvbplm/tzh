package com.redis.message;

import com.dao.OrdersMapper;
import com.dto.redis.WxUserAddSourceDTO;
import com.entity.Orders;
import com.entity.WxUser;
import com.service.SenderService;
import com.service.WxUserService;
import com.util.LoggerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
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
	private OrdersMapper orderMapper;
	@Autowired
	private WxUserService wxUserService;
	
	@Override
	public void onMessage(Message key, byte[] arg1) {
		if(key.toString().startsWith("tsout")){
			Orders orders = orderMapper.selectByPrimaryKey(key.toString().split(",")[1]);
			try {
				senderService.end(key.toString().split(",")[1],true);
				   stringRedisTemplate.convertAndSend("bell", new WxUserAddSourceDTO(orders.getOpenId(), orders.getPayPrice().intValue()).toJsonString());
				WxUser wxUser = wxUserService.findById(orders.getOpenId());
				wxUserService.sendWXGZHM(wxUser.getPhone(), new com.dto.wxgzh.Message(null,
						"8Qy7KQRt2upGjwmhp7yYaR2ycfKkXNI8gqRvGBnovsk",
						null, null,
						"您的" + orders.getTyp() + "已经自动完成!", orders.getId(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
						null, null, null, null, null, null,
						null, "成功获得" + orders.getPayPrice().intValue() + "积分，可以前往积分商城兑换哟！"));

			} catch (Exception e) {
				LoggerUtil.log("堂食完成失败:"+e.getMessage());
			}
					 //增加积分
		}
	}

}
