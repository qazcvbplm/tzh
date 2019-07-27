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
import ops.school.api.util.PublicUtilS;
import ops.school.constants.NumConstants;
import ops.school.service.TWxUserCouponService;
import org.springframework.beans.factory.annotation.Autowired;
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
        return rs;
    }

    @Override
    public List<WxUserCoupon> findUserCoupon(Long wxUserId, Long shopId) {

        List<WxUserCoupon> wxUserCoupons = wxUserCouponMapper.userFindCouponsByWIdSId(wxUserId,shopId);
        return wxUserCoupons;
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
}
