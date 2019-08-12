package ops.school.scheduled;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import ops.school.api.constants.NumConstants;
import ops.school.api.constants.ShopPrintConfigConstants;
import ops.school.api.dto.print.PrintDataDTO;
import ops.school.controller.SignController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
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

    /**
     * 每隔30秒查询打印一次
     */
    @Scheduled(cron = "1 0 0 * * ?")
    public void doPrintAndAcceptOrder() {
        List<String> findOrders = stringRedisTemplate.boundListOps("Shop_Wait_Print_OId_List").range(0,-1);
        if (findOrders.size() < NumConstants.INT_NUM_1){
            return;
        }
        String all = JSONArray.toJSONString(findOrders);
        List<PrintDataDTO> dataDTOS = JSONObject.parseArray(all,PrintDataDTO.class);
        //批量查询订单 todo 考虑程序执行时间数据库状态改变了，这的批量查询数据不最新，改变上面，每次查10条
        for (PrintDataDTO dataDTO : dataDTOS) {
            if (dataDTO.getPrintBrand().intValue() == ShopPrintConfigConstants.PRINT_BRAND_DB_FEI_E){
                //先查数据库是否未接手
                //查询飞鹅打印
                //如果已打印变成状态
                //推送用户
                //如果未打印，查询打印机状态，打印
                //变更用户状态
                //不能打印，推送到商家待接手
            }else if (dataDTO.getPrintBrand().intValue() == ShopPrintConfigConstants.PRINT_BRAND_DB_FE_YIN){
                //先查数据库是否未接手
                //查询飞鹅打印
                //如果已打印变成状态
                //推送用户
                //如果未打印，查询打印机状态，打印
                //变更用户状态
                //不能打印，推送到商家待接手
            }
        }

    }
}
