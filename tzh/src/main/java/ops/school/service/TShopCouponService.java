package ops.school.service;

import ops.school.api.dto.project.ShopCouponDTO;
import ops.school.api.entity.ShopCoupon;

public interface TShopCouponService {

    int bindShopCoupon(String couponId, String shopIds);

    /**
     * @date:   2019/7/18 17:55
     * @author: QinDaoFang
     * @version:version
     * @return: ops.school.api.entity.ShopCoupon
     * @param   schoolId
     * @param   shopId
     * @param   couponId
     * @Desc:   desc 根据id查询一个店铺有效的优惠券
     */
    ShopCoupon findPoweredCouponBySIdAndSId(Long schoolId, Long shopId, Long couponId);
}
