package ops.school.treadRunnable;

import com.alibaba.fastjson.JSON;
import io.lettuce.core.protocol.AsyncCommand;
import ops.school.api.dto.print.PrintDataDTO;
import ops.school.api.util.LoggerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.CountDownLatch;

class DelaySendToRedis implements Runnable {

    private PrintDataDTO printData;

    private CountDownLatch countDownLatch;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public DelaySendToRedis(PrintDataDTO printDataDTO) {
        this.printData = printDataDTO;
    }

    @Override
    public synchronized void run() {
        try {
            //等待主线程执行完毕，获得开始执行信号...
            countDownLatch.await();
            //完成预期工作，发出完成信号...
            System.out.println("进入新的异步线程等待发送缓存");
            Thread.sleep(4000);
            System.out.println("发送缓存");
            stringRedisTemplate.boundListOps("Shop_Wait_Print_OId_List").leftPush(JSON.toJSONString(printData));
            stringRedisTemplate.boundListOps("Shop_Test_Print_OId_List").leftPush(JSON.toJSONString(printData));
        } catch (InterruptedException e) {
            e.printStackTrace();
            LoggerUtil.logError(e.getMessage());
        } finally {
            countDownLatch.countDown();
        }

    }
}