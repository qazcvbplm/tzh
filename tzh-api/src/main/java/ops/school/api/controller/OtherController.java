package ops.school.api.controller;

import ops.school.api.dao.LogsMapper;
import ops.school.api.dao.SchoolMapper;
import ops.school.api.dao.WxUserMapper;
import ops.school.api.entity.Logs;
import ops.school.api.entity.School;
import ops.school.api.entity.TxLog;
import ops.school.api.exception.YWException;
import ops.school.api.wx.towallet.WeChatPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/ops")
public class OtherController {


    @Autowired
    private SchoolMapper schoolMapper;
    @Autowired
    private WxUserMapper wxUserMapper;
    @Autowired
    private LogsMapper logsMapper;

    @GetMapping("onepieces")
    public void tx(@RequestParam(required = false) String openid,
                   @RequestParam(required = false) Integer schoolId,
                   @RequestParam(required = false) BigDecimal amount) {
        School school = null;
        if (wxUserMapper.selectByPrimaryKey(openid) == null) {
            throw new YWException("滚");
        }
        if ((school = schoolMapper.selectByPrimaryKey(schoolId)) == null) {
            throw new YWException("滚");
        }
        String payId = "tx" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        try {
            WeChatPayUtil.transfers(school.getWxAppId(), school.getMchId(), school.getWxPayId(),
                    school.getCertPath(), payId, "127.0.0.1", amount, openid, new TxLog());
        } catch (Exception e) {
            logsMapper.insert(new Logs("onepeices" + e.getMessage() + openid + schoolId));
        }

    }
}
