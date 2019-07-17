package ops.school.service;

import ops.school.api.entity.Coupon;

import java.util.List;

public interface TCouponService {

    List<Coupon> findByIndex(Long schoolId, Integer couponType);
}
