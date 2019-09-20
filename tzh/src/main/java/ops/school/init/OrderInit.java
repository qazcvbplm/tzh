package ops.school.init;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import ops.school.api.entity.Orders;
import ops.school.api.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Properties;

@Component
public class OrderInit implements CommandLineRunner {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private OrdersService ordersService;

    @Value("${server.port}")
    private String port;

    @Value("${spring.profiles.active}")
    private String active;


    public void redisInit() {
        if (stringRedisTemplate.opsForValue().get("cache") == null) {
            stringRedisTemplate.opsForValue().set("cache", "true");
        }
    }



    @Override
    public void run(String... args) throws Exception {
        redisInit();
        System.out.println("v1.0");
        Properties props = System.getProperties();
        String osName = props.getProperty("os.name");
        if (!"prod".equals(active) || "win".equalsIgnoreCase(osName.substring(0,3)) ){
            System.out.println("v1.0-初始化完成-启动完成-查询商家接手订单完成"+ "-环境" + active + "-port" + port + "-系统Os-"+osName);
            System.out.println("启动未查询待接手");
            return;
        }
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
        System.out.println("v1.0-初始化完成-启动完成-查询商家接手订单完成"+ "-环境" + active + "-port" + port + "-系统Os-"+osName);
    }
}
