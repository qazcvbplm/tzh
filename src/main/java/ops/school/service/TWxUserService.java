package ops.school.service;

import ops.school.api.entity.WxUserBell;

public interface TWxUserService {
    int addSource(String openId, Integer source);

    WxUserBell getbell(String openid);

    Object charge(String openId, int chargeId);
}
