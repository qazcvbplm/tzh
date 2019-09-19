package ops.school.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import io.swagger.models.auth.In;
import ops.school.App;
import ops.school.api.dao.OrdersCompleteMapper;
import ops.school.api.entity.*;
import ops.school.api.service.*;
import ops.school.api.util.LoggerUtil;
import ops.school.api.util.PublicUtilS;
import ops.school.api.util.ResponseObject;
import ops.school.api.util.TimeUtilS;
import ops.school.api.wxutil.WxMessageUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.awt.*;
import java.io.Serializable;
import java.util.*;
import java.util.List;

import static org.junit.Assert.*;

/**
 * CreatebyFang
 * fangfor@outlook.com
 * 2019/9/7
 * 16:15
 * #
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class OrdersServiceTest {
    private Logger logger = LoggerFactory.getLogger(OrdersServiceTest.class);

    @Autowired
    private TOrdersService tOrdersService;

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private CouponService couponService;

    @Autowired
    private OrdersCompleteMapper ordersCompleteMapper;

    @Autowired
    private WxUserCouponService wxUserCouponService;

    @Autowired
    private SenderService senderService;

    @Autowired
    private WxUserService wxUserService;

    @Autowired
    private TSenderService tSenderService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private OrderProductService orderProductService;

    @Test
    public void keyOrderCompleteTest(){
        List<String> orderList = new ArrayList<>();
//        orderList.add("201909171149038394489931615");
//        orderList.add("201909171149371356568796785");
//        orderList.add("201909171151580221681265288");
//        orderList.add("201909171208128175899961796");
//        orderList.add("201909171215418237112321213");
//        orderList.add("201909171219486911119386998");
//        orderList.add("201909171223213309511382980");
//        orderList.add("201909171226374859555646571");
//        orderList.add("201909171258277409860776773");
//        orderList.add("201909171315445596956865554");
        for (String orderId : orderList) {
            this.doOrderComplete(orderId);
        }


    }

    private void doOrderComplete(String orderId) {
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
                    //WxMessageUtil.wxSendMsg(orders, formIds.get(0), orders.getSchoolId());
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
