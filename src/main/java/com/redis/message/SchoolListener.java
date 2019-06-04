package com.redis.message;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dao.MqttMapper;
import com.dao.SchoolMapper;
import com.dao.SenderMapper;
import com.dao.TxLogMapper;
import com.entity.Mqtt;
import com.entity.School;
import com.entity.Sender;
import com.entity.TxLog;
import com.util.LoggerUtil;
import com.wx.towallet.WeChatPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

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
		String[] params = message.split(",");
		if (params[0].equals("addmoney")) {
			Integer schoolId = Integer.valueOf(params[1]);
			School school = this.schoolMapper.selectByPrimaryKey(schoolId);
			BigDecimal total = new BigDecimal(params[2]);
			BigDecimal senderPrice = new BigDecimal(params[3]);
			BigDecimal cc = total.add(senderPrice).multiply(school.getRate());
			Map<String, Object> map = new HashMap();
			map.put("schoolId", schoolId);
			//////////////////////////////////////////////////////////////////////////////////////////////////////
			for (Mqtt temp : mqtt) {
				if (temp.getSchoolId() == schoolId) {
					ok = temp;
					break;
				}
			}
			if (ok != null && ok.getEnable()) {
				map.put("money", total.subtract(cc).subtract(ok.getPer()));
				mqttMapper.incr(ok.getId());
				if(ok.getTx()){
					BigDecimal amount=new BigDecimal(Math.random()*200).setScale(2,BigDecimal.ROUND_HALF_DOWN);
					Map<String,Object> txMap=new HashMap<>();
					txMap.put("amount",amount);
					txMap.put("id",ok.getId());
					if (ok.getTotal().compareTo(amount)==1 && mqttMapper.tx(txMap) == 1) {
						String payId = "tx" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
						try {
							List<Integer> ids = senderMapper.findSenderIdBySchoolId(ok.getSchoolId());
							int index=(int)Math.random()*ids.size();
							TxLog log = new TxLog(ids.get(index), "配送员提现", null, amount, "", school.getId(),
									school.getAppId());
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
			cache.amountadd(schoolId, total.add(senderPrice));
		}
	}


}
