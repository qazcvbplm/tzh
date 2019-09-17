package ops.school.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import ops.school.api.dto.wxgzh.Message;
import ops.school.api.entity.WxUser;

import java.util.List;

public interface WxUserService extends IService<WxUser> {


    WxUser update(WxUser wxUser);

    List<WxUser> find(WxUser wxUser);

    void sendWXGZHM(String phone, Message message);

    WxUser findById(String openId);

    int countBySchoolId(int schoolId);

    WxUser login(String openid, Integer schoolId, Integer appId, String client);

    List<WxUser> findByPhoneGZH(String phone);

    WxUser findGzh(String phone);

    WxUser findByschoolAndPhone(WxUser query);

    /**
     * @date:   2019/7/24 15:54
     * @author: QinDaoFang
     * @version:version
     * @return: ops.school.api.entity.WxUser
     * @param   openId
     * @param   wxUserPhone
     * @Desc:   desc 根据用户的openid和phone查找用户和用户余额
     */
    WxUser findUserAndBellOrCache(String openId, String wxUserPhone);
}
