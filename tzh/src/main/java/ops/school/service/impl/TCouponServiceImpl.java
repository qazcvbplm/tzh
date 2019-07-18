package ops.school.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ops.school.api.dao.*;
import ops.school.api.dto.project.CouponDTO;
import ops.school.api.entity.*;
import ops.school.api.enums.PublicErrorEnums;
import ops.school.api.enums.ResponseViewEnums;
import ops.school.api.exception.Assertions;
import ops.school.api.service.CouponService;
import ops.school.api.util.ResponseObject;
import ops.school.constants.CouponContants;
import ops.school.constants.NumContants;
import ops.school.service.TCouponService;
import ops.school.service.TShopCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TCouponServiceImpl implements TCouponService {

    @Autowired
    private CouponMapper couponMapper;
    @Autowired
    private CouponService couponService;

    @Autowired
    private SchoolMapper schoolMapper;

    @Autowired
    private HomeCouponMapper homeCouponMapper;

    @Autowired
    private WxUserCouponMapper wxUserCouponMapper;

    @Autowired
    private TShopCouponService tShopCouponService;

    @Autowired
    private WxUserMapper wxUserMapper;

    @Override
    public List<Coupon> findByIndex(Long schoolId, Integer couponType, Integer yesShowIndex) {
        if (schoolId == null || couponType == null){
            return null;
        }
        Map<String,Object> map = new HashMap<>();
        map.put("schoolId",schoolId);
        map.put("couponType",couponType);
        map.put("yesShowIndex",yesShowIndex);
        return couponMapper.findByIndex(map);
    }

    @Override
    public Integer count(Long schoolId, Integer couponType) {
        if (schoolId == null || couponType == null){
            return 0;
        }
        CouponDTO couponDTO = new CouponDTO();
        couponDTO.setCouponType(couponType);
        couponDTO.setSchoolId(schoolId);
        couponDTO.setIsDelete(0);
        couponDTO.setIsInvalid(0);
        return couponMapper.countLimitByDTO(couponDTO);
    }

    @Override
    public List<Coupon> findCoupons(Long schoolId, Integer couponType, int page, int size) {
        if (schoolId == null || couponType == null){
            return null;
        }
        QueryWrapper<Coupon> query = new QueryWrapper<>();
        query.lambda().eq(Coupon::getSchoolId,schoolId);
        query.lambda().eq(Coupon::getCouponType,couponType);
        query.lambda().eq(Coupon::getIsDelete, 0);
        query.lambda().eq(Coupon::getIsInvalid,0);
        IPage<Coupon> rs = couponService.page(new Page<>(page,size),query);
        return rs.getRecords();
    }

    @Override
    public int insert(Coupon coupon) {
        couponMapper.insert(coupon);
        return 0;
    }

    /**
     * @date:   2019/7/18 14:16
     * @author: QinDaoFang
     * @version:version
     * @return: ops.school.api.util.ResponseObject
     * @param   couponDTO 包括schoolid，ids，createid
     * @Desc:   desc 首页优惠券绑定
     */
    @Override
    public ResponseObject bindHomeCouponsBySIdAndIds(CouponDTO couponDTO) {
        Assertions.notNull(couponDTO);
        Assertions.notNull(couponDTO.getSchoolId());
        Assertions.notEmpty(couponDTO.getCouponIdS());
        Assertions.notNull(couponDTO.getCreateId(),couponDTO.getUpdateId());
        //查询数据看对不对
        List<Coupon> couponLists = couponMapper.batchFindHomeBySIdAndCIds(couponDTO);
        if (CollectionUtils.isEmpty(couponLists) || couponLists.size() != couponDTO.getCouponIdS().size()){
            return new ResponseObject(false, ResponseViewEnums.COUPON_HOME_NUM_ERROR);
        }
        //查询完批量更新优惠券首页展示
        Integer updateNum = couponMapper.batchUpdateToShowIndex(couponDTO);
        return new ResponseObject(true,"操作成功，更新条数"+updateNum);
    }

    /**
     * @date:   2019/7/18 17:33
     * @author: QinDaoFang
     * @version:version
     * @return: ops.school.api.util.ResponseObject
     * @param   map
     * @Desc:   desc 根据用户id，学校id，店铺id，优惠券id，让用户获取优惠券
     */
    @Override
    public ResponseObject userGetCouponByIdMap(Map map) {
        Assertions.notNull(map,map.get("userId"),map.get("schoolId"),map.get("shopId"),map.get("couponId"));
        //查询用户是否存在 todo 可能导致查询速度慢
        WxUser wxUser = wxUserMapper.selectById((Long)map.get("userId"));
        Assertions.notNull(wxUser,ResponseViewEnums.WX_USER_NO_EXIST);
        //根据id查询有效的优惠券
        ShopCoupon shopCoupon = tShopCouponService.findPoweredCouponBySIdAndSId((Long) map.get("schoolId"),(Long)map.get("shopId"),(Long)map.get("couponId"));
        Assertions.notNull(shopCoupon,ResponseViewEnums.COUPON_HOME_NUM_ERROR);
        WxUserCoupon wxUserCoupon = new WxUserCoupon();
        wxUserCoupon.setCouponId(shopCoupon.getCouponId());
        wxUserCoupon.setGetTime(new Date());
        wxUserCoupon.setIsInvalid(NumContants.DB_TABLE_IS_INVALID_NOT_USED);
        wxUserCoupon.setWxUserId(wxUser.getId());
        //加上过期时间 todo 工具类
        Date failureTime = new Date();
        wxUserCoupon.setFailureTime(failureTime);
        Integer addNum = wxUserCouponMapper.insert(wxUserCoupon);
        if (addNum == null || addNum < 1){
            return new ResponseObject(false,ResponseViewEnums.COUPON_USER_GET_ERROR);
        }
        return new ResponseObject(true,ResponseViewEnums.COUPON_USER_GET_SUCCESS);
    }
}
