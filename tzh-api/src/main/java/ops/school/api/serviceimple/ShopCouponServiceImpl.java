package ops.school.api.serviceimple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ops.school.api.dao.ShopCouponMapper;
import ops.school.api.entity.ShopCoupon;
import ops.school.api.service.ShopCouponService;
import org.springframework.stereotype.Service;

@Service
public class ShopCouponServiceImpl extends ServiceImpl<ShopCouponMapper, ShopCoupon> implements ShopCouponService {
}
