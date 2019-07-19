package ops.school.service;

import ops.school.api.dto.project.CouponDTO;
import ops.school.api.entity.Coupon;
import ops.school.api.util.ResponseObject;

import java.util.List;
import java.util.Map;

public interface TCouponService {

    List<Coupon> findByIndex(Long schoolId, Integer yesShowIndex);

    Integer count(Long schoolId, Integer couponType);

    List<Coupon> findCoupons(Long schoolId, Integer couponType, int page, int size);

    int insert(Coupon coupon);

    /**
     * @date:   2019/7/18 14:16
     * @author: QinDaoFang
     * @version:version
     * @return: ops.school.api.util.ResponseObject
     * @param   couponDTO 包括schoolid，ids，createid
     * @Desc:   desc 首页优惠券绑定
     */
    ResponseObject bindHomeCouponsBySIdAndIds(CouponDTO couponDTO);

    /**
     * @date:   2019/7/18 17:40
     * @author: Lee
     * @version:version
     * @return: java.util.List
     * @param
     * @Desc:   desc 查询所有未失效和未删除的优惠券中已失效的优惠券
     */
    List<Coupon> findInvalidCoupon();

    /**
     * @date:   2019/7/18 17:33
     * @author: QinDaoFang
     * @version:version
     * @return: ops.school.api.util.ResponseObject
     * @param   map
     * @Desc:   desc 根据用户id，学校id，店铺id，优惠券id，让用户获取优惠券
     */
    ResponseObject userGetCouponByIdMap(Map map);
}
