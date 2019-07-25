package ops.school.scheduled;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.qcloudsms.httpclient.HTTPException;
import ops.school.api.dto.RunOrdersTj;
import ops.school.api.entity.*;
import ops.school.api.service.*;
import ops.school.api.util.*;
import ops.school.controller.SignController;
import ops.school.service.TCouponService;
import ops.school.service.TWxUserCouponService;
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
    @Autowired
    private TCouponService tCouponService;
    @Autowired
    private CouponService couponService;
    @Autowired
    private TWxUserCouponService tWxUserCouponService;

    //0 0 10,14,16 * * ?   每天上午10点，下午2点，4点
    @Scheduled(cron = "0 0 0 * * ?")
    public void task() {
        //订单流水号重置
        stringRedisTemplate.delete("SHOP_WATER_NUMBER");
        ordersService.remove(new QueryWrapper<Orders>().lambda().eq(Orders::getStatus, "待付款"));
        runOrdersService.remove(new QueryWrapper<RunOrders>().lambda().eq(RunOrders::getStatus, "待付款"));
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
        List<School> schools = schoolService.list(new QueryWrapper<School>().lambda().eq(School::getIsDelete, 0));
        for (School schooltemp : schools) {
            List<Shop> shops = shopService.list(new QueryWrapper<Shop>().lambda().eq(Shop::getSchoolId, schooltemp.getId()));
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
     * 每1分钟执行一次,隔5分钟提醒一次
     * 提醒学校负责人，商家有超时未接手订单
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void remindUntakenOrders() {

        // 查询所有待接手订单
        List<Orders> ordersList;
        if (SpringUtil.redisCache()) {
            ordersList = JSON.parseArray(stringRedisTemplate.boundHashOps("ALL_DJS").values().toString(), Orders.class);
        } else {
            ordersList = ordersService.findAllDjs();
        }
        //TODO
        //服务重启的话缓存会漏单
        for (Orders temp : ordersList) {
            Shop shop = shopService.getById(temp.getShopId());
            School school = schoolService.findById(temp.getSchoolId());
            // 获取当前时间
            long current = System.currentTimeMillis();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String curTime = df.format(current);
            // 获取订单支付时间
            long differTime = TimeUtilS.dateDiff(temp.getPayTime(), curTime);
            if (differTime > 10) {
                if (stringRedisTemplate.opsForValue().get("SCHOOL_NOTIFY_SHOP" + school.getPhone()) == null) {
                    try {
                        Util.qqsms(1400169549, "0eb188f83ef4b2dc8976b5e76c70581e", school.getPhone(), 372755, shop.getShopName() + "," + school.getPhone(), null);
                    } catch (Exception e) {
                        LoggerUtil.log(e.getMessage());
                    }
                    stringRedisTemplate.opsForValue().set("SCHOOL_NOTIFY_SHOP" + school.getPhone(), "", 5, TimeUnit.MINUTES);
                }
            }
        }
    }

    /**
     * 每1分钟执行一次，隔5分钟提醒一次
     * 提醒学校负责人，配送员有超时未接手订单
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void remindDistributor() {

        // 查询所有商家已接手订单
        List<Orders> orders;
        if (SpringUtil.redisCache()) {
            orders = JSON.parseArray(stringRedisTemplate.boundHashOps("SHOP_YJS").values().toString(), Orders.class);
        } else {
            orders = ordersService.list(new QueryWrapper<Orders>().lambda().eq(Orders::getStatus, "商家已接手"));
        }
        if (!orders.isEmpty()) {
            for (Orders order : orders) {
                School school = schoolService.findById(order.getSchoolId());
                long currentTime = System.currentTimeMillis();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String curTime = df.format(currentTime);
                // 获取商家接手时间
                long differTime = TimeUtilS.dateDiff(order.getShopAcceptTime(), curTime);
                if (differTime > 10) {
                    if (stringRedisTemplate.opsForValue().get("SCHOOL_NOTIFY_SENDER" + school.getPhone()) == null) {
                        try {
                            Util.qqsms(1400169549, "0eb188f83ef4b2dc8976b5e76c70581e", school.getPhone(), 372793, "", null);
                        } catch (HTTPException | IOException | org.json.JSONException e) {
                            e.printStackTrace();
                        }
                        stringRedisTemplate.opsForValue().set("SCHOOL_NOTIFY_SENDER" + school.getPhone(), "", 5, TimeUnit.MINUTES);
                    }
                }
            }
        }
    }

    /**
     * 判断学校优惠券是否失效
     * 每天上午10点,下午14点,18点,晚上22点执行一次
     */
    @Scheduled(cron = "0 0 10,14,18,22 * * ?")
    public void couponInvalid(){

        List<Coupon> couponList = tCouponService.findInvalidCoupon();
        if (couponList.size() != 0){
            for (Coupon coupon:couponList) {
                coupon.setIsInvalid(1);
                couponService.updateById(coupon);
            }
        }
    }

    /**
     * 判断用户优惠券是否失效
     * 每天上午8点,下午10点,14点,16点，晚上22点执行一次
     */
    @Scheduled(cron = "0 0 8,10,14,16,22 * * ?")
    public void wxUserCouponInvalid(){

        List<WxUserCoupon> wxUserCoupons = tWxUserCouponService.findInvalidUserCoupon();
        if (wxUserCoupons.size() != 0){
            for (WxUserCoupon userCoupon:wxUserCoupons) {
                userCoupon.setIsInvalid(2);
                tWxUserCouponService.updateIsInvalid(userCoupon);
            }
        }
    }
}
