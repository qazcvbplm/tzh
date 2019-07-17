package ops.school.api.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ops.school.api.entity.WxUserCoupon;
import org.springframework.stereotype.Repository;

/**
 * WxUserCouponDAO继承基类
 */
@Repository
public interface WxUserCouponMapper extends BaseMapper<WxUserCoupon> {
}