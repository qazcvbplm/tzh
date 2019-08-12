package ops.school.service.impl;

import ops.school.api.dao.RunOrdersMapper;
import ops.school.api.entity.*;
import ops.school.api.exception.YWException;
import ops.school.api.service.*;
import ops.school.api.util.LoggerUtil;
import ops.school.api.util.RedisUtil;
import ops.school.api.wx.refund.RefundUtil;
import ops.school.api.wxutil.AmountUtils;
import ops.school.api.constants.NumConstants;
import ops.school.service.TRunOrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class TRunOrdersServiceImpl implements TRunOrdersService {


    @Autowired
    private RunOrdersService runOrdersService;
    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private WxUserService wxUserService;
    @Autowired
    private WxUserBellService wxUserBellService;
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RunOrdersMapper runOrdersMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Transactional
    @Override
    public int cancel(String id) {
        RunOrders runOrders = runOrdersService.getById(id);
        if (System.currentTimeMillis() - runOrders.getPayTimeLong() < 5 * 60 * 1000) {
            return 2;
        }
        if (runOrdersService.cancel(id) == 1) {
            if (runOrders.getPayment().equals("微信支付")) {
                School school = schoolService.findById(runOrders.getSchoolId());
                String fee = AmountUtils.changeY2F(runOrders.getTotalPrice().toString());
                int result = RefundUtil.wechatRefund1(school.getWxAppId(), school.getWxSecret(), school.getMchId(), school.getWxPayId(), school.getCertPath(),
                        runOrders.getId(), fee, fee);
                if (result != 1) {
                    throw new YWException("退款失败联系管理员");
                } else {
                    //取消跑腿订单计入缓存
                    redisUtil.cancelRunOrdersAdd(runOrders.getSchoolId());
                    return 1;
                }
            }
            if (runOrders.getPayment().equals("余额支付")) {
                Map<String, Object> map = new HashMap<>();
                WxUser user = wxUserService.findById(runOrders.getOpenId());
                map.put("phone", user.getOpenId() + "-" + user.getPhone());
                map.put("amount", runOrders.getTotalPrice());
                if (wxUserBellService.charge(map) == 1) {
                    //取消后把钱加会学校表，余额剩余,钱包剩余,user_bell_all
                    School schoolCancel = new School();
                    schoolCancel.setId(runOrders.getSchoolId());
                    schoolCancel.setUserCharge(BigDecimal.valueOf(0));
                    schoolCancel.setUserBellAll(runOrders.getTotalPrice());
                    schoolCancel.setUserChargeSend(BigDecimal.valueOf(0));
                    Integer cancelAddSchool = schoolService.rechargeScChargeSendBellByModel(schoolCancel);
                    if (cancelAddSchool != NumConstants.INT_NUM_1){
                        LoggerUtil.logError("跑腿订单取消退还学校余额错误-runOrdersId"+runOrders.getId());
                    }
                    //取消跑腿订单计入缓存
                    redisUtil.cancelRunOrdersAdd(runOrders.getSchoolId());
                    return 1;
                } else {
                    throw new YWException("退款失败联系管理员");
                }
            }

        }
        return 0;
    }

    @Override
    public int pay(RunOrders orders,String formid) {
        Application application = applicationService.getById(orders.getAppId());
        if (application.getVipRunDiscountFlag() == 1) {
            orders.setTotalPrice((orders.getTotalPrice().multiply(application.getVipRunDiscount())).setScale(2, BigDecimal.ROUND_HALF_DOWN));
        }
        Map<String, Object> map = new HashMap<>();
        WxUser user = wxUserService.findById(orders.getOpenId());
        map.put("phone", user.getOpenId() + "-" + user.getPhone());
        map.put("amount", orders.getTotalPrice());
        if (wxUserBellService.pay(map) == 1) {
            if (runOrdersService.paySuccess(orders.getId(), "余额支付") == 0) {
                throw new YWException("订单状态异常");
            }
            WxUserBell userbell = wxUserBellService.getById(user.getOpenId() + "-" + user.getPhone());
            String[] formIds = formid.split(",");
            if (formIds.length < 1){
                LoggerUtil.logError("runOrder pay formid为空"+ orders.getId());
            }
            stringRedisTemplate.boundListOps("FORMID" + orders.getId()).leftPushAll(formIds);
            stringRedisTemplate.boundListOps("FORMID" + orders.getId()).expire(1, TimeUnit.DAYS);
            /*wxUserService.sendWXGZHM(user.getPhone(), new Message(null, "JlaWQafk6M4M2FIh6s7kn30yPdy2Cd9k2qtG6o4SuDk",
                    null, null
                    + orders.getId() + "&typ=" + orders.getTyp(),
                    " 您的会员帐户余额有变动！", "暂无", "-" + orders.getTotalPrice(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
                    "消费", userbell.getMoney() + "", null, null, null,
                    null, "如有疑问请在小程序内联系客服人员！"));*/
            return 1;
        } else {
            throw new YWException("余额不足");
        }
    }

    @Override
    public BigDecimal countTotalPriceByFloor(Integer floorId, String beginTime, String endTime) {
        Map<String,Object> map = new HashMap<>();
        map.put("floorId",floorId);
        map.put("beginTime",beginTime);
        map.put("endTime",endTime);
        return runOrdersMapper.countTotalPriceByFloor(map);
    }

}
