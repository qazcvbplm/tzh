package ops.school.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.models.auth.In;
import ops.school.api.dao.*;
import ops.school.api.dto.project.CouponDTO;
import ops.school.api.entity.*;
import ops.school.api.enums.PublicErrorEnums;
import ops.school.api.enums.ResponseViewEnums;
import ops.school.api.exception.Assertions;
import ops.school.api.service.CouponService;
import ops.school.api.service.ShopCouponService;
import ops.school.api.util.PublicUtilS;
import ops.school.api.util.ResponseObject;
import ops.school.api.util.TimeUtilS;
import ops.school.constants.NumConstants;
import ops.school.service.TCouponService;
import ops.school.service.TShopCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class TCouponServiceImpl implements TCouponService {

    @Autowired
    private CouponMapper couponMapper;
    @Autowired
    private CouponService couponService;

    @Autowired
    private WxUserCouponMapper wxUserCouponMapper;

    @Autowired
    private TShopCouponService tShopCouponService;

    @Autowired
    private ShopCouponService shopCouponService;

    @Autowired
    private WxUserMapper wxUserMapper;


    @Autowired
    private ShopCouponMapper shopCouponMapper;


    @Override
    public List<Coupon> findByIndex(Long schoolId, Integer yesShowIndex,Long userId) {
        Assertions.notNull(schoolId,yesShowIndex,userId);
        Map<String,Object> map = new HashMap<>();
        map.put("schoolId",schoolId);
        map.put("yesShowIndex",yesShowIndex);
        List<Coupon> couponList = couponMapper.findByIndex(map);
        if (CollectionUtils.isEmpty(couponList)){
            return new ArrayList<>();
        }
        QueryWrapper<WxUserCoupon> wrapper = new QueryWrapper<>();
        wrapper.eq("wx_user_id",userId);
        List<WxUserCoupon> userCoupons =  wxUserCouponMapper.selectList(wrapper);
        Map<Long,Integer> couponIdMap = PublicUtilS.listForMap(userCoupons,"couponId","couponType");
        List<Coupon> result = new ArrayList<>();
        for (Coupon coupon : couponList) {
            if (couponIdMap.get(coupon.getId()) == null){
                result.add(coupon);
            }
        }
        return result;
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

    /**
     * @author: QinDaoFang
     * @date:   2019/7/25 16:03 
     * @desc:   
     */
    @Override
    public ResponseObject findCoupons(Long schoolId, Integer couponType, int page, int size) {
        if (schoolId == null){
            return new ResponseObject(false, PublicErrorEnums.PULBIC_EMPTY_PARAM);
        }
        QueryWrapper<Coupon> query = new QueryWrapper<>();
        query.eq("school_id",schoolId)
                .eq("is_delete",NumConstants.DB_TABLE_IS_DELETE_NO)
                .orderByDesc("create_time").orderByAsc("is_invalid");
        if (couponType != null){
            query.eq("coupon_type",couponType);
        }
        IPage<Coupon> rs = couponService.page(new Page<>(page,size),query);
        if (rs == null || rs.getTotal() < 1 || rs.getRecords().size() < 1 ){
            return new ResponseObject(true, ResponseViewEnums.SUCCESS)
                    .push("list",rs.getRecords())
                    .push("total",rs.getTotal());
        }
        List<Long> couponIdS = PublicUtilS.getValueList(rs.getRecords(),"id");
        if (couponIdS.size() < 1){
            return new ResponseObject(true, ResponseViewEnums.SUCCESS)
                    .push("list",rs.getRecords())
                    .push("total",rs.getTotal());
        }
        List<ShopCoupon> shopCouponList =  shopCouponService.batchFindSCByCouponIdS(couponIdS);
        if (CollectionUtils.isEmpty(shopCouponList)){
            return new ResponseObject(true, ResponseViewEnums.SUCCESS)
                    .push("list",rs.getRecords())
                    .push("total",rs.getTotal());
        }
        //根据优惠券id分组，id一样的放到一个集合里面
        Map<Long,List<ShopCoupon>> groupCIdListShopCouponMap = PublicUtilS.listforEqualKeyListMap(shopCouponList,"couponId");
        //给前端shopId的字符串拼接，Map<CouponId,ShopIdsString>
        Map<Long,String> couponIdAndShopIdsStringMap = new HashMap();
        for (Map.Entry<Long, List<ShopCoupon>> entry : groupCIdListShopCouponMap.entrySet()) {
            List<ShopCoupon> shopCouponListInEntry = entry.getValue();
            StringBuffer resultShopIds = new StringBuffer();
            for (int i = 0; i <shopCouponListInEntry.size() ; i++) {
                resultShopIds.append(shopCouponListInEntry.get(i).getShopId());
                if (i < shopCouponListInEntry.size() -1){
                    resultShopIds.append(',');
                }
            }
            couponIdAndShopIdsStringMap.put(entry.getKey(),resultShopIds.toString());
        }
        for (Coupon record : rs.getRecords()) {
            if (groupCIdListShopCouponMap.get(record.getId()) != null){
                record.setShopCouponList(groupCIdListShopCouponMap.get(record.getId()));
            }
            if (couponIdAndShopIdsStringMap.get(record.getId()) != null){
                record.setShopIds(couponIdAndShopIdsStringMap.get(record.getId()));
            }
        }
        return new ResponseObject(true, ResponseViewEnums.SUCCESS)
                .push("list",rs.getRecords())
                .push("total",rs.getTotal());

    }

    @Override
    public int insert(Coupon coupon) {
        couponMapper.insertOne(coupon);
        Long couponId = coupon.getId();
        if (couponId == null){
            return -1;
        }
        if (coupon != null && StringUtils.hasText(coupon.getShopIds())){
            return 2;
        }
        String[] shopIdS = coupon.getShopIds().split(",");
        List<ShopCoupon> shopCouponList = new ArrayList<>();
        Long shopIdLong = null;
        for (String shopId : shopIdS) {
            ShopCoupon shopCoupon = new ShopCoupon();
            shopIdLong = Long.valueOf(shopId);
            shopCoupon.setCouponId(couponId);
            shopCoupon.setCouponType(coupon.getCouponType());
            shopCoupon.setShopId(shopIdLong);
            shopCoupon.setCreateId(coupon.getCreateId());
            shopCoupon.setCreateTime(new Date());
            shopCoupon.setIsDelete(NumConstants.DB_TABLE_IS_DELETE_NO);
            shopCouponList.add(shopCoupon);
        }
        int saveNum = tShopCouponService.bindShopCoupon(shopCouponList);
        return saveNum;
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
        //Assertions.notEmpty(couponDTO.getCouponList());
        Assertions.notNull(couponDTO.getCreateId(),couponDTO.getUpdateId());
        // 查询数据看对不对
        List<Coupon> couponLists = couponMapper.batchFindHomeBySIdAndCIds(couponDTO);
        if (CollectionUtils.isEmpty(couponLists) || couponLists.size() != couponDTO.getCouponList().size()){
            return new ResponseObject(false, ResponseViewEnums.COUPON_HOME_NUM_ERROR);
        }
        //查询完批量更新优惠券首页展示
        Integer updateNum = couponMapper.batchUpdateToShowIndex(couponDTO);
        return new ResponseObject(true,"操作成功，更新条数"+updateNum);
    }

    @Override
    public List<Coupon> findInvalidCoupon() {
        return couponMapper.findInvalidCoupon();
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
        Assertions.notNull(map,map.get("userId"),map.get("schoolId"),map.get("couponId"));
        //查询用户是否存在 todo 可能导致查询速度慢
        WxUser wxUser = wxUserMapper.selectOneByUserId((Long)map.get("userId"));
        Assertions.notNull(wxUser,ResponseViewEnums.WX_USER_NO_EXIST);
        Long couponId = (Long) map.get("couponId");
        Coupon coupon = couponMapper.selectById(couponId);
        Assertions.notNull(coupon,ResponseViewEnums.COUPON_HOME_NUM_ERROR);
        //根据id查询有效的优惠券
        ShopCoupon shopCoupon = tShopCouponService.findPoweredCouponBySIdAndSId((Long) map.get("schoolId"),(Long)map.get("shopId"),(Long)map.get("couponId"));
        Assertions.notNull(shopCoupon,ResponseViewEnums.COUPON_HOME_NUM_ERROR);
        WxUserCoupon wxUserCoupon = new WxUserCoupon();
        wxUserCoupon.setCouponId(shopCoupon.getCoupon().getId());
        wxUserCoupon.setGetTime(new Date());
        wxUserCoupon.setIsInvalid(NumConstants.DB_TABLE_IS_INVALID_NOT_USED);
        wxUserCoupon.setWxUserId(wxUser.getId());
        wxUserCoupon.setCouponType(shopCoupon.getCoupon().getCouponType());
        if (shopCoupon != null && shopCoupon.getShopId() != null){
            wxUserCoupon.setShopId(shopCoupon.getShopId());
        }else {
            wxUserCoupon.setShopId(NumConstants.Long_NUM_0);
        }
        //加上过期时间
        Date failureTime = TimeUtilS.getNextDay(new Date(),shopCoupon.getCoupon().getEffectiveDays());
        wxUserCoupon.setFailureTime(failureTime);
        Integer addNum = wxUserCouponMapper.insert(wxUserCoupon);
        if (addNum == null || addNum < 1){
            return new ResponseObject(false,ResponseViewEnums.COUPON_USER_GET_ERROR);
        }
        return new ResponseObject(true,ResponseViewEnums.COUPON_USER_GET_SUCCESS);
    }

    /**
     * @date:   2019/7/26 19:36
     * @author: QinDaoFang
     * @version:version
     * @return: ops.school.api.util.ResponseObject
     * @param   coupon
     * @Desc:   desc
     */
    @Override
    public ResponseObject updateOneById(Coupon coupon) {
        Assertions.notNull(coupon);
        if (coupon.getCouponType() != null){
            coupon.setCouponType(null);
            return new ResponseObject(false,ResponseViewEnums.COUPON_TYPE_CANT_UPDATE);
        }
        couponMapper.updateById(coupon);
        if (coupon == null || !StringUtils.hasText(coupon.getShopIds())){
            return  new ResponseObject(true,"更新成功");
        }
        String[] shopIdS = coupon.getShopIds().split(",");
        List<ShopCoupon> shopCouponList = new ArrayList<>();
        Long shopIdLong = null;
        List<Long> deleteShopIds = new ArrayList<>();
        for (String shopId : shopIdS) {
            ShopCoupon shopCoupon = new ShopCoupon();
            shopIdLong = Long.valueOf(shopId);
            deleteShopIds.add(shopIdLong);
            shopCoupon.setCouponId(coupon.getId());
            shopCoupon.setShopId(shopIdLong);
            shopCoupon.setCreateId(coupon.getSchoolId());
            shopCoupon.setCreateTime(new Date());
            shopCoupon.setIsDelete(NumConstants.DB_TABLE_IS_DELETE_NO);
            shopCouponList.add(shopCoupon);
        }
        //先删除关联
        Integer deleteNum = tShopCouponService.batchDeleteSCByCouponId(coupon.getId());
        //再新增
        int saveNum = tShopCouponService.bindShopCoupon(shopCouponList);
        return new ResponseObject(true,"更新成功");
    }

    /**
     * @date:   2019/7/27 11:58
     * @author: QinDaoFang
     * @version:version
     * @return: ops.school.api.util.ResponseObject
     * @param   coupon
     * @Desc:   desc 根据优惠券id删除优惠券
     */
    @Override
    public ResponseObject deleteCouponAndShopByCId(Coupon coupon) {
        Assertions.notNull(coupon,coupon.getId());
        coupon.setIsDelete(NumConstants.DB_TABLE_IS_DELETE_YES_DELETE);
        int deleteNum = couponMapper.updateById(coupon);
        if (deleteNum < NumConstants.INT_NUM_1){
            return new ResponseObject(false,ResponseViewEnums.DELETE_FAILED);
        }
        Integer deleteShopCoupon = shopCouponMapper.batchDeleteSCByCouponId(coupon.getId());
        return new ResponseObject(true,ResponseViewEnums.DELETE_SUCCESS);
    }
}