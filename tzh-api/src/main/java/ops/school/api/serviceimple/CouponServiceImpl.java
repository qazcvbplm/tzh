package ops.school.api.serviceimple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ops.school.api.dao.CouponMapper;
import ops.school.api.entity.Coupon;
import ops.school.api.service.CouponService;
import org.springframework.stereotype.Service;

@Service
public class CouponServiceImpl extends ServiceImpl<CouponMapper, Coupon> implements CouponService {
}
