package ops.school.service.impl;

import ops.school.api.entity.*;
import ops.school.api.exception.YWException;
import ops.school.api.service.*;
import ops.school.api.util.LoggerUtil;
import ops.school.api.wx.towallet.WeChatPayUtil;
import ops.school.service.TCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class TCommonServiceImpl implements TCommonService {

    @Autowired
    private SenderService senderService;
    @Autowired
    private WxUserService wxUserService;
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private WxUserBellService wxUserBellService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private TxLogService txLogService;

    /**
     * @param sourceId 配送员/商家提现来源账户（senderId, shopId）
     * @param userId   提现指定账户
     * @return
     */
    @Transactional
    @Override
    public int txApply(BigDecimal amount, String sourceId, String userId) {
        Pattern pt = Pattern.compile("[0-9]*");
        // 提现指定账户
        WxUser wxUser = wxUserService.findById(userId);
        TxLog log = new TxLog();
        // 满足以下提现的是商家提现
        if (!sourceId.isEmpty() && pt.matcher(sourceId).matches()) {
            // 商家信息
            Shop shop = shopService.getById(Integer.valueOf(sourceId));
            log = new TxLog(shop.getId(), "商家提现", null, amount, "", shop.getSchoolId(), wxUser.getAppId());
        } else if (!sourceId.isEmpty()) {
            // 配送员提现
            Sender sender = senderService.check(sourceId);
            log = new TxLog(sender.getId(), "配送员提现", null, amount, "", sender.getSchoolId(),
                    wxUser.getAppId());
        }
        // 申请提现设置isTx为0
        log.setIsTx(0);
        boolean result = txLogService.save(log);
        if (result) {
            return 1;
        } else {
            return 0;
        }
    }

    @Transactional
    @Override
    public int txAudit(BigDecimal amount,Integer txId, Integer status,String sourceId, String userId) {
        Pattern pt = Pattern.compile("[0-9]*");
        // 提现指定账户
        WxUser wxUser = wxUserService.findById(userId);
        // 通过txId查询提现记录表
        TxLog log = txLogService.getById(txId);
        // 满足下面条件的是配送员提现
        if (!pt.matcher(sourceId).matches()) {
            Sender sender = senderService.check(sourceId);
            School school = schoolService.findById(sender.getSchoolId());
            if (status == 1) {
                Map<String, Object> map = new HashMap();
                map.put("phone", sourceId + "-" + sender.getPhone());
                map.put("amount", amount);
                map.put("schoolId", sender.getSchoolId());
                // 从配送员余额中扣除提现金额
                if (wxUserBellService.pay(map) == 1) {
                    try {
                        String payId = "tx" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                        // 审核成功(提现成功)
                        log.setIsTx(1);
                        if (WeChatPayUtil.transfers(school.getWxAppId(), school.getMchId(), school.getWxPayId(),
                                school.getCertPath(), payId, "127.0.0.1", amount, wxUser.getOpenId(),
                                log) == 1) {
                            txLogService.updateById(log);
                            if (schoolService.sendertx(map) == 0) {
                                LoggerUtil.log("配送员提现学校减少金额失败:" + sourceId + ":" + amount);
                            }
                            return 1;
                        }
                    } catch (Exception e) {
                        LoggerUtil.log(sourceId + ":" + amount + e.getMessage());
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    }
                    return 2;
                }
                throw new YWException("余额不足");
            } else if (status == 2) {
                // 审核失败（提现失败）
                log.setIsTx(2);
                txLogService.updateById(log);
                return 2;
            }
        } else {
            // 商家信息
            Shop shop = shopService.getById(Integer.valueOf(sourceId));
            School school = schoolService.findById(shop.getSchoolId());
            if (status == 1){
                Map<String,Object> map = new HashMap<>();
                map.put("amount",amount);
                map.put("shopId",shop.getId());
                if (shopService.shoptx(map) == 1) {

                    
                    try {
                        String payId = "tx" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                        // 审核成功(提现成功)
                        log.setIsTx(1);
                        if (WeChatPayUtil.transfers(school.getWxAppId(), school.getMchId(), school.getWxPayId(),
                                school.getCertPath(), payId, "127.0.0.1", amount, wxUser.getOpenId(),
                                log) == 1) {
                            txLogService.updateById(log);
                            if (schoolService.sendertx(map) == 0) {
                                LoggerUtil.log("配送员提现学校减少金额失败:" + sourceId + ":" + amount);
                            }
                            return 1;
                        }
                    } catch (Exception e) {
                        LoggerUtil.log(sourceId + ":" + amount + e.getMessage());
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    }
                    return 2;
                }
                throw new YWException("余额不足");
            } else if (status == 2) {
                // 审核失败（提现失败）
                log.setIsTx(2);
                txLogService.updateById(log);
                return 2;
            }
        }
        return 0;
    }
}
