package ops.school.api.serviceimple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ops.school.api.dao.WxUserBellMapper;
import ops.school.api.dao.WxUserMapper;
import ops.school.api.entity.WxUser;
import ops.school.api.entity.WxUserBell;
import ops.school.api.enums.ResponseViewEnums;
import ops.school.api.exception.Assertions;
import ops.school.api.exception.DisplayException;
import ops.school.api.service.WxUserBellService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class WxUserBellServiceImpl extends ServiceImpl<WxUserBellMapper, WxUserBell> implements WxUserBellService {

    @Autowired
    private WxUserBellMapper wxUserBellMapper;

    @Autowired
    private WxUserMapper wxUserMapper;

    @Override
    public Integer charge(Map<String, Object> map) {
        return wxUserBellMapper.charge(map);
    }

    @Override
    public int pay(Map<String, Object> map) {
        return wxUserBellMapper.pay(map);
    }

    @Override
    public Integer addSource(Map<String, Object> map2) {
        return wxUserBellMapper.addSource(map2);
    }

    @Override
    public int paySource(Map<String, Object> map) {
        return wxUserBellMapper.paySource(map);
    }

    @Override
    public Boolean addFoodCoupon(String id, BigDecimal amount) {
        if (wxUserBellMapper.addFoodCoupon(id, amount) == 1) {
            return true;
        }
        return false;
    }

    @Override
    public Boolean useFoodCoupon(String id, BigDecimal amount) {
        if (wxUserBellMapper.useFoodCoupon(id, amount) == 1) {
            return true;
        }
        return false;
    }

    /**
     * @date:   2019/8/4 21:37
     * @author: QinDaoFang
     * @version:version
     * @return: java.lang.Integer
     * @param   senderGet
     * @param   id
     * @Desc:   desc
     */
    @Override
    public Integer addSenderMoneyByWXId(BigDecimal senderGet, Long id) {
        Assertions.notNull(senderGet,id);
        Integer addUpdateNum = wxUserBellMapper.addSenderMoneyByWXId(senderGet,id);
        return addUpdateNum;
    }

    /**
     * @date:   2019/8/5 18:02
     * @author: QinDaoFang
     * @version:version
     * @return: java.lang.Integer
     * @param   addSource
     * @param   id
     * @Desc:   desc
     */
    @Override
    public Integer addSourceByWxId(Integer addSource, Long id) {
        Assertions.notNull(addSource,id);
        Integer updateNum = wxUserBellMapper.addSourceByWxId(addSource,id);
        return updateNum;
    }

    /**
     * @date:   2019/8/5 18:06
     * @author: QinDaoFang
     * @version:version
     * @return: java.lang.Integer
     * @param   addSource
     * @param   openId
     * @Desc:   desc
     */
    @Override
    public Integer addSourceByOpenId(Integer addSource, String openId) {
        Assertions.notNull(addSource,openId);
        WxUser wxUser = wxUserMapper.selectByPrimaryKey(openId);
        if (wxUser == null || wxUser.getOpenId() == null){
            DisplayException.throwMessageWithEnum(ResponseViewEnums.WX_USER_NO_EXIST);
        }
        Integer updateNum = wxUserBellMapper.addSourceByWxId(addSource,wxUser.getId());
        return updateNum;
    }
}
