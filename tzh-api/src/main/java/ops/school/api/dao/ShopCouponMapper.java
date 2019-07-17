package ops.school.api.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ops.school.api.entity.ShopCoupon;
import org.springframework.stereotype.Repository;

/**
 * ShopCouponDAO继承基类
 */
@Repository
public interface ShopCouponMapper extends BaseMapper<ShopCoupon> {
}