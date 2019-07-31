package ops.school.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import ops.school.api.dao.ShopCouponMapper;
import ops.school.api.entity.Coupon;
import ops.school.api.entity.ShopCoupon;
import ops.school.api.entity.WxUserCoupon;
import ops.school.api.enums.ResponseViewEnums;
import ops.school.api.exception.Assertions;
import ops.school.api.service.CouponService;
import ops.school.api.util.SpringUtil;
import ops.school.constants.CouponConstants;
import ops.school.constants.NumConstants;
import ops.school.service.TShopCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Service
public class TShopCouponServiceImpl implements TShopCouponService {

    @Autowired
    private ShopCouponMapper shopCouponMapper;
    @Autowired
    private CouponService couponService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    /**
     * @date:   2019/7/25 20:46
     * @author: QinDaoFang
     * @version:version
     * @return: int
     * @param   shopCouponList
     * @Desc:   desc
     */
    @Override
    public int bindShopCoupon(List<ShopCoupon> shopCouponList) {
        Assertions.notEmpty(shopCouponList);
        Integer saveNum = shopCouponMapper.batchInsert(shopCouponList);
        return saveNum;
    }

    @Override
    public int bindShopCoupon(String couponId, String shopIds) {
        Coupon coupon = couponService.getById(couponId);
        if (coupon != null && StringUtils.hasText(shopIds)){
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
        Assertions.notNull(schoolId,couponId);
        //先查询优惠券是否有效且存在
        QueryWrapper<Coupon> queryWrapperCoupon = new QueryWrapper<>();
        queryWrapperCoupon
                .eq("school_id",schoolId)
                .eq("id",couponId)
                .eq("is_delete", NumConstants.DB_TABLE_IS_DELETE_NO)
                .eq("is_invalid", NumConstants.DB_TABLE_IS_INVALID_YES)
                .lt("send_begin_time",new Date(System.currentTimeMillis()))
                .ge("send_end_time",new Date(System.currentTimeMillis()))
                .select("id","school_id","coupon_name","coupon_desc","full_amount","cut_amount","coupon_type","yes_show_index","send_begin_time","send_end_time","effective_days","create_time","create_id  ","update_time","update_id","is_invalid","is_delete");
        Coupon coupon = couponService.getOne(queryWrapperCoupon);
        Assertions.notNull(coupon, ResponseViewEnums.COUPON_HOME_NUM_ERROR);
        //如果是平台优惠券不需要绑定店铺直接返回
        //现在的逻辑是所有的优惠券绑定店铺
        if (coupon.getCouponType().intValue() == CouponConstants.COUPON_TYPE_PLATE ){
            ShopCoupon shopCoupon2 = new ShopCoupon();
            shopCoupon2.setCoupon(coupon);
            shopCoupon2.setShopId(CouponConstants.USER_COUPON_TYPE_PLATE);
            return shopCoupon2;
        }
        if ( coupon.getCouponType().intValue() == CouponConstants.COUPON_TYPE_HOME){
            ShopCoupon shopCoupon2 = new ShopCoupon();
            shopCoupon2.setCoupon(coupon);
            shopCoupon2.setShopId(CouponConstants.USER_COUPON_TYPE_HOME);
            return shopCoupon2;
        }
        //店铺优惠券，或者首页优惠券（需要绑定店铺）查询是否绑定店铺
        QueryWrapper<ShopCoupon> queryWrapperShopCoupon = new QueryWrapper<>();
        queryWrapperShopCoupon
                .eq("coupon_id",couponId)
                .eq("is_delete", NumConstants.DB_TABLE_IS_DELETE_NO)
                .select("id","shop_id","coupon_id","create_time","create_id","is_delete");
        if (shopId != null){
            queryWrapperShopCoupon.eq("shop_id",couponId);
        }
        ShopCoupon shopCoupon = shopCouponMapper.selectOne(queryWrapperShopCoupon);
        Assertions.notNull(shopCoupon,ResponseViewEnums.COUPON_USER_GET_NEED_SHOP);
        //存在并绑定店铺则有效，返回
        shopCoupon.setCoupon(coupon);
        return shopCoupon;
    }


    /**
     * @date:   2019/7/27 11:46
     * @author: QinDaoFang
     * @version:version
     * @return: java.lang.Integer
     * @param   deleteShopIds
     * @Desc:   desc 根据店铺id批量逻辑删除优惠卷
     */
    @Override
    public Integer batchDeleteSCByShopId(List<Long> deleteShopIds) {
        if (CollectionUtils.isEmpty(deleteShopIds)){
            return 0;
        }
        Integer deleteNum = shopCouponMapper.batchDeleteSCByShopId(deleteShopIds);
        return deleteNum;
    }

    /**
     * @date:   2019/7/27 11:52
     * @author: QinDaoFang
     * @version:version
     * @return: java.lang.Integer
     * @param   id
     * @Desc:   desc 根据优惠券id批量逻辑删除优惠卷
     */
    @Override
    public Integer batchDeleteSCByCouponId(Long id) {
        if (id == null){
            return 0;
        }
        Integer deleteNum = shopCouponMapper.batchDeleteSCByCouponId(id);
        return deleteNum;
    }

    /**
     * @date:   2019/7/31 11:58
     * @author: QinDaoFang
     * @version:version
     * @return: java.util.List<ops.school.api.entity.ShopCoupon>
     * @param   shopId
     * @Desc:   desc 根据店铺id查询店铺发放的优惠券
     */
    @Override
    public List<ShopCoupon> getAllShopCouponsByShopId(Long shopId,Integer couponType) {
        Assertions.notNull(shopId,couponType);
        if (SpringUtil.redisCache()){
            String cacheList = (String) stringRedisTemplate.opsForHash().get("SHOP_ALL_COUPONS_LIST",shopId.toString());
            if (cacheList != null){
                return JSON.parseArray(cacheList, ShopCoupon.class);
            }
        }
        QueryWrapper<ShopCoupon> wrapper = new QueryWrapper<>();
        wrapper.eq("shop_id",shopId)
                .eq("is_delete",NumConstants.DB_TABLE_IS_DELETE_NO)
                .eq("coupon_type",couponType);
        List<ShopCoupon> shopCouponList = shopCouponMapper.selectList(wrapper);
        if (SpringUtil.redisCache()){
            stringRedisTemplate.boundHashOps("SHOP_ALL_COUPONS_LIST").put(shopId, JSON.toJSON(shopCouponList));
        }
        return shopCouponList;
    }
}
