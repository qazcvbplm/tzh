package ops.school.service;

import ops.school.api.entity.WxUserBell;
import org.springframework.transaction.annotation.Transactional;

public interface TWxUserService {
    int addSource(String openId, Integer source);

    WxUserBell getbell(String openid);

    Object charge(String openId, int chargeId);

    @Transactional
    void chargeSuccess(String orderId, String openId, String attach);

    int decryptPhone(String decryptData,String sessionKey, String ivData, String openid);

    /**
     * @date:   2019/9/3 18:20
     * @author: QinDaoFang
     * @version:version
     * @return: void
     * @param
     * @Desc:   desc
     */
    void updateBackDataFromOldToNewByPhone();

    /**
     * @date:   2019/9/3 20:57
     * @author: QinDaoFang
     * @version:version
     * @return: void
     * @param
     * @Desc:   desc
     */
    void updateDataSourceFromOldToNewByPhone();
}
