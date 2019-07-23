package ops.school.service.impl;

import ops.school.api.config.Server;
import ops.school.api.dto.wxgzh.Message;
import ops.school.api.entity.*;
import ops.school.api.service.*;
import ops.school.api.util.LoggerUtil;
import ops.school.api.util.Util;
import ops.school.api.wxutil.WXpayUtil;
import ops.school.constants.NumConstants;
import ops.school.service.TWxUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class TWxUserServiceImpl implements TWxUserService {


    @Autowired
    private WxUserService wxUserService;
    @Autowired
    private WxUserBellService wxUserBellService;
    @Autowired
    private ChargeService chargeService;
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private ChargeLogService chargeLogService;
    @Autowired
    private LogsService logsService;

    @Override
    public int addSource(String openId, Integer source) {
        WxUser wxUser = wxUserService.findById(openId);
        Map<String, Object> map2 = new HashMap<>();
        map2.put("phone", wxUser.getOpenId() + "-" + wxUser.getPhone());
        map2.put("source", source);
        wxUserBellService.addSource(map2);
        return 0;
    }

    @Override
    public WxUserBell getbell(String openId) {
        WxUser wxUser = wxUserService.findById(openId);
        WxUserBell wxUserBell = wxUserBellService.getById(wxUser.getOpenId() + "-" + wxUser.getPhone());
        if (wxUserBell == null){
            wxUserBell = new WxUserBell();
            wxUserBell.setMoney(BigDecimal.ZERO);
            wxUserBell.setFoodCoupon(BigDecimal.ZERO);
            wxUserBell.setSource(NumConstants.INT_NUM_0);
            return wxUserBell;
        }
        if (wxUserBell.getMoney() == null){
            wxUserBell.setMoney(BigDecimal.ZERO);
        }
        if (wxUserBell.getFoodCoupon() == null){
            wxUserBell.setFoodCoupon(BigDecimal.ZERO);
        }
        if (wxUserBell.getSource() == null){
            wxUserBell.setSource(NumConstants.INT_NUM_0);
        }
        return wxUserBell;
    }

    @Override
    public Object charge(String openId, int chargeId) {
        Charge charge = chargeService.getById(chargeId);
        WxUser wxUser = wxUserService.findById(openId);
        School school = schoolService.findById(wxUser.getSchoolId());

        if (charge != null && wxUser != null && school != null) {
            return WXpayUtil.payrequest(school.getWxAppId(), school.getMchId(), school.getWxPayId(),
                    "椰子-w", Util.GenerateOrderId(), charge.getFull() * 100 + "", openId,
                    "127.0.0.1", chargeId + "", Server.URL + "notify/charge");
        }
        return null;
    }

    @Transactional
    @Override
    public void chargeSuccess(String orderId, String openId, String attach) {
        WxUser wxUser = wxUserService.findById(openId);
        School school = schoolService.findById(wxUser.getSchoolId());
        Charge charge = chargeService.getById(Integer.valueOf(attach));
        ChargeLog log = new ChargeLog(orderId, new BigDecimal(charge.getFull()), new BigDecimal(charge.getSend()), openId, wxUser.getAppId());
        chargeLogService.save(log); // 添加进充值记录
        Map<String, Object> map = new HashMap<>();
        map.put("phone", wxUser.getOpenId() + "-" + wxUser.getPhone());
        map.put("amount", log.getPay());
        if (wxUserBellService.charge(map) == 0) {
            // 把赠送金额存进用户的粮票里面
            wxUserBellService.addFoodCoupon(wxUser.getOpenId() + "-" + wxUser.getPhone(), log.getSend());
            LoggerUtil.log("充值失败：" + wxUser.getPhone() + "" + (log.getPay().add(log.getSend()).toString()));
        } else {
            Map<String, Object> map2 = new HashMap<>();
            map2.put("schoolId", school.getId());
            map2.put("charge", log.getPay());
            map2.put("send", log.getSend());
            try {
                schoolService.charge(map2);
            } catch (Exception e) {
                logsService.save(new Logs(e.getMessage()));
            }

            WxUserBell userbell = wxUserBellService.getById(wxUser.getOpenId() + "-" + wxUser.getPhone());
            //发送模板
            wxUserService.sendWXGZHM(wxUser.getPhone(), new Message(null,
                    "JlaWQafk6M4M2FIh6s7kn30yPdy2Cd9k2qtG6o4SuDk",
                    school.getWxAppId(),
                    "pages/mine/payment/payment",
                    "暂无",
                    "+" + log.getPay().add(log.getSend()),
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
                    , "充值", userbell.getMoney() + "", null, null, null, null, null, "如有疑问请在小程序内联系客服人员！"));
        }
    }
}
