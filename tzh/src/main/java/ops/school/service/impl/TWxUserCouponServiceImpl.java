package ops.school.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ops.school.api.dao.ShopCouponMapper;
import ops.school.api.dao.ShopMapper;
import ops.school.api.dao.WxUserCouponMapper;
import ops.school.api.entity.Shop;
import ops.school.api.entity.ShopCoupon;
import ops.school.api.entity.WxUserCoupon;
import ops.school.api.enums.ResponseViewEnums;
import ops.school.api.exception.Assertions;
import ops.school.api.service.ShopService;
import ops.school.api.service.WxUserService;
import ops.school.api.util.PublicUtilS;
import ops.school.api.util.SpringUtil;
import ops.school.api.constants.CouponConstants;
import ops.school.api.constants.NumConstants;
import ops.school.service.TWxUserCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
public class TWxUserCouponServiceImpl implements TWxUserCouponService {

    @Autowired
    private WxUserCouponMapper wxUserCouponMapper;

    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private ShopCouponMapper shopCouponMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private WxUserService wxUserService;

    @Autowired
    private ShopService shopService;

    @Override
    public List<WxUserCoupon> findInvalidUserCoupon() {
        return wxUserCouponMapper.findInvalidUserCoupon();
    }

    @Override
    public int updateIsInvalid(WxUserCoupon wxUserCoupon) {
        if (wxUserCoupon == null || wxUserCoupon.getId() == null || wxUserCoupon.getIsInvalid() == null){
            return -1;
        }
        int rs = wxUserCouponMapper.updateOnee(wxUserCoupon);
        if (rs > NumConstants.INT_NUM_0){
            if (SpringUtil.redisCache()){
                QueryWrapper<WxUserCoupon> wrapper = new QueryWrapper<>();
                wrapper.eq("id",wxUserCoupon.getId());
                WxUserCoupon wxUserCouponSelect = wxUserCouponMapper.selectOne(wrapper);
                //删除 用户的优惠券
                if (wxUserCouponSelect != null && wxUserCouponSelect.getWxUserId() != null){
                    stringRedisTemplate.opsForHash().delete("WX_USER_CAN_USE_COUPONS_LIST",wxUserCouponSelect.getWxUserId().toString());

                }
            }
        }
        return rs;
    }

    @Override
    public List<WxUserCoupon> findUserCoupon(Long wxUserId, Long shopId) {
        Assertions.notNull(wxUserId,shopId);
        List<WxUserCoupon> wxUserCoupons = wxUserCouponMapper.selectAllUserCoupons(wxUserId,shopId);
        if (wxUserCoupons.size() < NumConstants.INT_NUM_1){
            return new ArrayList<>();
        }
        List<Long> couponIdS = PublicUtilS.getValueList(wxUserCoupons,"couponId");
        if (couponIdS.size() < NumConstants.INT_NUM_1){
            return new ArrayList<>();
        }
        List<ShopCoupon> shopCouponList = shopCouponMapper.batchFindSCByCouponIdSAndShopId(couponIdS,shopId);
        List<WxUserCoupon> resultWXCouponList = new ArrayList<>();
        for (WxUserCoupon wxUserCoupon : wxUserCoupons) {
            if (wxUserCoupon.getCoupon().getCouponType() == CouponConstants.COUPON_TYPE_SHOP || wxUserCoupon.getCoupon().getCouponType() == CouponConstants.COUPON_TYPE_HOME){
                for (ShopCoupon shopCoupon : shopCouponList) {
                    if (wxUserCoupon.getCoupon().getCouponType() == CouponConstants.COUPON_TYPE_HOME ){
                        //如果是优惠券id是一样的，
                        if (wxUserCoupon.getCouponId().intValue() == shopCoupon.getCouponId().intValue()){
                            //如果是优惠卷是1 并且优惠卷是一张，并且shopid是当前shopid就是传的值
                            if (shopId.intValue() == shopCoupon.getShopId().intValue()){
                                resultWXCouponList.add(wxUserCoupon);
                                break;
                            }

                        }
                    }else if (wxUserCoupon.getCoupon().getCouponType() == CouponConstants.COUPON_TYPE_SHOP){
                        // 如果类型是0 并且是当前店铺的，就是传过来的id
                        if (wxUserCoupon.getShopId().intValue() == shopId.intValue()){
                            resultWXCouponList.add(wxUserCoupon);
                            break;
                        }
                    }
                } //for
            }else {
                //如果是2的直接返回
                resultWXCouponList.add(wxUserCoupon);
            }
        }
        return resultWXCouponList;
    }

