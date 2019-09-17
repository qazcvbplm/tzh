package ops.school.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import ops.school.api.dto.project.CouponDTO;
import ops.school.api.entity.Coupon;
import ops.school.api.util.ResponseObject;

import java.util.List;
import java.util.Map;

public interface TCouponService {

    List<Coupon> findByIndex(Long schoolId, Integer yesShowIndex,Long userId);

    Integer count(Long schoolId, Integer couponType);

    /**
     * @author: QinDaoFang
     * @date:   2019/7/25 16:03 
     * @desc:   
     */
    ResponseObject findCoupons(Long schoolId, Integer couponType, int page, int size);

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

    /**
     * @date:   2019/7/26 19:35
     * @author: QinDaoFang
     * @version:version
     * @return: ops.school.api.util.ResponseObject
     * @param   coupon
     * @Desc:   desc
     */
    ResponseObject updateOneById(Coupon coupon);

    /**
     * @date:   2019/7/27 11:57
     * @author: QinDaoFang
     * @version:version
     * @return: ops.school.api.util.ResponseObject
     * @param   coupon
     * @Desc:   desc 根据优惠券id删除优惠券
     */
    ResponseObject deleteCouponAndShopByCId(Coupon coupon);

    /**
     * @date:   2019/8/7 15:11
     * @author: QinDaoFang
     * @version:version
     * @return: java.lang.Integer
     * @param
     * @Desc:   desc
     */
    Integer countInvalidCoupon();

    /**
     * @date:   2019/8/7 15:16
     * @author: QinDaoFang
     * @version:version
     * @return: java.util.List<ops.school.api.entity.Coupon>
     * @param
     * @Desc:   desc
     */
    List<Coupon> limitFindInvalidUserCoupon();

    /**
     * @date:   2019/8/7 15:18
     * @author: QinDaoFang
     * @version:version
     * @return: java.lang.Integer
     * @param   idList
     * @Desc:   desc
     */
    Integer batchUpdateToUnInvalidByIds(List<Long> idList);
}
