package ops.school.service.impl;

import ops.school.api.entity.Logs;
import ops.school.api.entity.School;
import ops.school.api.entity.TxLog;
import ops.school.api.exception.YWException;
import ops.school.api.service.LogsService;
import ops.school.api.service.SchoolService;
import ops.school.api.service.TxLogService;
import ops.school.api.wx.towallet.WeChatPayUtil;
import ops.school.service.TSchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class TSchoolServiceImpl implements TSchoolService {

    @Autowired
    private SchoolService schoolService;
    @Autowired
    private TxLogService txLogService;
    @Autowired
    private LogsService logsService;

    @Transactional
    @Override
    public String tx(int schoolId, BigDecimal amount, String openId) {
        School school = schoolService.findById(schoolId);
        Map<String, Object> map = new HashMap<>();
        map.put("schoolId", schoolId);
        map.put("amount", amount);
        if (schoolService.tx(map) == 1) {
            String payId = "tx" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            try {
                TxLog log = new TxLog(schoolId, "代理提现", null, amount, "", schoolId, school.getAppId());
                if (WeChatPayUtil.transfers(school.getWxAppId(), school.getMchId(),
                        school.getWxPayId(), school.getCertPath(), payId, "127.0.0.1", amount, openId, log) == 1) {
                    txLogService.save(log);
                    return "提现成功";
                }
            } catch (Exception e) {
                logsService.save(new Logs(schoolId + "," + openId + ":" + amount + e.getMessage()));
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
            throw new YWException("提现失败");
        } else {
            throw new YWException("余额不足");
        }

    }
}