    @Override
    public List<Map<String,Object>> findAllUserCoupons(Long wxUserId) {
        return wxUserCouponMapper.findAllUserCoupons(wxUserId);
    }

    /**
     * @date:   2019/7/25 15:30
     * @author: QinDaoFang
     * @version:version
     * @return: java.util.List<ops.school.api.entity.WxUserCoupon>
     * @param   userId
     * @param   wxUserCoupon
     * @Desc:   desc 分页查询用户所有的优惠券（isInvalid不传查所有，传0查有效）
     */
    @Override
    public IPage<WxUserCoupon> pageFindALLCouponsByUserId(Long userId, WxUserCoupon wxUserCoupon) {
        Assertions.notNull(userId, ResponseViewEnums.WX_USER_NEED_USER_ID);
        QueryWrapper<WxUserCoupon> wrapper = new QueryWrapper<>();
        wrapper.eq("wx_user_id",userId);
        if (wxUserCoupon.getIsInvalid() != null){
            wrapper.eq("is_invalid",wxUserCoupon.getIsInvalid());
        }
        Integer countNum = wxUserCouponMapper.selectCount(wrapper);
        List<WxUserCoupon> userCoupons =  wxUserCouponMapper.pageFindALLCouponsByUserId(userId,wxUserCoupon);
        if (userCoupons.size() < NumConstants.INT_NUM_1){
            IPage<WxUserCoupon> wxUserCouponIPage = new Page<>();
            wxUserCouponIPage.setRecords(new ArrayList<>());
            wxUserCouponIPage.setTotal(NumConstants.Long_NUM_0);
            return wxUserCouponIPage;
        }
        //把店铺也拼接进去
        List<Long> shopIds = PublicUtilS.getValueList(userCoupons,"shopId");
        PublicUtilS.removeDuplicate(shopIds);
        Collection collection = shopMapper.selectBatchIds(shopIds);
        Map<Long,Shop> map = PublicUtilS.listForMapValueE(collection,"id");
        for (WxUserCoupon userCoupon : userCoupons) {
            if (map.get(userCoupon.getShopId().intValue()) != null && userCoupon.getCouponType().intValue() == NumConstants.INT_NUM_0){
                userCoupon.setShop(map.get(userCoupon.getShopId().intValue()));
            }
        }
        IPage<WxUserCoupon> result = new Page<>();
        result.setRecords(userCoupons);
        result.setTotal(Long.valueOf(countNum));
        return result;
    }

    /**
     * @date:   2019/7/27 17:16
     * @author: QinDaoFang
     * @version:version
     * @return: java.util.List<ops.school.api.entity.Shop>
     * @param   userId
     * @param   couponId
     * @Desc:   desc 根据优惠券id查询所有优惠券是1，2的店铺
     */
    @Override
    public List<Shop> findALLType12ShopsByUserId(Long userId, Long couponId) {
        Assertions.notNull(userId, ResponseViewEnums.WX_USER_NEED_USER_ID);
        Assertions.notNull(couponId, ResponseViewEnums.COUPON_FIND_NEED_ID);
        QueryWrapper<WxUserCoupon> wrapper = new QueryWrapper<>();
        wrapper.eq("wx_user_id",userId)
                .eq("coupon_id",couponId);
        //不要判断逻辑
//        Integer countNum = wxUserCouponMapper.selectCount(wrapper);
//        if (countNum < NumConstants.INT_NUM_1){
//            return null;
//        }
        List<ShopCoupon> shopCouponList = shopCouponMapper.batchFindSCByCouponIdS(Collections.singletonList(couponId));
        if (CollectionUtils.isEmpty(shopCouponList)){
            return new ArrayList<>();
        }
        List<Long> shopIds = PublicUtilS.getValueList(shopCouponList,"shopId");
        if (CollectionUtils.isEmpty(shopIds)){
            return new ArrayList<>();
        }
        List<Shop> shopList = shopMapper.selectBatchIds(shopIds);
        return shopList;
    }

    /**
     * @date:   2019/8/7 14:21
     * @author: QinDaoFang
     * @version:version
     * @return: java.lang.Integer
     * @param
     * @Desc:   desc
     */
    @Override
    public Integer countInvalidUserCoupon() {
        Integer countNum = wxUserCouponMapper.countInvalidUserCoupon();
        return countNum;
    }

