package ops.school.api.serviceimple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ops.school.api.dao.WxUserBellMapper;
import ops.school.api.entity.WxUserBell;
import ops.school.api.service.WxUserBellService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class WxUserBellServiceImpl extends ServiceImpl<WxUserBellMapper, WxUserBell> implements WxUserBellService {

    @Autowired
    private WxUserBellMapper wxUserBellMapper;

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
}
