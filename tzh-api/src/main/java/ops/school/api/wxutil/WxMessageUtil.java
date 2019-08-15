package ops.school.api.wxutil;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import ops.school.api.dto.wxgzh.Message;
import ops.school.api.entity.OrderProduct;
import ops.school.api.entity.Orders;
import ops.school.api.entity.RunOrders;
import ops.school.api.enums.ResponseViewEnums;
import ops.school.api.exception.Assertions;
import ops.school.api.service.OrderProductService;
import ops.school.api.util.LoggerUtil;
import ops.school.api.wxutil.WxGUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class WxMessageUtil {


    private static final String templateId = "Wg-yNBXd6CvtYcDTCa17Qy6XEGPeD2iibo9rU2ng67o";

    // 商家接手跳转页面/配送员已接手
    private static final String acceptOrder = "pages/order/orderDetail/orderDetail?orderId=";

    // 跑腿订单/外卖订单完成
    private static final String orderFinish = "pages/mine/integral/integral";

    // 商家接手备注
    private static final String shopAcceptRemark = "商家正火速备餐中，请耐心等待";

    // 配送员接手备注（含跑腿）
    private static final String senderAcceptRemark = "配送员正火速配送中，请耐心等待";

    // 配送员送达备注(楼上)
    private static final String senderOrderFinishRemarkTop = "订单已送达到寝，并获得";

    // 配送员送达备注(楼下)
    private static final String senderOrderFinishRemarkDown = "订单已放置楼下，请及时下楼自取。系统返还";

    // 跑腿订单送达
    private static final String runOrderFinishRemark = "订单已完成，并获得";

    // 自取或堂食自动完成
    private static final String selfTakingAndTSFinishRemark = "订单已自动完成，并获得";

    // 积分查收
    private static final String sourceAccept = "的积分奖励，请注意查收";


    public static void wxSendMsg(Orders orders, String formid) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Message message = new Message();
        message.setToUser(orders.getOpenId());
        message.setTemplateId(templateId);
        // 订单编号
        message.setDataKeyWord3(orders.getId());
        // 更新时间
        message.setDataKeyWord4(df.format(new Date()));
        // formid
        message.setFormId(formid);
        // 放大内容
        message.setEmphasisKeyword("keyword1.DATA");
        // 订单商品表信息
        List<OrderProduct> list = orders.getOp();
        /**
         * 外卖订单
         */
        if (orders.getTyp().equals("外卖订单")){
            // 订单流水号
            message.setDataKeyWord2(orders.getWaterNumber()+"");
            // 订单内容
            message.setDataKeyWord5(list.get(0).getProductName()+"等");
            if (orders.getStatus().equals("待接手")){
                // 订单状态
                message.setDataKeyWord1("商家已接手");
                // 订单流水号
                message.setDataKeyWord2(orders.getWaterNumber()+"");
                // 订单内容
                message.setDataKeyWord5(list.get(0).getProductName()+"等");
                // 订单备注
                message.setDataKeyWord6(shopAcceptRemark);
                // 接手订单跳转页面
                message.setMinPath(acceptOrder + orders.getId() + "&typ=" + orders.getTyp());
            } else if (orders.getStatus().equals("商家已接手")){
                // 订单状态
                message.setDataKeyWord1("配送员已接手");
                // 订单备注
                message.setDataKeyWord6(senderAcceptRemark);
                // 接手订单跳转页面
                message.setMinPath(acceptOrder + orders.getId() + "&typ=" + orders.getTyp());
            } else if (orders.getStatus().equals("配送员已接手")){
                // 订单状态
                message.setDataKeyWord1("已完成");
                if (orders.getDestination() == 0){
                    // 订单备注
                    message.setDataKeyWord6(senderOrderFinishRemarkDown
                            + orders.getSchoolTopDownPrice()+"元粮票，并获得"
                            + orders.getPayPrice() + sourceAccept);
                } else if (orders.getDestination() == 1){
                    // 订单备注
                    message.setDataKeyWord6(senderOrderFinishRemarkTop
                            + orders.getPayPrice() +sourceAccept);
                }
                // 接手订单跳转页面
                message.setMinPath(orderFinish);
            }
            // 发送消息模板
            // 发送消息模板
            try{
                WxGUtil.snedM(message.toJson());
            }catch (Exception ex){
                LoggerUtil.logError("微信发送模板消息报错："+ex.getMessage());
            }
        }
        else if(orders.getTyp().equals("自取订单") || orders.getTyp().equals("堂食订单")){
            /**
             * 自取或堂食订单
             */
            if (orders.getStatus().equals("待接手")){
                // 订单状态
                message.setDataKeyWord1("已完成");

                // 订单流水号
                message.setDataKeyWord2(orders.getWaterNumber()+"");
                // 订单内容
                message.setDataKeyWord5(list.get(0).getProductName()+"等");
                // 订单备注
                message.setDataKeyWord6(selfTakingAndTSFinishRemark
                        + orders.getPayPrice() + sourceAccept);
                // 跳转页面
                message.setMinPath(null);
                // 发送消息模板
                // 发送消息模板
                try{
                    WxGUtil.snedM(message.toJson());
                }catch (Exception ex){
                    LoggerUtil.logError("微信发送模板消息报错："+ex.getMessage());
                }
            }

        }
    }

    public static void wxRunOrderSendMsg(RunOrders runOrders, String openid, String formid){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Message message = new Message();
        message.setToUser(openid);
        message.setTemplateId(templateId);
        // 订单编号
        message.setDataKeyWord3(runOrders.getId());
        // 更新时间
        message.setDataKeyWord4(df.format(new Date()));
        // formid
        message.setFormId(formid);
        // 放大内容
        message.setEmphasisKeyword("keyword1.DATA");
        // 订单流水号
        message.setDataKeyWord2("该订单暂无编号");
        // 订单内容
        message.setDataKeyWord5(runOrders.getContent());
        if (runOrders.getStatus().equals("待接手")){
            message.setDataKeyWord1("配送员已接手");
            message.setDataKeyWord6(senderAcceptRemark);
            message.setMinPath(acceptOrder);
        } else if (runOrders.getStatus().equals("配送员已接手")) {
            message.setDataKeyWord1("已完成");
            message.setDataKeyWord6(runOrderFinishRemark
                    + runOrders.getTotalPrice() + sourceAccept);
            message.setMinPath(orderFinish);
        }
        // 发送消息模板
        try{
            WxGUtil.snedM(message.toJson());
        }catch (Exception ex){
            LoggerUtil.logError("微信发送模板消息报错："+ex.getMessage());
        }

    }

}
