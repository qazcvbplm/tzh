package ops.school.service.impl;



import ops.school.App;
import ops.school.service.TCommonService;
import ops.school.service.TOrdersService;
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

    @Autowired
    private TCommonService tCommonService;

    @Autowired
    private TOrdersService tOrdersService;

    @Test
    public void chargeSuccess() {
        String attach = "30";
        String orderId = "5201314";
        String openId = "oWP5G4wITSFF8Ts7kJmjNy7E0xzQ";
        tWxUserService.chargeSuccess(orderId,openId,attach);
    }

    @Test
    public void TCommonServiceImplTest(){
        Integer txid = 4566;
        Integer status = 1;
        tCommonService.txAudit(txid,status);
    }

    @Test
    public void cancelOrder(){
        String orderId = "201908052041354162258080205";
        tOrdersService.cancel(orderId);
    }

    @Test
    public void testOrderComplete(){
        String orderId = "201908061718417597677171055";
        tOrdersService.orderSettlement(orderId);
    }



}
