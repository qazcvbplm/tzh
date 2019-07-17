package ops.school.service.impl;

import ops.school.api.dao.CouponMapper;
import ops.school.api.entity.Coupon;
import ops.school.service.TCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TCouponServiceImpl implements TCouponService {

    @Autowired
    private CouponMapper couponMapper;

    @Override
    public List<Coupon> findByIndex(Long schoolId, Integer couponType) {
        Map<String,Object> map = new HashMap<>();
        map.put("schoolId",schoolId);
        map.put("couponType",couponType);
        return couponMapper.findByIndex(map);
    }
}
