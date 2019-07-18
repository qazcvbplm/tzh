package ops.school.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ops.school.api.dao.CouponMapper;
import ops.school.api.dto.project.CouponDTO;
import ops.school.api.entity.Coupon;
import ops.school.api.service.CouponService;
import ops.school.service.TCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TCouponServiceImpl implements TCouponService {

    @Autowired
    private CouponMapper couponMapper;
    @Autowired
    private CouponService couponService;

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
}
