package ops.school.message;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import ops.school.api.entity.OrderProduct;
import ops.school.api.entity.Orders;
import ops.school.api.entity.WxUser;
import ops.school.api.service.*;
import ops.school.api.util.LoggerUtil;
import ops.school.api.wxutil.WxMessageUtil;
import ops.school.config.RabbitMQConfig;
import ops.school.message.dto.WxUserAddSourceDTO;
import ops.school.service.TOrdersService;
import ops.school.service.TSenderService;
import ops.school.service.TWxUserService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class KeyOutTimeListener extends KeyExpirationEventMessageListener {

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
    private WxUserBellService wxUserBellService;

	@Autowired
	private OrderProductService orderProductService;

    @Override
    public void onMessage(Message key, byte[] arg1) {
        if (key.toString().startsWith("tsout")) {
        	String orderId = key.toString().split(",")[1];
            Orders orders = ordersService.findById(orderId);
            try {
                //完结订单并结算
                tSenderService.tsTakeOutOrdersEnd(orderId, true);
                //发送用户消息，已完成
                // 微信小程序推送消息
                List<String> formIds = new ArrayList<>();
                try {
                    formIds = stringRedisTemplate.boundListOps("FORMID" + orders.getId()).range(0, -1);
                } catch (Exception ex) {
                    LoggerUtil.logError("商家接手外卖订单-shopAcceptOrderById-完成发送消息失败，formid取缓存为空" + orders.getId());
                }
                if (formIds.size() > 0) {
                    // 查询订单商品表信息
                    QueryWrapper<OrderProduct> productWrapper = new QueryWrapper<>();
                    productWrapper.lambda().eq(OrderProduct::getOrderId, orders.getId());
                    List<OrderProduct> orderProducts = orderProductService.list(productWrapper);
                    orders.setOp(orderProducts);
                    orders.setStatus("商家已接手");
                    try {
                        WxMessageUtil.wxSendMsg(orders, formIds.get(0), orders.getSchoolId());
                        stringRedisTemplate.boundListOps("FORMID" + orders.getId()).remove(1, formIds.get(0));
                    } catch (Exception ex) {
                        LoggerUtil.logError("wx发送消息失败-shopAcceptOrderById-" + ex.getMessage());
                    }
                } else {
                    LoggerUtil.logError("商家接手自取堂食订单-KeyOutTimeListener-完成发送消息失败，发送失败-formid为空" + orders.getId());
                }
            } catch (Exception e) {
                LoggerUtil.log("堂食完成失败:" + e.getMessage());
            }
        }
    }

}
