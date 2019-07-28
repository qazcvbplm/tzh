package ops.school.message;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rabbitmq.client.Channel;
import ops.school.api.dao.MqttMapper;
import ops.school.api.entity.Mqtt;
import ops.school.api.entity.School;
import ops.school.api.service.SchoolService;
import ops.school.api.service.SenderService;
import ops.school.api.service.TxLogService;
import ops.school.api.util.LoggerUtil;
import ops.school.api.util.RedisUtil;
import ops.school.config.RabbitMQConfig;
import ops.school.message.dto.BaseMessage;
import ops.school.message.dto.SchoolAddMoneyDTO;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RabbitListener(queues = RabbitMQConfig.QUEUE_SCHOOL_BELL)
public class SchoolListener {
	@Autowired
    private SchoolService schoolService;
	@Autowired
	private MqttMapper mqttMapper;
	@Autowired
	private RedisUtil cache;
	@Autowired
    private TxLogService txLogService;
	@Autowired
    private SenderService senderService;

    @RabbitHandler
    public void receiveMessage(String obj, Channel channel, Message message) {
		List<Mqtt> mqtt = mqttMapper.selectList(new QueryWrapper<>());
		Mqtt ok = null;
        BaseMessage redisMessage = JSON.parseObject(obj, BaseMessage.class);
		if (redisMessage.getType().equals("addmoney")) {
            SchoolAddMoneyDTO schoolAddMoneyDTO = JSON.parseObject(obj, SchoolAddMoneyDTO.class);
            School school = schoolService.findById(schoolAddMoneyDTO.getSchoolId());
			BigDecimal total = schoolAddMoneyDTO.getSelfGet();
			BigDecimal senderPrice = schoolAddMoneyDTO.getSenderGet();
			BigDecimal cc = total.add(senderPrice).multiply(school.getRate());
			Map<String, Object> map = new HashMap();
			map.put("schoolId", schoolAddMoneyDTO.getSchoolId());
			map.put("sendMoney", senderPrice);
            if (schoolService.endOrder(map) == 0) {
                LoggerUtil.log("学校增加余额失败：" + obj);
			}
			cache.amountadd(schoolAddMoneyDTO.getSchoolId(), total.add(senderPrice));
		}
	}



}
