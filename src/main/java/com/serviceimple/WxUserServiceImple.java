package com.serviceimple;

import com.alibaba.fastjson.JSON;
import com.controller.OrdersNotify;
import com.dao.*;
import com.entity.*;
import com.exception.YWException;
import com.service.SchoolService;
import com.service.WxUserService;
import com.util.LoggerUtil;
import com.util.SpringUtil;
import com.util.Util;
import com.wxutil.WXpayUtil;
import com.wxutil.WxGUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class WxUserServiceImple implements WxUserService {

    @Autowired
    private WxUserMapper wxUserMapper;
    @Autowired
    private WxUserBellMapper wxUserBellMapper;
    @Autowired
    private ChargeMapper chargeMapper;
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private ChargeLogMapper chargeLogMapper;
    @Autowired
    private LogsMapper logsMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public WxUser login(String openid, Integer schoolId, Integer appId, String client) {
        WxUser wxUser = findById(openid);
        if (wxUser == null) {
            wxUser = new WxUser(openid, client);
            wxUser.setAppId(appId);
            wxUser.setSchoolId(schoolId);
            wxUserMapper.insert(wxUser);
        }
        return wxUser;
    }

    @Transactional
    @Override
    public WxUser update(WxUser wxUser) {
        if (wxUser.getPhone() != null) {
            if (wxUserBellMapper.findByPhone(wxUser.getOpenId() + "-" + wxUser.getPhone()) == 0) {
                wxUserBellMapper.insert(new WxUserBell(wxUser.getOpenId() + "-" + wxUser.getPhone()));
            } else {
                throw new YWException("手机号码已被注册");
            }
        }
        wxUserMapper.updateByPrimaryKeySelective(wxUser);
        return wxUserMapper.selectByPrimaryKey(wxUser.getOpenId());
    }

    @Override
    public List<WxUser> find(WxUser wxUser) {
        if (wxUser.getQueryType().equals("school")) {
            wxUser.setSchoolId(Integer.valueOf(wxUser.getQuery()));
        }
        return wxUserMapper.find(wxUser);
    }

    @Override
    public Object charge(String openId, int chargeId) {
        Charge charge = chargeMapper.selectByPrimaryKey(chargeId);
        WxUser wxUser = wxUserMapper.selectByPrimaryKey(openId);
        School school = schoolService.findById(wxUser.getSchoolId());
        if (charge != null) {
            return WXpayUtil.payrequest(school.getWxAppId(), school.getMchId(), school.getWxPayId(),
                    "椰子-w", Util.GenerateOrderId(), charge.getFull() * 100 + "", openId,
                    "127.0.0.1", chargeId + "", OrdersNotify.URL + "notify/charge");
        }
        return null;
    }


    @Transactional
    @Override
    public void chargeSuccess(String orderId, String openId, String attach) {
        WxUser wxUser = wxUserMapper.selectByPrimaryKey(openId);
        School school = schoolService.findById(wxUser.getSchoolId());
        Charge charge = chargeMapper.selectByPrimaryKey(Integer.valueOf(attach));
        ChargeLog log = new ChargeLog(orderId, new BigDecimal(charge.getFull()), new BigDecimal(charge.getSend()), openId, wxUser.getAppId());
        chargeLogMapper.insert(log);
        Map<String, Object> map = new HashMap<>();
        map.put("phone", wxUser.getOpenId() + "-" + wxUser.getPhone());
        map.put("amount", log.getPay().add(log.getSend()));
        if (wxUserBellMapper.charge(map) == 0) {
            LoggerUtil.log("充值失败：" + wxUser.getPhone() + "" + (log.getPay().add(log.getSend()).toString()));
        } else {
            Map<String, Object> map2 = new HashMap<>();
            map2.put("schoolId", school.getId());
            map2.put("charge", log.getPay());
            map2.put("send", log.getSend());
            try {
                schoolService.charge(map2);
            } catch (Exception e) {
                logsMapper.insert(new Logs(e.getMessage()));
            }
            //发送模板
            WxUserBell userbell = wxUserBellMapper.selectByPrimaryKey(wxUser.getOpenId() + "-" + wxUser.getPhone());
            WxUser wxGUser = wxUserMapper.findGzh(wxUser.getPhone());
            if (wxGUser != null) {
                Map<String, String> mb = new HashMap<>();
                mb.put("touser", wxGUser.getOpenId());
                mb.put("template_id", "JlaWQafk6M4M2FIh6s7kn30yPdy2Cd9k2qtG6o4SuDk");
                mb.put("data_first", " 您的会员帐户余额有变动！");
                mb.put("data_keyword1", "暂无");
                mb.put("data_keyword2", "+" + log.getPay().add(log.getSend()));
                mb.put("data_keyword3", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                mb.put("data_keyword4", "充值");
                mb.put("data_keyword5", userbell.getMoney() + "");
                mb.put("data_remark", "如有疑问请在小程序内联系客服人员！");
                mb.put("min_appid", school.getWxAppId());
                mb.put("min_path", "pages/mine/payment/payment");
                WxGUtil.snedM(mb);
            }
        }
    }

    @Override
    public Object findcharge(String openId) {
        return chargeLogMapper.findByOpenId(openId);
    }


    @Override
    public WxUser findById(String openId) {
        if (SpringUtil.redisCache()) {
            String rs = stringRedisTemplate.opsForValue().get("WX_USER_OPENID_" + openId);
            if (rs != null) {
                return JSON.parseObject(rs, WxUser.class);
            } else {
                WxUser wxUser = wxUserMapper.selectByPrimaryKey(openId);
                stringRedisTemplate.opsForValue().set("WX_USER_OPENID_" + openId, JSON.toJSONString(wxUser), 1, TimeUnit.DAYS);
                return wxUser;
            }
        }
        return wxUserMapper.selectByPrimaryKey(openId);
    }

    @Override
    public int addSource(String openId, Integer source) {
        WxUser wxUser = findById(openId);
        Map<String, Object> map2 = new HashMap<>();
        map2.put("phone", wxUser.getOpenId() + "-" + wxUser.getPhone());
        map2.put("source", source);
        wxUserBellMapper.addSource(map2);
        return 0;
    }

    @Override
    public int countBySchoolId(int schoolId) {
        return wxUserMapper.countBySchoolId(schoolId);
    }

    @Override
    public List<WxUser> findByPhoneGZH(String query) {
        return wxUserMapper.findByPhoneGZH(query);
    }

    @Override
    public WxUser findGZH(String phone) {
        return wxUserMapper.findGzh(phone);
    }

    @Override
    public WxUser findGzh(String phone) {
        return wxUserMapper.findGzh(phone);
    }

    @Override
    public WxUser findByschoolAndPhone(WxUser query) {
        return wxUserMapper.findByschoolAndPhone(query);
    }

}
