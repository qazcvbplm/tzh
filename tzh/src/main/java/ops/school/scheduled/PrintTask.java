package ops.school.scheduled;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import ops.school.api.constants.NumConstants;
import ops.school.api.constants.ShopPrintConfigConstants;
import ops.school.api.dto.print.PrintDataDTO;
import ops.school.api.dto.print.ShopPrintResultDTO;
import ops.school.api.entity.Orders;
import ops.school.api.service.OrdersService;
import ops.school.api.util.LoggerUtil;
import ops.school.api.util.ShopPrintUtils;
import ops.school.util.WxMessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

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
            //订单待接手状态，需要处理
            if (printDataDTO.getPrintBrand().intValue() == ShopPrintConfigConstants.PRINT_BRAND_DB_FEI_E){
                //查询飞鹅打印
                ShopPrintResultDTO<Boolean> printResult = ShopPrintUtils.feiEGetPrintStatusYes(printDataDTO.getPlatePrintOrderId());
                if (printResult == null || !printResult.isSuccess()){
                    LoggerUtil.logError("系统记录-定时器查询飞鹅打印机-doPrintAndAcceptOrder-日志" + printResult.getErrorMessage()+printDataDTO.getOurOrderId()+"飞鹅id"+printDataDTO.getPlatePrintOrderId());
                }
                //如果已打印变成状态
                if (printResult.isSuccess()){
                    Boolean updateTrue = this.changeOrderStatusToJS(printDataDTO);
                    if (!updateTrue){
                        //如果更改订单状态失败
                        LoggerUtil.logError("doPrintAndAcceptOrder-修改订单状态失败-"+printDataDTO.getOurOrderId());
                        stringRedisTemplate.boundListOps("Shop_Wait_Print_OId_List").leftPush(JSON.toJSONString(printDataDTO));
                        continue;
                    }
                    //修改成功商家已接手 推送用户消息
                    Boolean sendTrue = WxMessageUtil.wxSendOrderMsgByOrder(orders);
                    if (!sendTrue){
                        LoggerUtil.logError("doPrintAndAcceptOrder-修改订单状态后发送模板消息失败-"+printDataDTO.getOurOrderId());
                    }
                    continue;

                }else {
                    //如果未打印，跳过
                    stringRedisTemplate.boundListOps("Shop_Wait_Print_OId_List").leftPush(JSON.toJSONString(printDataDTO));
                    continue;
                }
            }else if (printDataDTO.getPrintBrand().intValue() == ShopPrintConfigConstants.PRINT_BRAND_DB_FE_YIN){
                stringRedisTemplate.boundListOps("Shop_Wait_Print_OId_List").leftPush(JSON.toJSONString(printDataDTO));
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
            //查询状态
            Orders orders = ordersService.findById(printDataDTO.getOurOrderId());
            if (orders != null && "商家已接手".equals(orders.getStatus())){
                return true;
            }else if ("待接手".equals(orders.getStatus())){
                return false;
            }else if ("已取消".equals(orders.getStatus()) || "已完成".equals(orders.getStatus())){
                return true;
            }
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

