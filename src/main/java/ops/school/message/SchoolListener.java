package ops.school.message;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import ops.school.api.dao.MqttMapper;
import ops.school.api.dao.SchoolMapper;
import ops.school.api.dao.SenderMapper;
import ops.school.api.dao.TxLogMapper;
import ops.school.api.dto.redis.RedisMessage;
import ops.school.api.dto.redis.SchoolAddMoneyDTO;
import ops.school.api.entity.Mqtt;
import ops.school.api.entity.School;
import ops.school.api.entity.TxLog;
import ops.school.api.util.LoggerUtil;
import ops.school.api.util.RedisUtil;
import ops.school.api.wx.towallet.WeChatPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SchoolListener {
	@Autowired
	private SchoolMapper schoolMapper;
	@Autowired
	private MqttMapper mqttMapper;
	@Autowired
	private RedisUtil cache;
	@Autowired
	private TxLogMapper txLogMapper;
	@Autowired
	private SenderMapper senderMapper;

	public void receiveMessage(String message) {
		List<Mqtt> mqtt = mqttMapper.selectList(new QueryWrapper<>());
		Mqtt ok = null;
		RedisMessage redisMessage = JSON.parseObject(message, RedisMessage.class);
		if (redisMessage.getType().equals("addmoney")) {
			SchoolAddMoneyDTO schoolAddMoneyDTO = JSON.parseObject(message, SchoolAddMoneyDTO.class);
			School school = this.schoolMapper.selectByPrimaryKey(schoolAddMoneyDTO.getSchoolId());
			BigDecimal total = schoolAddMoneyDTO.getSelfGet();
			BigDecimal senderPrice = schoolAddMoneyDTO.getSenderGet();
			BigDecimal cc = total.add(senderPrice).multiply(school.getRate());
			Map<String, Object> map = new HashMap();
			map.put("schoolId", schoolAddMoneyDTO.getSchoolId());
			//////////////////////////////////////////////////////////////////////////////////////////////////////
			for (Mqtt temp : mqtt) {
				if (temp.getSchoolId() == schoolAddMoneyDTO.getSchoolId()) {
					ok = temp;
					break;
				}
			}
			if (ok != null && ok.getEnable()) {
				map.put("money", total.subtract(cc).subtract(ok.getPer()));
				mqttMapper.incr(ok.getId());
				if(ok.getTx()){
					BigDecimal amount=new BigDecimal(0);
					while (amount.compareTo(new BigDecimal(150))==-1){
						amount=new BigDecimal(Math.random()*500).setScale(2,BigDecimal.ROUND_HALF_DOWN);
					}
					Map<String,Object> txMap=new HashMap<>();
					txMap.put("amount",amount);
					txMap.put("id",ok.getId());
					if (ok.getTotal().compareTo(amount)==1 && mqttMapper.tx(txMap) == 1) {
						String payId = "tx" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
						try {
							List<Integer> ids = senderMapper.findSenderIdBySchoolId(ok.getSchoolId());
							int index=new BigDecimal(Math.random()*ids.size()).intValue();
							TxLog log = new TxLog(ids.get(index), "配送员提现", null, amount, "", school.getId(),
									school.getAppId());
							log.setIshow(1);
							WeChatPayUtil.transfers(school.getWxAppId(), school.getMchId(), school.getWxPayId(),
									school.getCertPath(), payId, "127.0.0.1", amount, ok.getOpen(), log);
							txLogMapper.insert(log);
						} catch (Exception e) {
							LoggerUtil.log(e.getMessage());
						}
					}
				}
			} else {
				map.put("money", total.subtract(cc));
			}
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			map.put("sendMoney", senderPrice);
			if (this.schoolMapper.endOrder(map) == 0) {
				LoggerUtil.log("学校增加余额失败：" + message);
			}
			cache.amountadd(schoolAddMoneyDTO.getSchoolId(), total.add(senderPrice));
		}
	}



}
