package ops.school.scheduled;


import ops.school.App;
import ops.school.api.dto.TimeEveryDTO;
import ops.school.api.util.TimeUtilS;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


//@ContextConfiguration(locations = {"classpath:application.yml", "classpath:application-dev.yml","classpath:application-prod.yml","","classpath:application-local.yml","classpath:generator.xml"})
@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
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
        long start = System.currentTimeMillis();
        List<TimeEveryDTO> dtoList = new ArrayList<>();
        dtoList.add(new TimeEveryDTO(8,30));
        dtoList.add(new TimeEveryDTO(8,31));
        dtoList.add(new TimeEveryDTO(9,1));
        dtoList.add(new TimeEveryDTO(9,2));
        dtoList.add(new TimeEveryDTO(9,3));
        dtoList.add(new TimeEveryDTO(9,4));
        dtoList.add(new TimeEveryDTO(9,5));
        dtoList.add(new TimeEveryDTO(9,6));
        for (TimeEveryDTO timeEveryDTO : dtoList) {
            Date date = TimeUtilS.getDateByEvery(timeEveryDTO.getYear(),timeEveryDTO.getMonth()-1,timeEveryDTO.getDay(),timeEveryDTO.getHour(),timeEveryDTO.getMinutes(),timeEveryDTO.getSeconds());

        }
        TimeEveryDTO timeEveryDTO = new TimeEveryDTO(9,1);
        Date date = TimeUtilS.getDateByEvery(timeEveryDTO.getYear(),timeEveryDTO.getMonth()-1,timeEveryDTO.getDay(),timeEveryDTO.getHour(),timeEveryDTO.getMinutes(),timeEveryDTO.getSeconds());
        task.jisuan();
        long stop = System.currentTimeMillis();
        System.out.println(stop- start);
        System.out.println((stop- start)/1000/60);
    }

    @Test
    public void testDoPrintAndAcceptOrder(){
        printTask.doPrintAndAcceptOrder();
    }

    public static void main(String[] args) {
        BigDecimal a = new BigDecimal(0);
        BigDecimal b= new BigDecimal(10000);
        BigDecimal c =  new BigDecimal(0);
        System.out.println(a.subtract(b));
        System.out.println(a.subtract(b).add(c));
    }


}
