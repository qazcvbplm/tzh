package ops.school.service.impl;

import ops.school.api.dao.ShopCouponMapper;
import ops.school.api.entity.Coupon;
import ops.school.api.entity.ShopCoupon;
import ops.school.api.service.CouponService;
import ops.school.service.TShopCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TShopCouponServiceImpl implements TShopCouponService {

    @Autowired
    private ShopCouponMapper shopCouponMapper;
    @Autowired
    private CouponService couponService;

    @Override
    public int bindShopCoupon(String couponId, String shopIds) {
        Coupon coupon = couponService.getById(couponId);
        if (coupon != null && !shopIds.isEmpty()){
            String[] shopId = shopIds.split(",");
            for (int i = 0; i < shopId.length ; i++) {
                ShopCoupon shopCoupon = new ShopCoupon();
                shopCoupon.setCouponId(Long.valueOf(couponId));
                shopCoupon.setCreateId(-1L);
                shopCoupon.setShopId(Long.valueOf(shopId[i]));
                shopCouponMapper.insert(shopCoupon);
            }
            return 1;
        }
        return 0;
    }
}
