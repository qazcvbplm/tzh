package ops.school.scheduled;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import ops.school.api.constants.RedisConstants;
import ops.school.api.constants.StatisticsConstants;
import ops.school.api.dao.OrdersCompleteMapper;
import ops.school.api.dto.RunOrdersTj;
import ops.school.api.dto.TimeEveryDTO;
import ops.school.api.entity.*;
import ops.school.api.service.*;
import ops.school.api.util.*;
import ops.school.api.constants.NumConstants;
import ops.school.controller.SignController;
import ops.school.service.TCouponService;
import ops.school.service.TOrdersService;
import ops.school.service.TWxUserCouponService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
@Service(value = "scheduledTask")
public class Task {

    private Logger logger = LoggerFactory.getLogger(Task.class);

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
    private TWxUserCouponService tWxUserCouponService;

    @Autowired
    private TOrdersService tOrdersService;

    @Autowired
    private TxLogService txLogService;
    //10分钟
    private final long timeOut = 10;

    /**
     * 每天晚上把待付款的改成取消的
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void task() {
        //订单流水号重置
        stringRedisTemplate.delete("SHOP_WATER_NUMBER");
        cache.clear();
        QueryWrapper<Orders> wrapper = new QueryWrapper<>();
        wrapper.eq("status","待付款");
        List<Orders> ordersList = ordersService.list(wrapper);
        if (ordersList.size() < 1){
            return;
        }
        List<String> cancleFaidIds = new ArrayList<>();
        List<Long> ordersIds = PublicUtilS.getValueList(ordersList,"id");
        for (Orders orders : ordersList) {
            if ( !"待付款".equals(orders.getStatus())){
                continue;
            }
            if (StringUtils.isBlank(orders.getId())){
                LoggerUtil.log("定时取消订单，订单号是空，订单信息："+orders.toString());
            }
            Integer cancleNum = tOrdersService.cancel(orders.getId());
            if (cancleNum < NumConstants.INT_NUM_1){
                cancleFaidIds.add(orders.getId());
            }
        }
        if (cancleFaidIds.size() > NumConstants.INT_NUM_0){
            //如果有失败的计日志
            LoggerUtil.log("定时取消订单，订单号是："+ordersIds.toString()+"失败的订单号："+cancleFaidIds.toString());
        }
    }


    /**
     * Fang -每天晚上把取消跑腿订单
     * 每天晚上0点0分0秒逻辑
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void cancelRunOrdersTask() {
        //流水号置为0
        QueryWrapper<RunOrders> wrapper = new QueryWrapper<>();
        wrapper.eq("status","待付款");
        List<RunOrders> runOrdersList =  runOrdersService.list(wrapper);
        if (runOrdersList.size() < 1){
            return;
        }
        List<Long> runOrdersIds = PublicUtilS.getValueList(runOrdersList,"id");
        List<RunOrders> cancelRunOrders = new ArrayList<>();
        for (RunOrders runOrders : runOrdersList) {
            if ( !"待付款".equals(runOrders.getStatus())){
                continue;
            }
            RunOrders runOrdersCancel = new RunOrders();
            runOrdersCancel.setId(runOrders.getId());
            runOrdersCancel.setStatus("已取消");
            cancelRunOrders.add(runOrdersCancel);
        }
        if (cancelRunOrders.size() < 1){
            return;
        }
        boolean cancelRunTrue = runOrdersService.updateBatchById(cancelRunOrders);
        if (!cancelRunTrue){
            //如果有失败的计日志
            LoggerUtil.log("定时取消跑腿订单失败，订单号是："+runOrdersIds.toString());
        }
    }


    public String getYesterdayByCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        Date time = calendar.getTime();
        return new SimpleDateFormat("yyyy-MM-dd").format(time);
    }


    @Scheduled(cron = "0 0 2 * * ?")
    public void jisuan() {
        Date date = new Date();
        this.theDayilyStatistics(date);
    }

    private void theDayilyStatistics(Date runTaskDay) {
        long start = System.currentTimeMillis();
        String day = TimeUtilS.theYesterdayByCalendar(runTaskDay);
        String theDayBeforeYesterday = TimeUtilS.theDayBeforeYesterday(runTaskDay);
        Map<String, Object> map = new HashMap<>();
        map.put("day", day + "%");
        QueryWrapper<School> wrapper = new QueryWrapper<School>();
        wrapper.lambda().eq(School::getIsDelete, 0);
        List<School> schools = schoolService.list(wrapper);
        //统计需要学校每天提现的金额，查2点的前一天的前一天的所有学校
        QueryWrapper<DayLogTakeout> txSchoolWrapper = new QueryWrapper<DayLogTakeout>();
        txSchoolWrapper.lambda().eq(DayLogTakeout::getType,StatisticsConstants.DAY_SCHOOL_CAN_TX_MONEY);
        txSchoolWrapper.lambda().like(DayLogTakeout::getDay,theDayBeforeYesterday);
        List<DayLogTakeout> allTxSchoolList = dayLogTakeoutService.list(txSchoolWrapper);
        Map<Integer,DayLogTakeout> dayLogTxMap = PublicUtilS.listForMapValueE(allTxSchoolList,"selfId");

        //统计学校当日提现
        QueryWrapper<TxLog> txLog = new QueryWrapper<>();
        txLog.eq("type","代理提现");
        // todo
        txLog.ge("create_time",TimeUtilS.getDayBegin(runTaskDay,-1));
        txLog.le("create_time",TimeUtilS.getDayEnd(runTaskDay,-1));
        List<TxLog> txProxyList = txLogService.list(txLog);
        Map<Integer,List<TxLog>> txProxyListMap = PublicUtilS.listforEqualKeyListMap(txProxyList,"txerId");
        //统计店铺提现
        txLog.eq("type","商家提现");
        // todo
        txLog.ge("create_time",TimeUtilS.getDayBegin(runTaskDay,-1));
        txLog.le("create_time",TimeUtilS.getDayEnd(runTaskDay,-1));
        List<TxLog> txShoperList = txLogService.list(txLog);
        Map<Integer,List<TxLog>> txShoperListMap = PublicUtilS.listforEqualKeyListMap(txShoperList,"txerId");
        for (School schooltemp : schools) {
            //当天学校总钱
            BigDecimal toDaySchoolAllMoney = new BigDecimal(0);
            BigDecimal schoolEveryDayGetTotal = new BigDecimal(0);
            BigDecimal shopEveryDayGetTotal = new BigDecimal(0);
            BigDecimal shopsAllTxMoney = new BigDecimal(0);
            BigDecimal downSendMoney = new BigDecimal(0);
            Integer downSendCount = 0;
            List<Shop> shops = shopService.list(new QueryWrapper<Shop>().lambda().eq(Shop::getSchoolId, schooltemp.getId()));
            for (Shop shoptemp : shops) {
                map.put("shopId", shoptemp.getId());
                Orders result = ordersService.completeByShopId(map);
                DayLogTakeout shopDayLog = new DayLogTakeout()
                        .shoplog(shoptemp.getShopName(), shoptemp.getId(), day, result, "商铺日志", schooltemp.getId());
                BigDecimal schoolGetTotal = new BigDecimal(0);
                BigDecimal shopGetTotal = new BigDecimal(0);
                if (result.getSchoolGetTotal() != null){
                    schoolGetTotal = result.getSchoolGetTotal();
                }
                if (result.getComplete() != null&& result.getComplete().getShopGetTotal() != null){
                    shopGetTotal = result.getComplete().getShopGetTotal();
                }
                toDaySchoolAllMoney = toDaySchoolAllMoney.add(schoolGetTotal).add(shopGetTotal);
                //每日学校负责人所得
                schoolEveryDayGetTotal = schoolEveryDayGetTotal.add(schoolGetTotal);
                //店铺所得
                shopEveryDayGetTotal = shopEveryDayGetTotal.add(shopGetTotal);
                //计算每天店铺提现
                BigDecimal shopDayTxMoney = new BigDecimal(0);
                List<TxLog> shopTxList = txShoperListMap.get(shoptemp.getId());
                if (shopTxList == null ){
                    shopTxList = new ArrayList<>();
                }
                for (TxLog log : shopTxList) {
                    shopDayTxMoney = shopDayTxMoney.add(log.getAmount());
                }
                shopsAllTxMoney = shopsAllTxMoney.add(shopDayTxMoney);
                if (result.getDownSendMoney() == null){
                    result.setDownSendMoney(new BigDecimal(0));
                }
                downSendMoney = downSendMoney.add(result.getDownSendMoney());
                downSendCount = downSendCount + result.getDownSendCount();
                shopDayLog.setShopDayTx(shopDayTxMoney);
                //保存店铺
                dayLogTakeoutService.save(shopDayLog);
            }
            //////////////////////////////////////////////////学校统计///////////////////////////////////////////////////////////
            map.put("schoolId", schooltemp.getId());
            List<Orders> result = ordersService.completeBySchoolId(map);
            DayLogTakeout schoolDayLog = new DayLogTakeout()
                    .schoollog(schooltemp.getName(), schooltemp.getId(), day, result, "学校商铺日志", schooltemp.getAppId());
            //保存学校商铺日志
            dayLogTakeoutService.save(schoolDayLog);
            //////////////////////////////////////////////////跑腿日志///////////////////////////////////////////////////////////
            List<RunOrdersTj> runOrdersTjs = runOrdersService.tj(schooltemp.getId(), day + "%");
            DayLogTakeout runLog = new DayLogTakeout(day, schooltemp, runOrdersTjs);
            dayLogTakeoutService.save(runLog);
            //////////////////////////////////////////////////跑腿日志///////////////////////////////////////////////////////////

            //////////////////////////////////每日提现和截至可提现统计////////////////////////////////////////
            DayLogTakeout moneySave = schoolDayLog;
            DayLogTakeout dayLogTakeoutTemp = dayLogTxMap.get(schooltemp.getId());
            //统计前一天截至可提现的钱,不能为null
            BigDecimal lastDaySchoolAllMoney = new BigDecimal(0);
            BigDecimal lastDaySchoolDayTx = new BigDecimal(0);
            if(dayLogTakeoutTemp != null){
                lastDaySchoolAllMoney = dayLogTakeoutTemp.getSchoolAllMoney();
                lastDaySchoolDayTx = dayLogTakeoutTemp.getSchoolDayTx();
            }
            //统计当天代理提现的钱
            moneySave.setType(StatisticsConstants.DAY_SCHOOL_CAN_TX_MONEY);
            BigDecimal tx = new BigDecimal(0);
            List<TxLog> proxyList = txProxyListMap.get(schooltemp.getId());
            if (proxyList == null ){
                proxyList = new ArrayList<>();
            }
            for (TxLog log : proxyList) {
                tx = tx.add(log.getAmount());
            }
            //学校当日提现
            moneySave.setSchoolDayTx(tx);
            //学校所有店铺当日提现
            moneySave.setShopDayTx(shopsAllTxMoney);
            toDaySchoolAllMoney = toDaySchoolAllMoney.subtract(tx).add(lastDaySchoolAllMoney);
            //每日可提现加上跑腿所得
            BigDecimal runSchoolGet = new BigDecimal(0);
            if (runLog.getSelfGet() != null){
                runSchoolGet = runLog.getSelfGet();
            }
            toDaySchoolAllMoney = toDaySchoolAllMoney.add(runSchoolGet);
            moneySave.setSchoolAllMoney(toDaySchoolAllMoney);
            //学校所得
            moneySave.setSchoolGetTotal(schoolEveryDayGetTotal);
            //使用微信payGet字段记录店铺所得总和shopEveryDayGetTotal
            moneySave.setWxPayGet(shopEveryDayGetTotal);
            moneySave.setDownSendCount(downSendCount);
            moneySave.setDownSendMoney(downSendMoney);
            //保存当日提现
            dayLogTakeoutService.save(moneySave);
            logger.error("定时数据统计-"+schooltemp.getName()+TimeUtilS.dateFormat(new Date()));
        } //schoolFor

        logger.info("每天总的数据统计——theDayilyStatistics-耗时（ms）:" + (System.currentTimeMillis() - start));
    }

    @Scheduled(cron = "1 0 0 * * ?")
    public void initday() {
        SignController.day = Integer.valueOf(new SimpleDateFormat("yyyyMMdd").format(new Date()));
    }


    /**
     * 每n分钟执行一次,隔10分钟提醒一次
     * 提醒学校负责人，商家有超时未接手订单
     */
    @Scheduled(cron = "0 */5 * * * ?")
    public void remindUntakenOrders() {
        // 查询所有待接手订单
        List<Orders> ordersList;
        if (SpringUtil.redisCache()) {
            ordersList = JSON.parseArray(stringRedisTemplate.boundHashOps("ALL_DJS").values().toString(), Orders.class);
        } else {
            ordersList = ordersService.findAllDjs();
        }
        if (ordersList == null) {
            return;
        }
        //TODO
        //服务重启的话缓存会漏单
        /**
         * 批量查询shop信息
         */
        List<Integer> shopIdList = PublicUtilS.getValueList(ordersList, "shopId");
        Collection<Shop> shopCollection = shopService.listByIds(shopIdList);
        Map<Integer, Shop> shopMap = PublicUtilS.listForMapValueE(shopCollection, "id");
        /**
         * 批量查询school信息
         */
        List<Integer> schoolIdList = PublicUtilS.getValueList(ordersList, "schoolId");
        Collection<School> schoolCollection = schoolService.listByIds(schoolIdList);
        Map<Integer, School> schoolMap = PublicUtilS.listForMapValueE(schoolCollection, "id");
        for (Orders temp : ordersList) {
            Shop shop = null;
            if (shopMap.get(temp.getShopId()) != null) {
                shop = shopMap.get(temp.getShopId());
            } else {
                continue;
            }
            School school = null;
            if (schoolMap.get(temp.getSchoolId()) != null) {
                school = schoolService.findById(temp.getSchoolId());
            } else {
                continue;
            }
            // 获取当前时间
            long current = System.currentTimeMillis();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String curTime = df.format(current);
            // 获取订单支付时间
            long differTime = TimeUtilS.dateDiff(temp.getPayTime(), curTime);
            if (differTime > timeOut) {
                if (stringRedisTemplate.opsForValue().get("SCHOOL_NOTIFY_SHOP" + school.getPhone()) == null) {
                    try {
                        if (shop == null ){
                            shop = new Shop();
                            shop.setShopPhone("系统未查询到店家手机号");
                        }
                        if (shop.getShopPhone() == null){
                            shop.setShopPhone("系统未查询到店家手机号");
                        }
                        Util.qqsms(1400169549, "0eb188f83ef4b2dc8976b5e76c70581e", school.getMessagePhone(), 372755, shop.getShopName() + "," + shop.getShopPhone(), null);
                    } catch (Exception e) {
                        LoggerUtil.log(e.getMessage());
                    }
                    stringRedisTemplate.opsForValue().set("SCHOOL_NOTIFY_SHOP" + school.getMessagePhone(), "", 5, TimeUnit.MINUTES);
                }
            }
        }//for
    }

