package ops.school.scheduled;


import com.alibaba.fastjson.JSON;
import com.github.qcloudsms.httpclient.HTTPException;
import ops.school.api.dao.*;
import ops.school.api.dto.RunOrdersTj;
import ops.school.api.entity.*;
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
    private SchoolMapper schoolMapper;
    @Autowired
    private ApplicationMapper applicationMapper;
    @Autowired
    private ShopMapper shopMapper;
    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private RunOrdersMapper runOrdersMapper;
    @Autowired
    private DayLogTakeoutMapper dayLogTakeoutMapper;
    @Autowired
    private RedisUtil cache;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //0 0 10,14,16 * * ?   每天上午10点，下午2点，4点
    @Scheduled(cron = "0 0 2 * * ?")
    public void task() {
        ordersMapper.remove();
        runOrdersMapper.remove();
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
        List<School> schools = schoolMapper.find(new School());
        for (School schooltemp : schools) {
            List<Shop> shops = shopMapper.find(new Shop(schooltemp.getId()));
            for (Shop shoptemp : shops) {
                map.put("shopId", shoptemp.getId());
                Orders result = ordersMapper.completeByShopId(map);
                DayLogTakeout daylog = new DayLogTakeout()
                        .shoplog(shoptemp.getShopName(), shoptemp.getId(), day, result, "商铺日志", schooltemp.getId());
                dayLogTakeoutMapper.insert(daylog);
            }
            map.put("schoolId", schooltemp.getId());
            List<Orders> result = ordersMapper.completeBySchoolId(map);
            DayLogTakeout daylog = new DayLogTakeout()
                    .schoollog(schooltemp.getName(), schooltemp.getId(), day, result, "学校商铺日志", schooltemp.getAppId());
            dayLogTakeoutMapper.insert(daylog);
        }
        //////////////////////////////////////////////////跑腿日志///////////////////////////////////////////////////////////
        for (School schooltemp : schools) {
            List<RunOrdersTj> runOrdersTjs = runOrdersMapper.tj(schooltemp.getId(), day + "%");
            DayLogTakeout runLog = new DayLogTakeout(day, schooltemp, runOrdersTjs);
            dayLogTakeoutMapper.insert(runLog);
        }
        LoggerUtil.log("统计耗时:" + (System.currentTimeMillis() - start));
    }

    @Scheduled(cron = "1 0 0 * * ?")
    public void initday() {
        SignController.day = Integer.valueOf(new SimpleDateFormat("yyyyMMdd").format(new Date()));
    }
    //0 0 10,14,16 * * ?   每天上午10点，下午2点，4点
	/*	@Scheduled(fixedDelay=180000)
		public void clear(){
			Iterator iter = WxUserController.code.entrySet().iterator();
			while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String key = entry.getKey().toString();
			if(WxUserController.code.get(key)>System.currentTimeMillis()){
				WxUserController.code.remove(key);
				WxUserController.code2.remove(key);
			}
			}
		}*/

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
            ordersList=ordersMapper.findAllDjs();
        }
        System.out.println("待接手订单条数："+ordersList.size());
        List<School> schoolList = schoolMapper.find(new School());
        if (!schoolList.isEmpty()){
            for (School sch: schoolList) {
                int count = 0;
                if (!ordersList.isEmpty()) {
                    for (int i = 0;i < ordersList.size();i++) {
                        if (sch.getId().equals(ordersList.get(i).getSchoolId())){
                            long current = System.currentTimeMillis();
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String curTime = df.format(current);
                            System.out.println("订单支付时间：" + ordersList.get(0).getPayTime());
                            // 获取订单支付时间
                            long differTime = TimeUtil.dateDiff(ordersList.get(0).getPayTime(), curTime);
                            System.out.println(differTime);
                            if (differTime > 10) {
                                count++;
                                if (count != 0 && i == (ordersList.size()-1)){
                                    String params = count +"";
                                    try {
                                        Util.qqsms(1400169549, "0eb188f83ef4b2dc8976b5e76c70581e", sch.getPhone(), 372252, params, null);
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
            orders=ordersMapper.find(orders1);
        }
        System.out.println("商家已接手："+orders.size());
        List<School> schoolList = schoolMapper.find(new School());
        if (!schoolList.isEmpty()){
            for (School sch: schoolList) {
                int count = 0;
                if (!orders.isEmpty()) {
                    for (int i = 0;i < orders.size();i++) {
                        if (sch.getId().equals(orders.get(i).getSchoolId())){
                            long current = System.currentTimeMillis();
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String curTime = df.format(current);
                            System.out.println("商家接手时间：" + orders.get(0).getShopReceiptTime());
                            // 获取订单支付时间
                            long differTime = TimeUtil.dateDiff(orders.get(0).getShopReceiptTime(), curTime);
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
