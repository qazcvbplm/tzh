package ops.school.message;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import ops.school.api.entity.WxUser;
import ops.school.api.service.LogsService;
import ops.school.api.service.WxUserBellService;
import ops.school.api.service.WxUserService;
import ops.school.api.util.LoggerUtil;
import ops.school.config.RabbitMQConfig;
import ops.school.message.dto.WxUserAddSourceDTO;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RabbitListener(queues = RabbitMQConfig.QUEUE_WX_USER_BELL)
public class WxUserListener {

    @Autowired
    private WxUserService wxUserService;
    @Autowired
    private WxUserBellService wxUserBellService;
    @Autowired
    private LogsService logsService;

    @RabbitHandler
    public void receiveMessage(String obj, Channel channel, Message message) {
        WxUserAddSourceDTO wxUserAddSourceDTO = JSON.parseObject(obj, WxUserAddSourceDTO.class);
        //增加积分
        WxUser wxUser = wxUserService.findById(wxUserAddSourceDTO.getOpenId());
        Map<String, Object> map2 = new HashMap<>();
        map2.put("phone", wxUser.getOpenId() + "-" + wxUser.getPhone());
        map2.put("source", wxUserAddSourceDTO.getSource());
        if (wxUserBellService.addSource(map2) == 0) {
            LoggerUtil.log("用户增加积分失败：" + wxUser.getOpenId() + "-" + wxUser.getPhone() + wxUserAddSourceDTO.getSource());
        }

    }


}
