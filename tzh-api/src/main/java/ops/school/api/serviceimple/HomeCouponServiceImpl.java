package ops.school.api.serviceimple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ops.school.api.dao.HomeCouponMapper;
import ops.school.api.entity.HomeCoupon;
import ops.school.api.service.HomeCouponService;
import org.springframework.stereotype.Service;

@Service
public class HomeCouponServiceImpl extends ServiceImpl<HomeCouponMapper, HomeCoupon> implements HomeCouponService {
}
