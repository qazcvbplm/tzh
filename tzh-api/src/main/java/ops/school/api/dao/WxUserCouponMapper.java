package ops.school.api.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ops.school.api.dto.project.WxUserCouponDTO;
import ops.school.api.entity.WxUserCoupon;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * WxUserCouponDAO继承基类
 */
@Repository
public interface WxUserCouponMapper extends BaseMapper<WxUserCoupon> {

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: java.lang.Integer
     * @param   dto
     * @Desc:   desc 分页查询统计
     */
    Integer countLimitByDTO(WxUserCouponDTO dto);

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.LimitTableData<WxUserCouponVO>
     * @param   dto
     * @Desc:   desc 分页查询
     */
    List<WxUserCoupon> selectLimitByDTO(WxUserCouponDTO dto);

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.LimitTableData<WxUserCouponVO>
     * @param   ids
     * @Desc:   desc 批量查询
     */
    List<WxUserCoupon> batchFindByIds(@Param("list") List<Long> ids);

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: java.lang.Integer
     * @param   id
     * @Desc:   desc 通过id停用
     */
    Integer stopOneById(Long id);

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: java.lang.Integer
     * @param   id
     * @Desc:   desc 通过id启用
     */
    Integer startOneById(Long id);

    /**
     * @date:
     * @author: Lee
     * @version:version
     * @return: java.util.List
     * @param
     * @Desc:   desc 查询所有用户的未使用的失效优惠券
     */
    List<WxUserCoupon> findInvalidUserCoupon();

    /**
     * @date:
     * @author: Lee
     * @version:version
     * @return: int
     * @param
     * @Desc:   desc 修改用户优惠券is_invalid状态
     */
    int update(WxUserCoupon userCoupon);

    /**
     * @date:
     * @author: Lee
     * @version:version
     * @return: java.util.List
     * @param map map集合  wxUserId 和 shopId
     * @Desc:   desc 根据店铺查询用户所有可用的优惠券
     */
    List<WxUserCoupon> findUserCoupon(Map<String,Object> map);

    /**
     * @date:
     * @author: Lee
     * @version:version
     * @return: java.util.Map
     * @param   wxUserId 用户id
     * @Desc:   desc 根据用户id查询用户所有优惠券
     */
    List<Map<String,Object>> findAllUserCoupons(Long wxUserId);

    /**
     * @date:   2019/7/25 15:32
     * @author: QinDaoFang
     * @version:version
     * @return: java.util.List<ops.school.api.entity.WxUserCoupon>
     * @param   userId
     * @param   wxUserCoupon
     * @Desc:   desc 分页查询用户所有的优惠券（isInvalid不传查所有，传0查有效）
     */
    List<WxUserCoupon> pageFindALLCouponsByUserId(@Param("userId") Long userId, @Param("wxUserCoupon") WxUserCoupon wxUserCoupon);
}