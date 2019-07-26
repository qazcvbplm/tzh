package ops.school.init;

import com.alibaba.fastjson.JSON;
import ops.school.api.entity.Orders;
import ops.school.api.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderInit implements CommandLineRunner {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private OrdersService ordersService;
    @Override
    public void run(String... args) throws Exception {
        System.out.println("v1.0");
        List<Orders> orders= ordersService.findAllDjs();
        for(Orders temp:orders){
            String key = "SHOP_DJS"+temp.getShopId();
            String value = JSON.toJSONString(temp);
            stringRedisTemplate.boundHashOps(key).put(temp.getId(),value);
        }
    }
}
