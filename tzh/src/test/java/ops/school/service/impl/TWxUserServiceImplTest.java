package ops.school.service.impl;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import ops.school.App;
import ops.school.api.entity.OrderProduct;
import ops.school.api.entity.Orders;
import ops.school.api.entity.School;
import ops.school.api.util.LoggerUtil;
import ops.school.api.wxutil.WxMessageUtil;
import ops.school.service.TCommonService;
import ops.school.service.TOrdersService;
import ops.school.service.TWxUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    private WxMessageUtil wxMessageUtil;

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
    public void testWxMessageUtil(){
        Orders orders = new Orders();
        School school = new School();
        orders.setSchoolId(27);
        //wxMessageUtil.wxSendMsg(orders,"",orders.getSchoolId());
    }

    @Test
    public void testPaySuccess(){
        String orderId = "201908311212084831626837216";
        String openId = "oWP5G4wITSFF8Ts7kJmjNy7E0xzQ";
        String attach = "23";
        tWxUserService.chargeSuccess(orderId, openId, attach);
    }

//    @Test
//    public void dataBack(){
//        tWxUserService.updateBackDataFromOldToNewByPhone();
//    }
//
//    @Test
//    public void dataSourceBack(){
//        tWxUserService.updateDataSourceFromOldToNewByPhone();
//    }





}
