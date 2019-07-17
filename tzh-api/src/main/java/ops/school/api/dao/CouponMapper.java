package ops.school.api.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ops.school.api.entity.Coupon;
import org.springframework.stereotype.Repository;

/**
 * CouponDAO继承基类
 */
@Repository
public interface CouponMapper extends BaseMapper<Coupon> {
}