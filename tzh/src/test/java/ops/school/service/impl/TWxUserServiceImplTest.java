package ops.school.service.impl;

import ops.school.App;
import ops.school.service.TWxUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * CreatebyFang
 * fangfor@outlook.com
 * 2019/8/5
 * 11:32
 * #
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class TWxUserServiceImplTest {

    @Autowired
    private TWxUserService tWxUserService;

    @Test
    public void chargeSuccess() {
        String attach = "30";
        String orderId = "5201314";
        String openId = "oWP5G42cNW2gghAZiqvfOFDQ--0A";
        tWxUserService.chargeSuccess(orderId,openId,attach);
    }
}
