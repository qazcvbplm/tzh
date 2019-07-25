package ops.school.service.impl;

import ops.school.api.dao.WxUserCouponMapper;
import ops.school.api.entity.WxUserCoupon;
import ops.school.api.service.WxUserCouponService;
import ops.school.service.TWxUserCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TWxUserCouponServiceImpl implements TWxUserCouponService {

    @Autowired
    private WxUserCouponMapper wxUserCouponMapper;

    @Override
    public List<WxUserCoupon> findInvalidUserCoupon() {
        return wxUserCouponMapper.findInvalidUserCoupon();
    }

    @Override
    public int updateIsInvalid(WxUserCoupon wxUserCoupon) {
        if (wxUserCoupon == null || wxUserCoupon.getId() == null || wxUserCoupon.getIsInvalid() == null){
            return -1;
        }
        int rs = wxUserCouponMapper.update(wxUserCoupon);
        return rs;
    }

    @Override
    public List<WxUserCoupon> findUserCoupon(Long wxUserId, Long shopId) {
        Map<String,Object> map = new HashMap<>();
        map.put("wxUserId",wxUserId);
        map.put("shopId",shopId);
        return wxUserCouponMapper.findUserCoupon(map);
    }

    @Override
    public List<Map<String,Object>> findAllUserCoupons(Long wxUserId) {
        return wxUserCouponMapper.findAllUserCoupons(wxUserId);
    }
}
