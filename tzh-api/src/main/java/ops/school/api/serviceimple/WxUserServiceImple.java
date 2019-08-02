package ops.school.api.serviceimple;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ops.school.api.dao.*;
import ops.school.api.dto.wxgzh.Message;
import ops.school.api.entity.WxUser;
import ops.school.api.entity.WxUserBell;
import ops.school.api.enums.ResponseViewEnums;
import ops.school.api.exception.Assertions;
import ops.school.api.exception.DisplayException;
import ops.school.api.exception.YWException;
import ops.school.api.service.SchoolService;
import ops.school.api.service.WxUserService;
import ops.school.api.util.SpringUtil;
import ops.school.api.wxutil.WxGUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WxUserServiceImple extends ServiceImpl<WxUserMapper, WxUser> implements WxUserService {

    @Autowired
    private WxUserMapper wxUserMapper;
    @Autowired
    private WxUserBellMapper wxUserBellMapper;
    @Autowired
    private ChargeMapper chargeMapper;
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private ChargeLogMapper chargeLogMapper;
    @Autowired
    private LogsMapper logsMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public WxUser login(String openid, Integer schoolId, Integer appId, String client) {
        WxUser wxUser = findById(openid);
        if (wxUser == null) {
            wxUser = new WxUser(openid, client);
            wxUser.setAppId(appId);
            wxUser.setSchoolId(schoolId);
            this.save(wxUser);
        }
        WxUser result = findById(openid);
        if (result == null || result.getId() == null){
            DisplayException.throwMessageWithEnum(ResponseViewEnums.WX_USER_FAILED_TO_WX);
        }
        return result;
    }

    //todo 加了微信id的操作
    @Transactional
    @Override
    public WxUser update(WxUser wxUser) {
        Assertions.notNull(wxUser,wxUser.getId(), ResponseViewEnums.WX_USER_BELL_NEED_ID);
        if (wxUser.getPhone() != null) {
            if (wxUserBellMapper.findByPhone(wxUser.getOpenId() + "-" + wxUser.getPhone()) == 0) {
                wxUserBellMapper.insert(new WxUserBell(wxUser.getOpenId() + "-" + wxUser.getPhone(),wxUser.getId()));
            } else {
                throw new YWException("手机号码已被注册");
            }
        }
        wxUser.setId(null);
        boolean rs = this.updateById(wxUser);
        if (SpringUtil.redisCache() && rs) {
            stringRedisTemplate.boundHashOps("WX_USER_LIST").delete(wxUser.getOpenId());
        }
        return wxUserMapper.selectByPrimaryKey(wxUser.getOpenId());
    }

    @Override
    public List<WxUser> find(WxUser wxUser) {
        if (wxUser.getQueryType().equals("ops")) {
            wxUser.setSchoolId(Integer.valueOf(wxUser.getQuery()));
        }
        return wxUserMapper.find(wxUser);
    }






    @Override
    public void sendWXGZHM(String phone, Message message) {
        WxUser wxGUser = findGzh(phone);
        if (wxGUser != null) {
            message.setToUser(wxGUser.getOpenId());
            WxGUtil.snedM(message.toJson());
        }
    }



    @Override
    public WxUser findById(String openId) {
        if (SpringUtil.redisCache()) {
            String rs = stringRedisTemplate.opsForValue().get("WX_USER_OPENID_" + openId);
            if (rs != null) {
                return JSON.parseObject(rs, WxUser.class);
            } else {
                WxUser wxUser = wxUserMapper.selectByPrimaryKey(openId);
                stringRedisTemplate.boundHashOps("WX_USER_LIST").put(openId, JSON.toJSONString(wxUser));
                return wxUser;
            }
        }
        return wxUserMapper.selectById(openId);
    }



    @Override
    public int countBySchoolId(int schoolId) {
        return wxUserMapper.countBySchoolId(schoolId);
    }

    @Override
    public List<WxUser> findByPhoneGZH(String query) {
        return wxUserMapper.findByPhoneGZH(query);
    }


    @Override
    public WxUser findGzh(String phone) {
        List<WxUser> users = wxUserMapper.findGzh(phone);
        return users.size() > 0 ? users.get(0) : null;
    }

    @Override
    public WxUser findByschoolAndPhone(WxUser query) {
        return wxUserMapper.findByschoolAndPhone(query);
    }

    /**
     * @date:   2019/7/24 15:55
     * @author: QinDaoFang
     * @version:version
     * @return: ops.school.api.entity.WxUser
     * @param   openId
     * @param   wxUserPhone
     * @Desc:   desc 根据用户的openid和phone查找用户和用户余额
     */
    @Override
    public WxUser findUserAndBellOrCache(String openId, String wxUserPhone) {
        Assertions.hasText(openId);
        Assertions.hasText(wxUserPhone);
        if (SpringUtil.redisCache()) {
            String rs = stringRedisTemplate.opsForValue().get("WX_USER_OPENID_" + openId);
            if (rs != null) {
                return JSON.parseObject(rs, WxUser.class);
            } else {
                WxUser wxUser = wxUserMapper.selectByPrimaryKey(openId);
                stringRedisTemplate.boundHashOps("WX_USER_LIST").put(openId, JSON.toJSONString(wxUser));
                return wxUser;
            }
        }
        WxUser wxUser = wxUserMapper.findUserAndBell(openId,wxUserPhone);
        return wxUserMapper.selectById(openId);
    }
}
