package ops.school.scheduled;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import ops.school.api.constants.NumConstants;
import ops.school.api.constants.RedisConstants;
import ops.school.api.constants.ShopPrintConfigConstants;
import ops.school.api.dto.print.PrintDataDTO;
import ops.school.api.dto.print.ShopPrintResultDTO;
import ops.school.api.entity.Orders;
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


    /**
     * 每隔1分钟查询打印一次
     */
    @Scheduled(cron = "0 */1 * * * ?")
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
                if (printResult == null || !printResult.isSuccess()){
                    LoggerUtil.logError("系统记录-定时器查询飞鹅打印机-doPrintAndAcceptOrder-日志" + printResult.getMsg()+printDataDTO.toString());
                }
                //如果已打印变成状态
                if (printResult.isSuccess()){
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
                    Boolean sendTrue = WxMessageUtil.wxSendOrderMsgByOrder(orders);
                    if (!sendTrue){
                        //发送失败是不会重新循环处理的
                        LoggerUtil.logError("doPrintAndAcceptOrder-修改订单状态后发送模板消息失败-"+printDataDTO.getOurOrderId());
                    }
                    continue;

                }else {
                    //如果未打印，跳过,放入失败队列，没两分钟回来再查询,只能循环20次
                    if (printDataDTO.getCycleRedisCount() < NumConstants.INT_NUM_10 ){
                        printDataDTO.setCycleRedisCount(printDataDTO.getCycleRedisCount() + NumConstants.INT_NUM_1);
                        Long listIndex = stringRedisTemplate.boundListOps(RedisConstants.Shop_Failed_Print_OId_List).leftPush(JSON.toJSONString(printDataDTO));
                    }
                    continue;
                }
            }else if (printDataDTO.getPrintBrand().intValue() == ShopPrintConfigConstants.PRINT_BRAND_DB_FE_YIN){
                LoggerUtil.logError("系统记录-定时器查询打印机-此订单是飞印打印不能处理-信息-"+printDataDTO.toString());
                continue;
            }
        }
    }


    /**
     * 每隔2分钟全部放入重新打印队列一次
     */
    @Scheduled(cron = "0 */2 * * * ?")
    public void doFailedPrintToWaitQueue() {
        Long listSize = stringRedisTemplate.boundListOps(RedisConstants.Shop_Failed_Print_OId_List).size();
        if (listSize < NumConstants.INT_NUM_1){
            return;
        }
        String[] allList = new String[listSize.intValue()];
        for (int i = 0; i < listSize; i++) {
            String pop = stringRedisTemplate.boundListOps(RedisConstants.Shop_Failed_Print_OId_List).rightPop();
            allList[i] = pop;
        }
        if (allList == null || allList.length < NumConstants.INT_NUM_1){
            return;
        }
        stringRedisTemplate.boundListOps(RedisConstants.Shop_Wait_Print_OId_List).leftPushAll(allList);
        System.out.println("每隔2分钟全部放入重新打印队列一次");
    }


    /**
     * 每隔1分钟全部放入等待打印队列
     */
    @Scheduled(cron = "0/30 * * * * ?")
    public void doFirstQueuePrintToWaitQueue() {
        Long listSize = stringRedisTemplate.boundListOps(RedisConstants.Shop_First_Print_OId_List).size();
        if (listSize < NumConstants.INT_NUM_1){
            return;
        }
        String[] allList = new String[listSize.intValue()];
        for (int i = 0; i < listSize; i++) {
            String pop = stringRedisTemplate.boundListOps(RedisConstants.Shop_Failed_Print_OId_List).rightPop();
            allList[i] = pop;
        }
        if (allList == null || allList.length < NumConstants.INT_NUM_1){
            return;
        }
        stringRedisTemplate.boundListOps(RedisConstants.Shop_Wait_Print_OId_List).leftPushAll(allList);
        System.out.println("每隔1分钟全部放入等待打印队列");
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
}

