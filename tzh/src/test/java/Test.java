import com.alibaba.fastjson.JSON;
import com.github.qcloudsms.httpclient.HTTPException;
import ops.school.App;
import ops.school.api.dto.project.ProductOrderDTO;
import ops.school.api.entity.Orders;
import ops.school.api.service.TxLogService;
import ops.school.api.util.ResponseObject;
import ops.school.api.util.Util;
import ops.school.service.TOrdersService;
import ops.school.util.WxPhoneUtil;
import org.json.JSONException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
@WebAppConfiguration
public class Test {

    @Autowired
    private TxLogService txLogService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private TOrdersService tOrdersService;

    /*@org.junit.Test
    public void test(){
//        try {
//             Util.qqsms(1400169549, "0eb188f83ef4b2dc8976b5e76c70581e", "18857818257", 372755, "123123,ghuighiu", null);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        } catch (HTTPException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }*/
//
//    @org.junit.Test
//    public void addOrderTest(){
//        List<ProductOrderDTO> productOrderDTOS = new ArrayList<>();
//        ProductOrderDTO productOrderDTO = new ProductOrderDTO();
//        ProductOrderDTO productOrderDTO1 = new ProductOrderDTO();
//        productOrderDTO.setProductId(6);
//        productOrderDTO.setAttributeId(8);
//        productOrderDTO.setCount(2);
//        productOrderDTOS.add(productOrderDTO);
//        productOrderDTO1.setCount(1);
//        productOrderDTO1.setProductId(7);
//        productOrderDTO1.setAttributeId(16);
//        productOrderDTOS.add(productOrderDTO1);
//        Orders orders = new Orders();
//        orders.setShopId(10);
//        orders.setOpenId("o_JLm5eY7Pokd1i4hAQ70CxiYgNQ");
//        orders.setTyp("外卖订单");
//        orders.setFloorId(1);
//        orders.setRemark("多放辣椒！");
//        Integer cId = 2;
//        orders.setCouponId(Long.parseLong(cId.toString()));
//        orders.setSendPrice(new BigDecimal(1));
//        orders.setBoxPrice(new BigDecimal(3));
//        orders.setPayPrice(new BigDecimal(33));
//        orders.setReseverTime("2019-7-23 12:40:12");
//        ResponseObject re =  tOrdersService.addOrder2(productOrderDTOS,orders);
//        System.out.println(re.toString());
//    }

    @org.junit.Test
    public void wxPhoneTest(){
        String encrypData = "fHloH7K49+cmOUigiEpeb5umMRV/0w/FQF7ohstRChO2Z79lv4pjubUTv24Ldukx3sF4Fz7zLWFjsjBcm8eGp9gOOvuFN7VqYrlZmhkamZCU09KQ+qSe3X7kWIyhKTVMMbxWVwPIdC0GZGtQlhOhrf0oQmbUtqByw1QRtLST5UBLPGxKtnISyZZ/Dh7/66uOHX1Sko7TZTFwv3fdUPhYKA==";
        String ivData =  "krBvaIeGo2xZ/hZxTvd5kw==";
        String sessionKey =  "uj1qPKSsok54+RRe1q6k7A==";
        try {
            String rs = WxPhoneUtil.getPhoneNumberBeanS5(encrypData,sessionKey,ivData);
            System.out.println(rs);
        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
