package ops.school.scheduled;


import ops.school.App;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

    @Test
    public void taskCom(){
        List<String> stringList = new ArrayList<>();
        stringList.add("2019-09");
        task.jisuan();
    }

    public static void main(String[] args) {
        BigDecimal a = new BigDecimal(0);
        BigDecimal b= new BigDecimal(10000);
        BigDecimal c =  new BigDecimal(0);
        System.out.println(a.subtract(b));
        System.out.println(a.subtract(b).add(c));
    }


}
