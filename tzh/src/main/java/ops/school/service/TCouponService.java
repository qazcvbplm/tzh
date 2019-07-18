package ops.school.service;

import ops.school.api.entity.Coupon;

import java.util.List;

public interface TCouponService {

    List<Coupon> findByIndex(Long schoolId, Integer couponType, Integer yesShowIndex);

    Integer count(Long schoolId, Integer couponType);

    List<Coupon> findCoupons(Long schoolId, Integer couponType, int page, int size);

    int insert(Coupon coupon);
}
