package ops.school.message;

import com.alibaba.fastjson.JSON;
import ops.school.api.entity.Logs;
import ops.school.api.entity.WxUser;
import ops.school.api.service.LogsService;
import ops.school.api.service.WxUserBellService;
import ops.school.api.service.WxUserService;
import ops.school.api.util.LoggerUtil;
import ops.school.dto.message.RedisMessage;
import ops.school.dto.message.SenderAddMoneyDTO;
import ops.school.dto.message.WxUserAddSourceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class WxUserListener {

    @Autowired
    private WxUserService wxUserService;
    @Autowired
    private WxUserBellService wxUserBellService;
    @Autowired
    private LogsService logsService;

    public void receiveMessage(String message) {
        RedisMessage redisMessage = JSON.parseObject(message, RedisMessage.class);
        if (redisMessage.getType().equals("addsource")) {
            WxUserAddSourceDTO wxUserAddSourceDTO = JSON.parseObject(message, WxUserAddSourceDTO.class);
            addsource(wxUserAddSourceDTO.getOpenId(), wxUserAddSourceDTO.getSource());
        }
        if (redisMessage.getType().equals("addmoney")) {
            SenderAddMoneyDTO senderAddMoneyDTO = JSON.parseObject(message, SenderAddMoneyDTO.class);
            senderAddMoney(senderAddMoneyDTO.getAmount(), senderAddMoneyDTO.getOpenId());
        }
    }

    public void addsource(String openid, int source) {
        //增加积分
        WxUser wxUser = wxUserService.findById(openid);
        Map<String, Object> map2 = new HashMap<>();
        map2.put("phone", wxUser.getOpenId() + "-" + wxUser.getPhone());
        map2.put("source", source);
        if (wxUserBellService.addSource(map2) == 0) {
            LoggerUtil.log("用户增加积分失败：" + wxUser.getOpenId() + "-" + wxUser.getPhone() + source);
        }
    }

    public void senderAddMoney(BigDecimal amount, String openId) {
        amount = amount.setScale(2, BigDecimal.ROUND_HALF_DOWN);
        WxUser wxUser = wxUserService.findById(openId);
        Map<String, Object> map = new HashMap<>();
        map.put("phone", wxUser.getOpenId() + "-" + wxUser.getPhone());
        map.put("amount", amount);
        if (wxUserBellService.charge(map) == 0) {
            logsService.save(new Logs("配送员送达订单增加余额失败：" + wxUser.getOpenId() + "-" + wxUser.getPhone() + "," + amount.toString()));
        }
    }

}
