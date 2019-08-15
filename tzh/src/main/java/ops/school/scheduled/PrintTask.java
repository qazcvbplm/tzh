package ops.school.scheduled;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.qcloudsms.httpclient.HTTPException;
import ops.school.api.constants.NumConstants;
import ops.school.api.constants.RedisConstants;
import ops.school.api.constants.ShopPrintConfigConstants;
import ops.school.api.constants.WechatConfigConstants;
import ops.school.api.dto.print.PrintDataDTO;
import ops.school.api.dto.print.ShopPrintResultDTO;
import ops.school.api.entity.Orders;
import ops.school.api.entity.School;
import ops.school.api.service.OrderProductService;
import ops.school.api.service.OrdersService;
import ops.school.api.service.SchoolService;
import ops.school.api.util.LoggerUtil;
import ops.school.api.util.ShopPrintUtils;
import ops.school.api.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import sun.rmi.runtime.Log;

import java.io.IOException;

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

    @Autowired
    private SchoolService schoolService;


    /**
     * 每隔1分钟查询打印一次
     */
    @Scheduled(cron = "* */1 * * * ?")
    public void doPrintAndAcceptOrder() {
        while (true) {
            Long redisSize = stringRedisTemplate.boundListOps("Shop_Wait_Print_OId_List").size();
            if (redisSize < NumConstants.INT_NUM_1) {
                try {
                    //没有订单等xs
                    Thread.sleep(30 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    LoggerUtil.log(e.getMessage());
                }
                System.out.println("没有订单循环30s等待");
                break;
            }
            String findOrder = stringRedisTemplate.boundListOps("Shop_Wait_Print_OId_List").rightPop();
            if (findOrder == null) {
                return;
            }
            PrintDataDTO printDataDTO = JSONObject.parseObject(findOrder, PrintDataDTO.class);
            Orders orders = null;
            if (printDataDTO != null && printDataDTO.getRealOrder() != null) {
                orders = printDataDTO.getRealOrder();
            }else {
                //订单为空，如果不能发送，跳过,放入失败队列
                LoggerUtil.logError("doPrintAndAcceptOrder-修改订单状态后-订单缓存为空-订单和打印id"+printDataDTO.getOurOrderId()+"-"+printDataDTO.getPlatePrintOrderId());
                printDataDTO.setCycleRedisCount(printDataDTO.getCycleRedisCount() + NumConstants.INT_NUM_1);
                Long listIndex = stringRedisTemplate.boundListOps(RedisConstants.Shop_Failed_Print_OId_List).leftPush(JSON.toJSONString(printDataDTO));
                continue;
            }
            //订单待接手状态，需要处理
            if (printDataDTO.getPrintBrand().intValue() != ShopPrintConfigConstants.PRINT_BRAND_DB_FEI_E) {
                LoggerUtil.logError("系统记录-定时器查询打印机-此订单是飞印打印不能处理-信息-" + printDataDTO.toString());
                continue;
            }
            //查询飞鹅打印
            ShopPrintResultDTO<Boolean> printResult = ShopPrintUtils.feiEGetPrintStatusYes(printDataDTO.getPlatePrintOrderId());
            //如果已打印不管
            if (printResult != null && printResult.isSuccess()) {
                continue;

            }
            //没有打印给负责人发消息
            //1-先查学校
            School school = schoolService.findById(orders.getSchoolId());
            if (school.getPhone() == null || orders.getShopName() == null || orders.getShopPhone() == null) {
                LoggerUtil.logError("doPrintAndAcceptOrder-修改订单状态后发送负责人短信失败-订单" + orders.getId() + "负责人电话" + school.getPhone() + "店铺名称" + orders.getShopName() + "店铺电话" + orders.getShopPhone());
                //如果不能发送，跳过,放入失败队列
                printDataDTO.setCycleRedisCount(printDataDTO.getCycleRedisCount() + NumConstants.INT_NUM_1);
                Long listIndex = stringRedisTemplate.boundListOps(RedisConstants.Shop_Failed_Print_OId_List).leftPush(JSON.toJSONString(printDataDTO));
                continue;
            }
            String[] params = new String[2];
            params[0] = orders.getShopName();
            params[1] = orders.getShopPhone();
            try {
                Util.qqSmsNoConfig(school.getPhone(), WechatConfigConstants.Tencent_Message_NOT_Print_Template, params);
            } catch (Exception ex) {
                ex.printStackTrace();
                LoggerUtil.logError("doPrintAndAcceptOrder-修改订单状态后发送负责人短信失败-" + orders.getId());
            }

        }//true
    }


    /**
     * @param printDataDTO
     * @date: 2019/8/13 11:56
     * @author: QinDaoFang
     * @version:version
     * @return: void
     * @Desc: desc 把订单状态改成已接手
     */
    private Boolean changeOrderStatusToJS(PrintDataDTO printDataDTO) {
        //不做空判断，前面一定判断
        //先改数据库
        Orders update = new Orders();
        update.setWaterNumber(printDataDTO.getWaterNumber());
        update.setId(printDataDTO.getOurOrderId());
        Integer updateNum = ordersService.shopAcceptOrderById(update);
        if (updateNum != NumConstants.INT_NUM_1) {
            //如果失败
            return false;
        }
        //再改redis
        Long delNum = stringRedisTemplate.boundHashOps("SHOP_DJS" + printDataDTO.getOurShopId()).delete(printDataDTO.getOurOrderId());
        if (delNum.intValue() < NumConstants.INT_NUM_1) {
            //如果失败 只是记录日志
            LoggerUtil.logError("定时器-把订单状态改成已接手-changeOrderStatusToJS-删除缓存失败-" + "SHOP_DJS" + printDataDTO.getOurShopId() + printDataDTO.getOurOrderId());
        }
        return true;
    }


}

