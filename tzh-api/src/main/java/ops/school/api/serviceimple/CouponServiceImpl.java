package ops.school.api.serviceimple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ops.school.api.dao.CouponMapper;
import ops.school.api.dao.SchoolMapper;
import ops.school.api.dto.project.CouponDTO;
import ops.school.api.entity.Coupon;
import ops.school.api.entity.School;
import ops.school.api.enums.PublicErrorEnums;
import ops.school.api.exception.Assertions;
import ops.school.api.service.CouponService;
import ops.school.api.service.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CouponServiceImpl extends ServiceImpl<CouponMapper, Coupon> implements CouponService {



}
