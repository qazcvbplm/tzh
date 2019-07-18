package ops.school.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import ops.school.api.dao.ShopCouponMapper;
import ops.school.api.dto.project.ShopCouponDTO;
import ops.school.api.entity.Coupon;
import ops.school.api.entity.ShopCoupon;
import ops.school.api.enums.ResponseViewEnums;
import ops.school.api.exception.Assertions;
import ops.school.api.service.CouponService;
import ops.school.constants.NumContants;
import ops.school.service.TShopCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

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
    @Override
    public ShopCoupon findPoweredCouponBySIdAndSId(Long schoolId, Long shopId, Long couponId) {
        Assertions.notNull(schoolId,shopId,couponId);
        //先查询优惠券是否有效且存在
        QueryWrapper<Coupon> queryWrapperCoupon = new QueryWrapper<>();
        queryWrapperCoupon
                .eq("school_id",schoolId)
                .eq("id",couponId)
                .eq("is_delete", NumContants.DB_TABLE_IS_DELETE_NO)
                .eq("is_invalid",NumContants.DB_TABLE_IS_INVALID_YES)
                .lt("send_begin_time",new Date(System.currentTimeMillis()))
                .ge("send_end_time",new Date(System.currentTimeMillis()));
        Coupon coupon = couponService.getOne(queryWrapperCoupon);
        Assertions.notNull(coupon, ResponseViewEnums.COUPON_HOME_NUM_ERROR);
        //查询是否绑定店铺
        QueryWrapper<ShopCoupon> queryWrapperShopCoupon = new QueryWrapper<>();
        queryWrapperShopCoupon
                .eq("shop_id",shopId)
                .eq("coupon_id",couponId)
                .eq("is_delete", NumContants.DB_TABLE_IS_DELETE_NO);
        ShopCoupon shopCoupon = shopCouponMapper.selectOne(queryWrapperShopCoupon);
        Assertions.notNull(shopCoupon,ResponseViewEnums.COUPON_HOME_NUM_ERROR);
        //存在并绑定店铺则有效，返回
        shopCoupon.setCoupon(coupon);
        return shopCoupon;
    }
}
