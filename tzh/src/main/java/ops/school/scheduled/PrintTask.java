package ops.school.scheduled;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import ops.school.api.constants.NumConstants;
import ops.school.api.constants.RedisConstants;
import ops.school.api.constants.ShopPrintConfigConstants;
import ops.school.api.dto.print.PrintDataDTO;
import ops.school.api.dto.print.ShopPrintResultDTO;
import ops.school.api.entity.OrderProduct;
import ops.school.api.entity.Orders;
import ops.school.api.enums.ResponseViewEnums;
import ops.school.api.exception.Assertions;
import ops.school.api.service.OrderProductService;
import ops.school.api.service.OrdersService;
import ops.school.api.serviceimple.OrdersServiceImple;
import ops.school.api.util.LoggerUtil;
import ops.school.api.util.ShopPrintUtils;
import ops.school.util.WxMessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * CreatebyFang
 * fangfor@outlook.com
 * 2019/8/12
 * 20:22
 * #
 */

@Component
@Service(value = "printTask")
public class PrintTask {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Autowired
    private OrdersService ordersService;

    @Autowired
    private OrderProductService orderProductService;


    /**
     * 每隔1分钟查询打印一次
     */
    @Scheduled(cron = "* */1 * * * ?")
    public void doPrintAndAcceptOrder() {
        while (true) {
            Long redisSize = stringRedisTemplate.boundListOps("Shop_Wait_Print_OId_List").size();
            if (redisSize < NumConstants.INT_NUM_1){
                try {
                    //没有订单等xs
                    Thread.sleep(20*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    LoggerUtil.log(e.getMessage());
                }
                System.out.println("没有订单循环20s等待");
                continue;
            }
            String findOrder = stringRedisTemplate.boundListOps("Shop_Wait_Print_OId_List").rightPop();
            if (findOrder == null){
                return;
            }
            PrintDataDTO printDataDTO = JSONObject.parseObject(findOrder,PrintDataDTO.class);
            //先去查询店铺redis待接手
            Object getRedisOrder = stringRedisTemplate.boundHashOps("SHOP_DJS" + printDataDTO.getOurShopId()).get(printDataDTO.getOurOrderId());
            Orders orders = null;
            if (getRedisOrder == null){
                //如果是null，说明已经处理过了，跳过
                continue;
            }else{
                orders = JSONObject.parseObject(getRedisOrder.toString(),Orders.class);
                if (!"待接手".equals(orders.getStatus())){
                    continue;
                }
            }
            orders.setWaterNumber(printDataDTO.getWaterNumber());
            //订单待接手状态，需要处理
            if (printDataDTO.getPrintBrand().intValue() == ShopPrintConfigConstants.PRINT_BRAND_DB_FEI_E){
                //查询飞鹅打印
                ShopPrintResultDTO<Boolean> printResult = ShopPrintUtils.feiEGetPrintStatusYes(printDataDTO.getPlatePrintOrderId());

                //如果已打印变成状态
                if (printResult != null && printResult.isSuccess()){
                    Boolean updateTrue = this.changeOrderStatusToJS(printDataDTO);
                    if (!updateTrue){
                        Orders ordersSelect2 = ordersService.findById(printDataDTO.getOurOrderId());
                        if (ordersSelect2 == null && "商家已接手".equals(ordersSelect2.getStatus())){
                            //如果更改订单状态失败
                            LoggerUtil.logError("doPrintAndAcceptOrder-修改订单状态失败-"+printDataDTO.getOurOrderId());
                            stringRedisTemplate.boundListOps("Shop_Wait_Print_OId_List").leftPush(JSON.toJSONString(printDataDTO));
                        }else if ("已取消".equals(ordersSelect2.getStatus()) || "已完成".equals(ordersSelect2.getStatus())){
                            //同时尝试删除缓存待接手
                            Long delNum = stringRedisTemplate.boundHashOps("SHOP_DJS" + printDataDTO.getOurShopId()).delete(printDataDTO.getOurOrderId());
                            Long delALLNum = stringRedisTemplate.boundHashOps("ALL_DJS").delete(printDataDTO.getOurOrderId().toString());
                            continue;
                        }
                        continue;
                    }
                    //修改成功商家已接手 推送用户消息
                    Boolean sendTrue = wxSendOrderMsgByOrder(orders);
                    if (!sendTrue){
                        //发送失败是不会重新循环处理的
                        LoggerUtil.logError("doPrintAndAcceptOrder-修改订单状态后发送模板消息失败-"+printDataDTO.getOurOrderId());
                    }
                    continue;

                }else {
                    //记录日志，只记第一次或者最后一次
                    if (printDataDTO.getCycleRedisCount() == 0 || printDataDTO.getCycleRedisCount() == NumConstants.INT_NUM_60){
                        LoggerUtil.logError("系统记录-定时器查询飞鹅打印机-doPrintAndAcceptOrder-日志" + printResult.getMsg()+printDataDTO.toString());
                    }
                    //如果未打印，跳过,放入失败队列，没两分钟回来再查询,只能循环20次
                    if (printDataDTO.getCycleRedisCount() <= NumConstants.INT_NUM_60 ){
                        try {
                            //没有订单等xs
                            Thread.sleep(20*1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            LoggerUtil.log(e.getMessage());
                        }
                        System.out.println("如果未打印阻塞20重试");
                        printDataDTO.setCycleRedisCount(printDataDTO.getCycleRedisCount() + NumConstants.INT_NUM_1);
                        Long listIndex = stringRedisTemplate.boundListOps(RedisConstants.Shop_Wait_Print_OId_List).leftPush(JSON.toJSONString(printDataDTO));
                    }

                    //加入失败队列
                    continue;
                }
            }else if (printDataDTO.getPrintBrand().intValue() == ShopPrintConfigConstants.PRINT_BRAND_DB_FE_YIN){
                LoggerUtil.logError("系统记录-定时器查询打印机-此订单是飞印打印不能处理-信息-"+printDataDTO.toString());
                continue;
            }
        }
    }


    /**
     * @date:   2019/8/13 11:56
     * @author: QinDaoFang
     * @version:version
     * @return: void
     * @param   printDataDTO
     * @Desc:   desc 把订单状态改成已接手
     */
    private Boolean changeOrderStatusToJS(PrintDataDTO printDataDTO) {
        //不做空判断，前面一定判断
        //先改数据库
        Orders update = new Orders();
        update.setWaterNumber(printDataDTO.getWaterNumber());
        update.setId(printDataDTO.getOurOrderId());
        Integer updateNum = ordersService.shopAcceptOrderById(update);
        if (updateNum != NumConstants.INT_NUM_1){
            //如果失败
            return false;
        }
        //再改redis
        Long delNum = stringRedisTemplate.boundHashOps("SHOP_DJS" + printDataDTO.getOurShopId()).delete(printDataDTO.getOurOrderId());
        if (delNum.intValue() < NumConstants.INT_NUM_1){
            //如果失败 只是记录日志
            LoggerUtil.logError("定时器-把订单状态改成已接手-changeOrderStatusToJS-删除缓存失败-"+"SHOP_DJS" + printDataDTO.getOurShopId()+printDataDTO.getOurOrderId());
        }
        return true;
    }


    public  Boolean wxSendOrderMsgByOrder(Orders orders) {
        Assertions.notNull(orders, ResponseViewEnums.SEND_WX_MESSAGE_ERROR_NO_PARAMS);
        Assertions.notNull(orders.getId(), ResponseViewEnums.SEND_WX_MESSAGE_ERROR_NO_PARAMS);
        List<String> formIds = new ArrayList<>();
        try {
            Long redisSize = stringRedisTemplate.opsForList().size("FORMID" + orders.getId());
            formIds = stringRedisTemplate.opsForList().range("FORMID" + orders.getId(),0,-1);
        } catch (Exception ex) {
            LoggerUtil.logError("发送微信模板消息-商家接手类-wxSendOrderMsg-完成发送消息失败，formid取缓存为空" + orders.getId());
            return false;
        }
        if (formIds.size() > 0) {
            // 查询订单商品表信息
            QueryWrapper<OrderProduct> productWrapper = new QueryWrapper<>();
            productWrapper.lambda().eq(OrderProduct::getOrderId, orders.getId());
            List<OrderProduct> orderProducts = orderProductService.list(productWrapper);
            orders.setOp(orderProducts);
            orders.setStatus("待接手");
            WxMessageUtil.wxSendMsg(orders, formIds.get(0));
            stringRedisTemplate.boundListOps("FORMID" + orders.getId()).remove(1, formIds.get(0));
        } else {
            LoggerUtil.logError("发送微信模板消息-商家接手类-wxSendOrderMsg-完成发送消息失败，发送或者删除redis失败" + orders.getId());
            return false;
        }
        return true;
    }
}

