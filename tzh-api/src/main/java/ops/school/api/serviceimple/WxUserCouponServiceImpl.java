package ops.school.api.serviceimple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ops.school.api.dao.WxUserCouponMapper;
import ops.school.api.entity.WxUserCoupon;
import ops.school.api.service.WxUserCouponService;
import org.springframework.stereotype.Service;

@Service
public class WxUserCouponServiceImpl extends ServiceImpl<WxUserCouponMapper, WxUserCoupon> implements WxUserCouponService {
}