    /**
     * @date:   2019/8/7 14:31
     * @author: QinDaoFang
     * @version:version
     * @return: java.util.List<ops.school.api.entity.WxUserCoupon>
     * @param
     * @Desc:   desc
     */
    @Override
    public List<WxUserCoupon> limitFindInvalidUserCoupon() {
        List<WxUserCoupon> userCoupons = wxUserCouponMapper.limitFindInvalidUserCoupon();
        return userCoupons;
    }

    /**
     * @date:   2019/8/7 14:58
     * @author: QinDaoFang
     * @version:version
     * @return: java.lang.Integer
     * @param   idList
     * @Desc:   desc
     */
    @Override
    public Integer batchUpdateToUnInvalidByIds(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)){
            return 0;
        }
        Integer updateNum = wxUserCouponMapper.batchUpdateToUnInvalidByIds(idList);
        return updateNum;
    }

    /**
     * @date:   2019/9/3 23:48
     * @author: QinDaoFang
     * @version:version
     * @return: java.util.List<ops.school.api.entity.WxUserCoupon>
     * @param   wxUserId
     * @param   shopId
     * @param   schoolId
     * @Desc:   desc
     */
    @Override
    public List<WxUserCoupon> findUserCouponBySchool(Long wxUserId, Long shopId, Integer schoolId) {
        Assertions.notNull(wxUserId);
        Assertions.notNull(shopId);
        if (schoolId == null || schoolId == 0){
            Shop shop = shopService.getById(shopId);
            Assertions.notNull(shop,ResponseViewEnums.SHOP_HAD_CHANGE);
            Assertions.notNull(shop.getSchoolId(),ResponseViewEnums.SHOP_HAD_CHANGE);
            schoolId = shop.getSchoolId();
        }
        List<WxUserCoupon> wxUserCoupons = wxUserCouponMapper.selectAllUserCouponsBySchool(wxUserId,shopId,schoolId);
        if (wxUserCoupons.size() < NumConstants.INT_NUM_1){
            return new ArrayList<>();
        }
        List<Long> couponIdS = PublicUtilS.getValueList(wxUserCoupons,"couponId");
        if (couponIdS.size() < NumConstants.INT_NUM_1){
            return new ArrayList<>();
        }
        List<ShopCoupon> shopCouponList = shopCouponMapper.batchFindSCByCouponIdSAndShopId(couponIdS,shopId);
        List<WxUserCoupon> resultWXCouponList = new ArrayList<>();
        for (WxUserCoupon wxUserCoupon : wxUserCoupons) {
            if (wxUserCoupon.getCoupon().getCouponType() == CouponConstants.COUPON_TYPE_SHOP || wxUserCoupon.getCoupon().getCouponType() == CouponConstants.COUPON_TYPE_HOME){
                for (ShopCoupon shopCoupon : shopCouponList) {
                    //如果是1，wxcoupon村的是shopId-1，不能比较
                    if (wxUserCoupon.getCoupon().getCouponType() == CouponConstants.COUPON_TYPE_HOME ){
                        //如果是优惠券id是一样的，
                        if (wxUserCoupon.getCouponId().intValue() == shopCoupon.getCouponId().intValue()){
                            //如果是优惠卷是1 并且优惠卷是一张，并且shopid是当前shopid就是传的值
                            if (shopId.intValue() == shopCoupon.getShopId().intValue()){
                                resultWXCouponList.add(wxUserCoupon);
                                break;
                            }

                        }
                    }else if (wxUserCoupon.getCoupon().getCouponType() == CouponConstants.COUPON_TYPE_SHOP){
                        // 如果类型是0 并且是当前店铺的，就是传过来的id
                        if ((wxUserCoupon.getCouponId().intValue() == shopCoupon.getCouponId().intValue())&&(wxUserCoupon.getShopId().intValue() == shopCoupon.getShopId().intValue()) && (shopCoupon.getShopId().intValue() == shopId.intValue())){
                            resultWXCouponList.add(wxUserCoupon);
                            break;
                        }
                    }
                } //for
            }else {
                //如果是2的直接返回
                resultWXCouponList.add(wxUserCoupon);
            }
        }
        return resultWXCouponList;
    }
}
