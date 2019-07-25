package ops.school.service;

import ops.school.api.entity.WxUserCoupon;

import java.util.List;
import java.util.Map;

public interface TWxUserCouponService {

    /**
     * @date:
     * @author: Lee
     * @version:version
     * @return: java.util.List
     * @param
     * @Desc:   desc 查询所有用户的未使用的失效优惠券
     */
    List<WxUserCoupon> findInvalidUserCoupon();

    /**
     * @date:
     * @author: Lee
     * @version:version
     * @return: int
     * @param
     * @Desc:   desc 修改用户优惠券is_invalid状态
     */
    int updateIsInvalid(WxUserCoupon userCoupon);

    /**
     * @date:
     * @author: Lee
     * @version:version
     * @return: java.util.List
     * @param wxUserId 用户id shopId 店铺id
     * @Desc:   desc 根据店铺查询用户所有可用的优惠券
     */
    List<WxUserCoupon> findUserCoupon(Long wxUserId, Long shopId);

    /**
     * @date:
     * @author: Lee
     * @version:version
     * @return: java.util.Map
     * @param   wxUserId 用户id
     * @Desc:   desc 根据用户id查询用户所有优惠券
     */
    List<Map<String,Object>> findAllUserCoupons(Long wxUserId);
}
