import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import ops.school.App;
import ops.school.api.dto.project.ProductOrderDTO;
import ops.school.api.entity.Orders;
import ops.school.api.entity.Shop;
import ops.school.api.service.ShopService;
import ops.school.api.service.TxLogService;
import ops.school.api.util.ResponseObject;
import ops.school.service.TOrdersService;
import ops.school.util.Base64Util;
import ops.school.util.ImageUtil;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


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
    @Autowired
    private ShopService shopService;

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

    @org.junit.Test
    public void addOrderTest(){
        List<ProductOrderDTO> productOrderDTOS = new ArrayList<>();
        ProductOrderDTO productOrderDTO = new ProductOrderDTO();
        ProductOrderDTO productOrderDTO1 = new ProductOrderDTO();
        productOrderDTO.setProductId(6);
        productOrderDTO.setAttributeId(8);
        productOrderDTO.setCount(2);
        productOrderDTOS.add(productOrderDTO);
        productOrderDTO1.setCount(1);
        productOrderDTO1.setProductId(7);
        productOrderDTO1.setAttributeId(16);
        productOrderDTOS.add(productOrderDTO1);
        Orders orders = new Orders();
        orders.setShopId(10);
        orders.setOpenId("o_JLm5eY7Pokd1i4hAQ70CxiYgNQ");
        orders.setTyp("外卖订单");
        orders.setFloorId(1);
        orders.setRemark("多放辣椒！");
        Integer cId = 2;
        orders.setCouponId(Long.parseLong(cId.toString()));
        orders.setSendPrice(new BigDecimal(1));
        orders.setBoxPrice(new BigDecimal(3));
        orders.setPayPrice(new BigDecimal(33));
        orders.setReseverTime("2019-7-23 12:40:12");
        ResponseObject re =  tOrdersService.addOrder2(productOrderDTOS,orders);
        System.out.println(re.toString());
    }

    @org.junit.Test
    public void wxPhoneTest(){
        tOrdersService.orderSettlement("201907271706272822542855187");
    }

    @org.junit.Test
    public void imageTest(){
        QueryWrapper<Shop> query = new QueryWrapper<>();
        query.lambda().eq(Shop::getId,10);
        Shop shop = shopService.getOne(query);
        String shopImage = "D:/image/timg3.jpg";
        String codeImg = "D:/image/9.jpg";
        String path = "D:/image/";
        Map map = ImageUtil.pictureCombine(shopImage,codeImg,path,shop);
    }
}
