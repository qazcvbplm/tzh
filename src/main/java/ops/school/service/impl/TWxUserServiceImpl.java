package ops.school.service.impl;

import ops.school.api.config.Server;
import ops.school.api.entity.Charge;
import ops.school.api.entity.School;
import ops.school.api.entity.WxUser;
import ops.school.api.entity.WxUserBell;
import ops.school.api.service.ChargeService;
import ops.school.api.service.SchoolService;
import ops.school.api.service.WxUserBellService;
import ops.school.api.service.WxUserService;
import ops.school.api.util.Util;
import ops.school.api.wxutil.WXpayUtil;
import ops.school.service.TWxUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return wxUserBellService.getById(wxUser.getOpenId() + "-" + wxUser.getPhone());
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
}
