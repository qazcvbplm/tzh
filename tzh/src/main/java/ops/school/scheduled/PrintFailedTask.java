package ops.school.scheduled;

import ops.school.api.constants.NumConstants;
import ops.school.api.constants.RedisConstants;
import ops.school.api.service.OrdersService;
import ops.school.api.util.LoggerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * CreatebyFang
 * fangfor@outlook.com
 * 2019/8/14
 * 16:35
 * #
 */
@Component
public class PrintFailedTask {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Autowired
    private OrdersService ordersService;

    /**
     * 每隔2分钟全部放入重新打印队列一次
     */
//    @Scheduled(cron = "0/50 * * ? * *")
//    public void doFailedPrintToWaitQueue() {
//        while (true) {
//            Long listSize = stringRedisTemplate.boundListOps(RedisConstants.Shop_Failed_Print_OId_List).size();
//            if (listSize < NumConstants.INT_NUM_1){
//                try {
//                    //没有订单等xs
//                    Thread.sleep(10*1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                    LoggerUtil.log(e.getMessage());
//                }
//                System.out.println("没有订单重新打印循环10s等待");
//                continue;
//            }
//            String[] allList = new String[listSize.intValue()];
//            for (int i = 0; i < listSize; i++) {
//                String pop = stringRedisTemplate.boundListOps(RedisConstants.Shop_Failed_Print_OId_List).rightPop();
//                allList[i] = pop;
//            }
//            if (allList == null || allList.length < NumConstants.INT_NUM_1){
//                return;
//            }
//            stringRedisTemplate.boundListOps(RedisConstants.Shop_Wait_Print_OId_List).leftPushAll(allList);
//            System.out.println("每隔1分钟全部放入重新打印队列一次");
//        }
//    }
}
