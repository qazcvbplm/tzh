package ops.school.scheduled;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.qcloudsms.httpclient.HTTPException;
import com.sun.xml.internal.bind.v2.TODO;
import ops.school.api.dao.*;
import ops.school.api.dto.RunOrdersTj;
import ops.school.api.entity.*;
import ops.school.api.service.*;
import ops.school.api.util.*;
import ops.school.controller.SignController;
import ops.school.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class Task {

    @Autowired
    private SchoolService schoolService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private RunOrdersService runOrdersService;
    @Autowired
    private DayLogTakeoutService dayLogTakeoutService;
    @Autowired
    private RedisUtil cache;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //0 0 10,14,16 * * ?   每天上午10点，下午2点，4点
    @Scheduled(cron = "0 0 2 * * ?")
    public void task() {
        ordersService.remove(new QueryWrapper<Orders>().lambda().eq(Orders::getStatus,"待付款"));
        runOrdersService.remove(new QueryWrapper<RunOrders>().lambda().eq(RunOrders::getStatus,"待付款"));
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void clearCache() {
        cache.clear();
    }

    public String getYesterdayByCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        Date time = calendar.getTime();
        String yesterday = new SimpleDateFormat("yyyy-MM-dd").format(time);
        return yesterday;
    }


    //0 0 10,14,16 * * ?   每天上午10点，下午2点，4点
    @Scheduled(cron = "0 0 2 * * ?")
    public void jisuan() {
        long start = System.currentTimeMillis();
        String day = getYesterdayByCalendar();
        Map<String, Object> map = new HashMap<>();
        map.put("day", day + "%");
        List<School> schools = schoolService.list(new QueryWrapper<School>().lambda().eq(School::getIsDelete,0));
        for (School schooltemp : schools) {
            List<Shop> shops = shopService.list(new QueryWrapper<Shop>().lambda().eq(Shop::getSchoolId,schooltemp.getId()));
            for (Shop shoptemp : shops) {
                map.put("shopId", shoptemp.getId());
                Orders result = ordersService.completeByShopId(map);
                DayLogTakeout daylog = new DayLogTakeout()
                        .shoplog(shoptemp.getShopName(), shoptemp.getId(), day, result, "商铺日志", schooltemp.getId());
                dayLogTakeoutService.save(daylog);
            }
            map.put("schoolId", schooltemp.getId());
            List<Orders> result = ordersService.completeBySchoolId(map);
            DayLogTakeout daylog = new DayLogTakeout()
                    .schoollog(schooltemp.getName(), schooltemp.getId(), day, result, "学校商铺日志", schooltemp.getAppId());
            dayLogTakeoutService.save(daylog);
        }
        //////////////////////////////////////////////////跑腿日志///////////////////////////////////////////////////////////
        for (School schooltemp : schools) {
            List<RunOrdersTj> runOrdersTjs = runOrdersService.tj(schooltemp.getId(), day + "%");
            DayLogTakeout runLog = new DayLogTakeout(day, schooltemp, runOrdersTjs);
            dayLogTakeoutService.save(runLog);
        }
        LoggerUtil.log("统计耗时:" + (System.currentTimeMillis() - start));
    }

    @Scheduled(cron = "1 0 0 * * ?")
    public void initday() {
        SignController.day = Integer.valueOf(new SimpleDateFormat("yyyyMMdd").format(new Date()));
    }


    /**
     * 每5分钟执行一次
     * 提醒学校负责人，商家有超时未接手订单
     */
    @Scheduled(cron = "0 */5 * * * ?")
    public void remindUntakenOrders() {

        // 查询所有待接手订单
        List<Orders> ordersList;
        if (SpringUtil.redisCache()){
            ordersList = JSON.parseArray(stringRedisTemplate.boundHashOps("ALL_DJS").values().toString(), Orders.class);
        }else{
            ordersList=ordersService.findAllDjs();
        }
        //TODO
        //服务重启的话缓存会漏单
        for(Orders temp : ordersList){
            Shop shop = shopService.getById(temp.getShopId());
            if(stringRedisTemplate.opsForValue().get("SCHOOL_NOTIFY_SHOP"+shop.getShopPhone())!=null){
                try {
                    Util.qqsms(1400169549, "0eb188f83ef4b2dc8976b5e76c70581e", shop.getShopPhone(), 372755, shop.getShopName()+","+shop.getShopPhone(), null);
                } catch (Exception e) {
                    LoggerUtil.log(e.getMessage());
                }
                stringRedisTemplate.opsForValue().set("SCHOOL_NOTIFY_SHOP"+shop.getShopPhone(),"",10,TimeUnit.MINUTES);
            }
        }
       /* List<School> schoolList = schoolService.find(new School());
            for (School sch: schoolList) {
                int count = 0;
                if (ordersList.size()>0) {
                    for (int i = 0;i < ordersList.size();i++) {
                        if (sch.getId().equals(ordersList.get(i).getSchoolId())){
                            long current = System.currentTimeMillis();
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String curTime = df.format(current);
                            // 获取订单支付时间
                            long differTime = TimeUtil.dateDiff(ordersList.get(0).getPayTime(), curTime);
                            if (differTime > 10) {
                                count++;
                                if (count != 0 && i == (ordersList.size()-1)){
                                    String params = count +"";
                                    try {
                                        Util.qqsms(1400169549, "0eb188f83ef4b2dc8976b5e76c70581e", sch.getPhone(), 372252, params, null);
                                    } catch (HTTPException | IOException | org.json.JSONException e) {
                                        LoggerUtil.log(e.getMessage());
                                    }
                                }
                            }
                        }
                    }
                }
            }*/
    }

    /**
     * 每5分钟执行一次
     * 提醒学校负责人，配送员有超时未接手订单
     */
    @Scheduled(cron = "0 */5 * * * ?")
    public void remindDistributor() {

        // 查询所有商家已接手订单
        List<Orders> orders;
        if (SpringUtil.redisCache()){
            orders = JSON.parseArray(stringRedisTemplate.boundHashOps("SHOP_YJS").values().toString(), Orders.class);
        }else{
            Orders orders1 = new Orders();
            orders1.setStatus("商家已接手");
            orders=ordersService.find(orders1);
        }
        System.out.println("商家已接手："+orders.size());
        List<School> schoolList = schoolService.find(new School());
        if (!schoolList.isEmpty()){
            for (School sch: schoolList) {
                int count = 0;
                if (!orders.isEmpty()) {
                    for (int i = 0;i < orders.size();i++) {
                        if (sch.getId().equals(orders.get(i).getSchoolId())){
                            long current = System.currentTimeMillis();
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String curTime = df.format(current);
                            System.out.println("商家接手时间：" + orders.get(0).getShopAcceptTime());
                            // 获取订单支付时间
                            long differTime = TimeUtil.dateDiff(orders.get(0).getShopAcceptTime(), curTime);
                            System.out.println(differTime);
                            if (differTime > 10) {
                                count++;
                                if (count != 0 && i == (orders.size()-1)){
                                    String params = count +"";
                                    try {
                                        Util.qqsms(1400169549, "0eb188f83ef4b2dc8976b5e76c70581e", sch.getPhone(), 372253, params, null);
                                        stringRedisTemplate.opsForValue().set(sch.getPhone(), params, 5, TimeUnit.MINUTES);
                                    } catch (HTTPException | IOException | org.json.JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
