package ops.school.init;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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


    public void redisInit() {
        if (stringRedisTemplate.opsForValue().get("cache") == null) {
            stringRedisTemplate.opsForValue().set("cache", "true");
        }
    }


    @Override
    public void run(String... args) throws Exception {
        redisInit();
        System.out.println("v1.0");
        //设置待接手订单
        List<Orders> orders= ordersService.findAllDjs();
        for(Orders temp:orders){
            String key = "SHOP_DJS"+temp.getShopId();
            String value = JSON.toJSONString(temp);
            stringRedisTemplate.boundHashOps(key).put(temp.getId(),value);
        }
        //设置已经接手的订单
        QueryWrapper<Orders> query = new QueryWrapper<Orders>();
        query.lambda().eq(Orders::getStatus, "商家已接手");
        List<Orders> list = ordersService.list(query);
        for (Orders temp : list) {
            stringRedisTemplate.boundHashOps("SHOP_YJS").put(temp.getId(), JSON.toJSONString(temp));
        }
    }
}
