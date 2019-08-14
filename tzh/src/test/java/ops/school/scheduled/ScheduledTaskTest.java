package ops.school.scheduled;


import ops.school.App;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
@WebAppConfiguration
public class ScheduledTaskTest{

    @Resource(name = "scheduledTask")
    private Task task;

    @Autowired
    private PrintTask printTask;


    @Test
    public void task() {
        task.task();
    }

    @Test
    public void cancelRunOrdersTask() {
        task.cancelRunOrdersTask();
    }

    @Test
    public void couponsTest() {
        task.couponInvalid();
    }

    @Test
    public void wxUserCouponInvalid() {
        task.wxUserCouponInvalid();
    }

//    @Test
//    public void printTask(){
//        printTask.doFailedPrintToWaitQueue();
//    }


}
