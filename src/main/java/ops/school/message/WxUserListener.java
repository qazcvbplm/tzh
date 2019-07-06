package ops.school.message;

import com.alibaba.fastjson.JSON;
import ops.school.dto.redis.RedisMessage;
import ops.school.dto.redis.SenderAddMoneyDTO;
import ops.school.dto.redis.WxUserAddSourceDTO;
import ops.school.api.dao.LogsMapper;
import ops.school.api.dao.WxUserBellMapper;
import ops.school.api.entity.Logs;
import ops.school.api.entity.WxUser;
import ops.school.service.WxUserService;
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
    private WxUserBellMapper wxUserBellMapper;
    @Autowired
    private LogsMapper logsMapper;

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
        if (wxUserBellMapper.addSource(map2) == 0) {
            LoggerUtil.log("用户增加积分失败：" + wxUser.getOpenId() + "-" + wxUser.getPhone() + source);
        }
    }

    public void senderAddMoney(BigDecimal amount, String openId) {
        amount = amount.setScale(2, BigDecimal.ROUND_HALF_DOWN);
        WxUser wxUser = wxUserService.findById(openId);
        Map<String, Object> map = new HashMap<>();
        map.put("phone", wxUser.getOpenId() + "-" + wxUser.getPhone());
        map.put("amount", amount);
        if (wxUserBellMapper.charge(map) == 0) {
            logsMapper.insert(new Logs("配送员送达订单增加余额失败：" + wxUser.getOpenId() + "-" + wxUser.getPhone() + "," + amount.toString()));
        }
    }

}