    /**
     * 判断学校优惠券是否失效
     * 每天上午10点,下午14点,18点,晚上22点执行一次+
     */
    @Scheduled(cron = "0 0 10,14,18,22 * * ?")
    public void couponInvalid() {
        Integer invalidUserCouponCount = tCouponService.countInvalidCoupon();
        if (invalidUserCouponCount < NumConstants.INT_NUM_1){
            return;
        }
        // 查出来的总数，每次批量更新1000条数据，循环更新完
        int cycleNum = invalidUserCouponCount/NumConstants.INT_NUM_1000 + 1;
        for (int i = 0;i < cycleNum;i++){
            List<Coupon> couponList = tCouponService.limitFindInvalidUserCoupon();
            if (CollectionUtils.isEmpty(couponList)){
                return;
            }
            List<Long> idList = PublicUtilS.getValueList(couponList,"id");
            if (CollectionUtils.isEmpty(idList)){
                return;
            }
            Integer updateNum = tCouponService.batchUpdateToUnInvalidByIds(idList);
            if (updateNum < NumConstants.INT_NUM_1){
                LoggerUtil.logError("定时器批量更新失败-优惠券失效-ids-"+idList.toString());
            }
        }
    }

    /**
     * 判断用户优惠券是否失效
     * 每天上午8点,下午10点,14点,16点，晚上22点执行一次
     */
    @Scheduled(cron = "0 0 8,10,14,16,22 * * ?")
    public void wxUserCouponInvalid() {
        Integer invalidUserCouponCount = tWxUserCouponService.countInvalidUserCoupon();
        if (invalidUserCouponCount < NumConstants.INT_NUM_1){
            return;
        }
        // 查出来的总数，每次批量更新1000条数据，循环更新完
        int cycleNum = invalidUserCouponCount/NumConstants.INT_NUM_1000 + 1;
        for (int i = 0;i < cycleNum;i++){
            List<WxUserCoupon> wxUserCoupons = tWxUserCouponService.limitFindInvalidUserCoupon();
            if (CollectionUtils.isEmpty(wxUserCoupons)){
                return;
            }
            List<Long> idList = PublicUtilS.getValueList(wxUserCoupons,"id");
            if (CollectionUtils.isEmpty(idList)){
                return;
            }
            Integer updateNum = tWxUserCouponService.batchUpdateToUnInvalidByIds(idList);
            if (updateNum < NumConstants.INT_NUM_1){
                LoggerUtil.logError("定时器批量更新失败-用户优惠券失效-ids-"+idList.toString());
            }
        }
    }

}
