package ops.school.service;

import ops.school.api.dto.project.ShopCouponDTO;
import ops.school.api.entity.ShopCoupon;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TShopCouponService {


    /**
     * @date:   2019/7/25 20:52
     * @author: QinDaoFang
     * @version:version
     * @return: int
     * @param   shopCouponList
     * @Desc:   desc 绑定优惠券
     */
    int bindShopCoupon(List<ShopCoupon> shopCouponList);


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


    /**
     * @date:   2019/7/27 11:45
     * @author: QinDaoFang
     * @version:version
     * @return: java.lang.Integer
     * @param   deleteShopIds
     * @Desc:   desc 根据店铺id批量逻辑删除优惠卷
     */
    Integer batchDeleteSCByShopId(List<Long> deleteShopIds);

    /**
     * @date:   2019/7/27 11:52
     * @author: QinDaoFang
     * @version:version
     * @return: java.lang.Integer
     * @param   id
     * @Desc:   desc 根据优惠券id批量逻辑删除优惠卷
     */
    Integer batchDeleteSCByCouponId(Long id);
}
