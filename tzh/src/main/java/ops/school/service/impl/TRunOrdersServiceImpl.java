package ops.school.service.impl;

import com.alibaba.fastjson.JSON;
import ops.school.api.dao.RunOrdersMapper;
import ops.school.api.dto.wxgzh.Message;
import ops.school.api.entity.*;
import ops.school.api.exception.YWException;
import ops.school.api.service.*;
import ops.school.api.wx.refund.RefundUtil;
import ops.school.api.wxutil.AmountUtils;
import ops.school.api.wxutil.WxGUtil;
import ops.school.service.TRunOrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

    @Transactional
    @Override
    public int cancel(String id) {
        RunOrders orders = runOrdersService.getById(id);
        if (System.currentTimeMillis() - orders.getPayTimeLong() < 5 * 60 * 1000) {
            return 2;
        }
        if (runOrdersService.cancel(id) == 1) {
            if (orders.getPayment().equals("微信支付")) {
                School school = schoolService.findById(orders.getSchoolId());
                String fee = AmountUtils.changeY2F(orders.getTotalPrice().toString());
                int result = RefundUtil.wechatRefund1(school.getWxAppId(), school.getWxSecret(), school.getMchId(), school.getWxPayId(), school.getCertPath(),
                        orders.getId(), fee, fee);
                if (result != 1) {
                    throw new YWException("退款失败联系管理员");
                } else {
                    return 1;
                }
            }
            if (orders.getPayment().equals("余额支付")) {
                Map<String, Object> map = new HashMap<>();
                WxUser user = wxUserService.findById(orders.getOpenId());
                map.put("phone", user.getOpenId() + "-" + user.getPhone());
                map.put("amount", orders.getTotalPrice());
                if (wxUserBellService.charge(map) == 1) {
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
            stringRedisTemplate.boundHashOps("FORMID" + orders.getId()).put(orders.getId(), JSON.toJSONString(formIds));
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
