package com.serviceimple;

import com.alibaba.fastjson.JSON;
import com.service.SchoolService;
import com.util.SpringUtil;
import com.wx.towallet.WeChatPayUtil;
import ops.school.api.dao.LogsMapper;
import ops.school.api.dao.SchoolMapper;
import ops.school.api.dao.TxLogMapper;
import ops.school.api.entity.Logs;
import ops.school.api.entity.School;
import ops.school.api.entity.TxLog;
import ops.school.api.exception.YWException;
import ops.school.api.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SchoolServiceImple implements SchoolService{

	@Autowired
	private SchoolMapper schoolMapper;
	@Autowired
	private TxLogMapper txLogMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private LogsMapper logsMapper;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	@Override
	public void add(@Valid School school) throws Exception {
		if(schoolMapper.findByLoginName(school.getLoginName())==null){
			school.setLoginPassWord(Util.EnCode(school.getLoginPassWord()));
			school.setSort(System.currentTimeMillis());
			schoolMapper.insert(school);
		}else{
			throw new Exception("账号已存在请重新输入");
		}
	}

	@Override
	public List<School> find(School school) {
		school.setQuery("");
		switch (school.getQueryType()) {
		case "wxuser":
			school.setQuery("id,name");
			break;
		case "school":
			school.setQuery("*");
			break;
		default:
			return null;
		}
		return schoolMapper.find(school);
	}


	@Override
	public int update(School school) {
		int i = schoolMapper.updateByPrimaryKeySelective(school);
		if (i > 0 && SpringUtil.redisCache()) {
			stringRedisTemplate.delete("SCHOOL_ID_" + school.getId());
		}
		return i;
	}

	@Override
	public School findById(Integer schoolId) {
        if (SpringUtil.redisCache()) {
            String school = redisTemplate.opsForValue().get("SCHOOL_ID_" + schoolId);
            if (school != null) {
                return JSON.parseObject(school, School.class);
            } else {
                School rs = schoolMapper.selectByPrimaryKey(schoolId);
				redisTemplate.opsForValue().set("SCHOOL_ID_" + schoolId, JSON.toJSONString(rs));
                return rs;
            }
        }
		return schoolMapper.selectByPrimaryKey(schoolId);
	}

	@Override
	public School login(String loginName, String enCode) {
		School school=new School();
		school.setLoginName(loginName);
		school.setLoginPassWord(enCode);
		school=schoolMapper.login(school);
		return school;
	}

	@Transactional
	@Override
	public String tx(int schoolId, BigDecimal amount, String openId) {
        School school = findById(schoolId);
		Map<String,Object> map=new HashMap<>();
		map.put("schoolId", schoolId);
		map.put("amount", amount);
		if(schoolMapper.tx(map)==1){
			 String payId = "tx" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			try {
				TxLog log=new TxLog(schoolId, "代理提现", null, amount, "", schoolId, school.getAppId());
				if (WeChatPayUtil.transfers(school.getWxAppId(), school.getMchId(),
						school.getWxPayId(), school.getCertPath(), payId, "127.0.0.1", amount, openId,log) == 1)
				{
					txLogMapper.insert(log);
					return "提现成功";
				}
			} catch (Exception e) {
                logsMapper.insert(new Logs(schoolId + "," + openId + ":" + amount + e.getMessage()));
		        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			}
			throw new YWException("提现失败");
		}else{
			throw new YWException("余额不足");
		}
		
	}

	@Override
	public void chargeUse(Map<String, Object> map) {
      schoolMapper.chargeUse(map);		
	}

    @Override
    public int sendertx(Map<String, Object> map) {
        return schoolMapper.sendertx(map);
    }

    @Override
    public void charge(Map<String, Object> map2) {
        schoolMapper.charge(map2);
    }


}
