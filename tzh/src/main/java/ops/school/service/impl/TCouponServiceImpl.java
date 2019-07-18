package ops.school.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ops.school.api.dao.CouponMapper;
import ops.school.api.dao.HomeCouponMapper;
import ops.school.api.dao.SchoolMapper;
import ops.school.api.dto.project.CouponDTO;
import ops.school.api.entity.Coupon;
import ops.school.api.entity.HomeCoupon;
import ops.school.api.entity.School;
import ops.school.api.enums.PublicErrorEnums;
import ops.school.api.enums.ResponseViewEnums;
import ops.school.api.exception.Assertions;
import ops.school.api.service.CouponService;
import ops.school.api.util.ResponseObject;
import ops.school.constants.CouponContants;
import ops.school.constants.NumContants;
import ops.school.service.TCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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

    @Override
    public List<Coupon> findByIndex(Long schoolId, Integer couponType) {
        if (schoolId == null || couponType == null){
            return null;
        }
        Map<String,Object> map = new HashMap<>();
        map.put("schoolId",schoolId);
        map.put("couponType",couponType);
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
        List<Coupon> couponList = this.findByIndex(couponDTO.getSchoolId(), CouponContants.COUPON_TYPE_HOME);
        //查询数据看对不对
        List<Coupon> couponLists = couponMapper.batchFindHomeBySIdAndCIds(couponDTO);
        if (CollectionUtils.isEmpty(couponLists) || couponLists.size() != couponDTO.getCouponIdS().size()){
            return new ResponseObject(false, ResponseViewEnums.COUPON_HOME_NUM_ERROR);
        }
        //查询完批量更新优惠券首页展示
        Integer updateNum = couponMapper.batchUpdateToShowIndex(couponDTO);

        return null;
    }
}
