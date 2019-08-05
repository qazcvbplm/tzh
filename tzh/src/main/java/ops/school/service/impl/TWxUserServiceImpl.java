package ops.school.service.impl;

import com.alibaba.fastjson.JSON;
import ops.school.api.config.Server;
import ops.school.api.dao.WxUserBellMapper;
import ops.school.api.entity.*;
import ops.school.api.exception.YWException;
import ops.school.api.service.*;
import ops.school.api.util.LoggerUtil;
import ops.school.api.util.Util;
import ops.school.api.wxutil.WXpayUtil;
import ops.school.constants.NumConstants;
import ops.school.service.TWxUserService;
import ops.school.util.Base64Util;
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
    @Autowired
    private WxUserBellMapper wxUserBellMapper;

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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void chargeSuccess(String orderId, String openId, String attach) {
        WxUser wxUser = wxUserService.findById(openId);
        School school = schoolService.findById(wxUser.getSchoolId());
        Charge charge = chargeService.getById(Integer.valueOf(attach));
        //todo 修改支付流水
        //ChargeLog log = new ChargeLog(orderId, new BigDecimal(charge.getFull()), new BigDecimal(charge.getSend()), openId, wxUser.getAppId());
        ChargeLog log = new ChargeLog();
        if (orderId != null){
            try {
                log.setOpenId(openId);
                log.setAppId(wxUser.getAppId());
                log.setPay(new BigDecimal(charge.getFull()));
                log.setSend(new BigDecimal(charge.getSend()));
                //一、获取当前系统时间和日期并格式化输出:
                //设置日期格式
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                // Formats a Date into a date/time string.
                String dateTime = df.format(new Date());
                log.setCreateTime(dateTime);
                log.setWxTradeNo(orderId);
                // 添加进充值记录
                boolean chargeLogTrue = chargeLogService.save(log);
                if (!chargeLogTrue){
                    LoggerUtil.logError("微信充值错误-chargeLog日志失败-orderId"+orderId+"-openId"+openId+"attach"+attach);
                }
            }catch (Exception ex){
                LoggerUtil.logError("微信充值回调错误-orderId"+orderId+"-openId"+openId+"attach"+attach+ex);
            }

        }else {
            LoggerUtil.logError("微信充值回调错误-orderId为null-orderId"+orderId+"-openId"+openId+"attach"+attach);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("phone", wxUser.getOpenId() + "-" + wxUser.getPhone());
        map.put("amount", log.getPay());
        Integer addBellNum = wxUserBellService.charge(map);
        if (addBellNum != NumConstants.INT_NUM_1){
            LoggerUtil.log("充值余额失败：" + wxUser.getPhone() + "" + (log.getPay().add(log.getSend()).toString())+"orderId"+orderId+"-openId"+openId+"attach"+attach);
        }
        // 把赠送金额存进用户的粮票里面
        Boolean addFoodTrue = wxUserBellService.addFoodCoupon(wxUser.getOpenId() + "-" + wxUser.getPhone(), log.getSend());
        if (!addFoodTrue){
            LoggerUtil.log("充值粮票失败：" + wxUser.getPhone() + "" + (log.getPay().add(log.getSend()).toString())+"orderId"+orderId+"-openId"+openId+"attach"+attach);
        }
        //学校充值
        School rechargeChargeSendBell = new School();
        rechargeChargeSendBell.setUserCharge(BigDecimal.valueOf(charge.getFull()));
        rechargeChargeSendBell.setUserBellAll(BigDecimal.valueOf(charge.getFull()));
        rechargeChargeSendBell.setUserChargeSend(BigDecimal.valueOf(charge.getSend()));
        rechargeChargeSendBell.setId(school.getId());
        //增加user_charge，user_bell_all，user_charge_send，by id
        Integer rechargeChargeSendBellNum = 0;
        try{
            rechargeChargeSendBellNum = schoolService.rechargeScChargeSendBellByModel(rechargeChargeSendBell);
        }catch (Exception ex){
            LoggerUtil.log("充值后学校更新充值数据失败：" + wxUser.getPhone() + "" + (log.getPay().add(log.getSend()).toString())+"orderId"+orderId+"-openId"+openId+"attach"+attach+ex);
        }

        if (rechargeChargeSendBellNum != NumConstants.INT_NUM_1){
            LoggerUtil.log("充值后学校更新充值数据失败：" + wxUser.getPhone() + "" + (log.getPay().add(log.getSend()).toString())+"orderId"+orderId+"-openId"+openId+"attach"+attach);
        }
    }

    @Override
    public int decryptPhone(String decryptData, String sessionKey, String ivData, String openid) {
        String phoneNumber = null;
        String jsonString = null;
        WxUser wxUser = wxUserService.findById(openid);
        try {
            jsonString = Base64Util.getPhoneNumberBeanS5(decryptData,sessionKey,ivData);
            if (jsonString == null || jsonString.length() == 0){
                return 0;
            }
            // json串转换成map集合
            Map<String, Object> map;
            map = JSON.parseObject(jsonString, HashMap.class);
            phoneNumber = (String) map.get("phoneNumber");
            if (wxUser != null){
                // 如果手机号为空时，则需要添加手机号，并且生成wx_user_bell记录
                System.out.println("-------"+wxUser.getPhone() + "------");
                if (wxUser.getPhone() == null || wxUser.getPhone().length() == 0){
                    if (phoneNumber != null || phoneNumber != ""){
//                        if (wxUserBellMapper.findByPhone(openid + "-" + phoneNumber) == 0) {
//                            wxUserBellMapper.insert(new WxUserBell(openid + "-" + phoneNumber,wxUser.getId()));
//                        }
                        // 手机号加openid存进去
                        wxUserBellMapper.insert(new WxUserBell(openid + "-" + phoneNumber,wxUser.getId()));
                        // 手机号存进去
                        wxUser.setPhone(phoneNumber);
                        // 修改用户信息
                        wxUserService.updateById(wxUser);
                    } else {
                        return 0;
                    }
                } else {
                    // 手机号不为空时
                    // 如果手机号与之前手机号不一致时
                    if (phoneNumber!= null || phoneNumber != ""){
                        // 如果wx_user_bell表查不到该用户记录，则创建一条
                        if (wxUserBellMapper.findByPhone(openid + "-" + wxUser.getPhone()) == 0) {
                            wxUserBellMapper.insert(new WxUserBell(openid + "-" + phoneNumber,wxUser.getId()));
                        } else {
                            // 如果可以查到记录，则修改
                            WxUserBell wxUserBell = wxUserBellMapper.selectByPrimaryKey(openid+"-"+wxUser.getPhone());
                            Map<String,Object> map1 = new HashMap<>();
                            map1.put("updatePhone",openid + "-" + phoneNumber);
                            map1.put("phone",wxUserBell.getPhone());
                            int rs = wxUserBellMapper.updatePhone(map1);
                            if (rs == 0){
                                return 0;
                            }
                        }

                        // 修改用户手机号
                        wxUser.setPhone(phoneNumber);
                        wxUserService.updateById(wxUser);
                    } else {
                        return 0;
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return 1;
    }
}
