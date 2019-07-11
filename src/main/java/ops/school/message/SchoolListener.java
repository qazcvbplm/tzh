package ops.school.message;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import ops.school.api.dao.MqttMapper;
import ops.school.api.dto.redis.RedisMessage;
import ops.school.api.dto.redis.SchoolAddMoneyDTO;
import ops.school.api.entity.Mqtt;
import ops.school.api.entity.School;
import ops.school.api.service.SchoolService;
import ops.school.api.service.SenderService;
import ops.school.api.service.TxLogService;
import ops.school.api.util.LoggerUtil;
import ops.school.api.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
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

	public void receiveMessage(String message) {
		List<Mqtt> mqtt = mqttMapper.selectList(new QueryWrapper<>());
		Mqtt ok = null;
		RedisMessage redisMessage = JSON.parseObject(message, RedisMessage.class);
		if (redisMessage.getType().equals("addmoney")) {
			SchoolAddMoneyDTO schoolAddMoneyDTO = JSON.parseObject(message, SchoolAddMoneyDTO.class);
            School school = schoolService.findById(schoolAddMoneyDTO.getSchoolId());
			BigDecimal total = schoolAddMoneyDTO.getSelfGet();
			BigDecimal senderPrice = schoolAddMoneyDTO.getSenderGet();
			BigDecimal cc = total.add(senderPrice).multiply(school.getRate());
			Map<String, Object> map = new HashMap();
			map.put("schoolId", schoolAddMoneyDTO.getSchoolId());
            /////
			map.put("sendMoney", senderPrice);
            if (schoolService.endOrder(map) == 0) {
				LoggerUtil.log("学校增加余额失败：" + message);
			}
			cache.amountadd(schoolAddMoneyDTO.getSchoolId(), total.add(senderPrice));
		}
	}



}
