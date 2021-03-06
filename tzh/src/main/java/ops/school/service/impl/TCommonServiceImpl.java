package ops.school.service.impl;

import ops.school.api.dao.TxLogMapper;
import ops.school.api.dao.WxUserBellMapper;
import ops.school.api.entity.*;
import ops.school.api.enums.ResponseViewEnums;
import ops.school.api.exception.DisplayException;
import ops.school.api.exception.YWException;
import ops.school.api.service.*;
import ops.school.api.util.LoggerUtil;
import ops.school.api.wx.towallet.WeChatPayUtil;
import ops.school.api.constants.NumConstants;
import ops.school.service.TCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
    @Autowired
    private WxUserBellMapper wxUserBellMapper;
    @Autowired
    private TxLogMapper txLogMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Transactional
    @Override
    public int txApply(BigDecimal amount, String senderId, String dzOpenid,Integer shopId) {
        // 提现指定账户
        WxUser wxUser = wxUserService.findById(dzOpenid);
        TxLog log = new TxLog();
        Map<String,Object> map = new HashMap<>();
        // 满足以下提现的是商家提现
        if (shopId != 0) {
            // 商家信息
            map.put("txerId",shopId);
            map.put("type","商家提现");
            if (txLogMapper.findTxLogs(map) > 0){
                return 3;
            }
            Shop shop = shopService.getById(shopId);
            if(shop.getshopTxFlag() == 0){
                return 2;
            }
            if (shop.getTxAmount().compareTo(amount) != -1){
                log = new TxLog(shop.getId(), "商家提现", null, amount, "", shop.getSchoolId(), wxUser.getAppId());
                log.setTxName(shop.getShopName());
            }
        } else  {
            // 配送员提现
            Sender sender = senderService.check(senderId);
            map.put("txerId",sender.getId());
            map.put("type","配送员提现");
            if (txLogMapper.findTxLogs(map) > 0){
                return 3;
            }
            log = new TxLog(sender.getId(), "配送员提现", null, amount, "", sender.getSchoolId(),
                    wxUser.getAppId());
            log.setTxName(sender.getName());
        }
        // 申请提现设置isTx为0
        log.setIsTx(0);
        // 提现到账openid
        log.setDzOpenid(dzOpenid);
        boolean result = txLogService.save(log);
        if (result) {
            return 1;
        } else {
            return 0;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int txAudit(Integer txId, Integer status) {
        // 通过txId查询提现记录表
        TxLog log = txLogService.getById(txId);
        // 提现指定账户
        WxUser wxUser = wxUserService.findById(log.getDzOpenid());
        // 满足下面条件的是配送员提现
        if (log.getType().equals("配送员提现")) {
            Sender sender = senderService.findById(log.getTxerId());
            School school = schoolService.findById(sender.getSchoolId());
            if (status == 1) {
                Map<String, Object> map = new HashMap();
                WxUserBell wxUserBell = wxUserBellMapper.selectByPrimaryKey(sender.getOpenId() + "-" + sender.getPhone());
                BigDecimal amount = wxUserBell.getMoney().subtract(log.getAmount());
                if (amount.compareTo(BigDecimal.ZERO) == -1){
                    throw new YWException("余额不足");
                }
                map.put("phone", sender.getOpenId() + "-" + sender.getPhone());
                map.put("amount", amount);
                map.put("schoolId", sender.getSchoolId());
                // 从配送员余额中扣除提现金额
                int re = wxUserBellMapper.txUpdate(map);
                if (re != NumConstants.INT_NUM_1){
                    // 扣除失败（提现失败）
                    LoggerUtil.log("配送员提现学校扣除失败:" + sender.getOpenId() + ":" + log.getAmount()+"-"+txId);
                    DisplayException.throwMessageWithEnum(ResponseViewEnums.TX_ERROR_USER_BELL_FAILED);
                }
                //学校减去资金，配送员可提现余额sender_money，配送员累计提现sender_all_tx
                Map senderTxSchoolChargeMap = new HashMap(map);
                senderTxSchoolChargeMap.put("amount", log.getAmount());
                int schoolSenderTXNum = schoolService.sendertx(senderTxSchoolChargeMap);
                if (schoolSenderTXNum != NumConstants.INT_NUM_1){
                    LoggerUtil.log("配送员提现学校减少金额失败:" + sender.getOpenId() + ":" + log.getAmount()+"-"+txId);
                    DisplayException.throwMessageWithEnum(ResponseViewEnums.TX_ERROR_SCHOOL_BELL_FAILED);
                }
                stringRedisTemplate.delete("SCHOOL_ID_" + school.getId());
                //修改提现状态
                // 审核成功(提现成功)
                log.setIsTx(1);
                boolean updateTXLogStatusTrue = txLogService.updateById(log);
                if (!updateTXLogStatusTrue){
                    LoggerUtil.log("配送员提现学校修改提现状态失败:" + sender.getOpenId() + ":" + log.getAmount()+"-"+txId);
                    DisplayException.throwMessageWithEnum(ResponseViewEnums.TX_ERROR_BACK_FAILED);
                }
                //都成功 再微信打钱，回滚跑异常
                try {
                    String payId = "tx" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                    int wxDoPayNum = WeChatPayUtil.transfers(
                            school.getWxAppId(),
                            school.getMchId(),
                            school.getWxPayId(),
                            school.getCertPath(),
                            payId,
                            "127.0.0.1",
                            log.getAmount(),
                            wxUser.getOpenId(),
                            log);
                    if (wxDoPayNum == 1) {
                        return 1;
                    }else {
                        LoggerUtil.log("配送员提现学校微信打钱失败:" + sender.getOpenId() + ":" + log.getAmount()+"-"+txId);
                        DisplayException.throwMessageWithEnum(ResponseViewEnums.TX_ERROR_WX_CHARGE_FAILED);
                    }
                } catch (Exception e) {
                    LoggerUtil.log(sender.getOpenId() + ":" + log.getAmount() + e.getMessage()+e);
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                }
                return 1;
            } else if (status == 2) {
                // 审核失败（提现失败）
                log.setIsTx(2);
                txLogService.updateById(log);
                return 2;
            }
        } else if (log.getType().equals("商家提现")){
            // 商家信息
            Shop shop = shopService.getById(log.getTxerId());
            School school = schoolService.findById(shop.getSchoolId());
            if (status == 1){
                Map<String,Object> map = new HashMap<>();
                map.put("amount",log.getAmount());
                map.put("shopId",shop.getId());
                if (shopService.shoptx(map) == 1) {
                    try {
                        String payId = "tx" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                        // 审核成功(提现成功)
                        log.setIsTx(1);
                        if (WeChatPayUtil.transfers(school.getWxAppId(), school.getMchId(), school.getWxPayId(),
                                school.getCertPath(), payId, "127.0.0.1", log.getAmount(), wxUser.getOpenId(),
                                log) == 1) {
                            txLogService.updateById(log);
                            Map schoolMap = new HashMap();
                            schoolMap.put("schoolId",shop.getSchoolId());
                            schoolMap.put("amount",log.getAmount());
                            if (schoolService.tx(schoolMap) == 0) {
                                LoggerUtil.log("配送员提现学校减少金额失败:" + log.getTxerId() + ":" + log.getAmount());
                            }
                            return 1;
                        }
                    } catch (Exception e) {
                        LoggerUtil.log(log.getTxerId() + ":" + log.getAmount() + e.getMessage());
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
