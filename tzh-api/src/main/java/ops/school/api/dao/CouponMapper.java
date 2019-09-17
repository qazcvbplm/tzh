package ops.school.api.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ops.school.api.dto.project.CouponDTO;
import ops.school.api.entity.Coupon;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * CouponDAO继承基类
 */
@Repository
public interface CouponMapper extends BaseMapper<Coupon> {
    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: java.lang.Integer
     * @param   dto
     * @Desc:   desc 分页查询统计
     */
    Integer countLimitByDTO(CouponDTO dto);

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.LimitTableData<CouponVO>
     * @param   dto
     * @Desc:   desc 分页查询
     */
    List<Coupon> selectLimitByDTO(CouponDTO dto);

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.LimitTableData<CouponVO>
     * @param   ids
     * @Desc:   desc 批量查询
     */
    List<Coupon> batchFindByIds(@Param("list") List<Long> ids);

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
     * @return: List<Coupon>
     * @param   map
     * @Desc:   desc 通过学校id和优惠券分类查询
     */
    List<Coupon> findByIndex(Map<String,Object> map);

    /**
     * @date:
     * @author: Lee
     * @param coupon
     * @return
     */
    int insertOne(Coupon coupon);

    /**
     * @date:   2019/7/18 15:38
     * @author: QinDaoFang
     * @version:version
     * @return: java.util.List<ops.school.api.entity.Coupon>
     * @param   couponDTO
     * @Desc:   desc 根据学校id，优惠卷ids，查询优惠券
     */
    List<Coupon> batchFindHomeBySIdAndCIds(CouponDTO couponDTO);

    /**
     * @date:   2019/7/18 15:53
     * @author: QinDaoFang
     * @version:version
     * @return: java.lang.Integer
     * @param   couponDTO 包含学校id，优惠券ids，更新后展示状态
     * @Desc:   desc 查询完批量更新优惠券首页展示
     */
    Integer batchUpdateToShowIndex(CouponDTO couponDTO);

    /**
     * @date:   2019/7/18 17:39
     * @author: Lee
     * @version:version
     * @return: java.util.List
     * @param
     * @Desc:   desc 查询所有未失效和未删除的优惠券中已失效的优惠券
     */
    List<Coupon> findInvalidCoupon();

    /**
     * @date:   2019/8/7 15:12
     * @author: QinDaoFang
     * @version:version
     * @return: java.lang.Integer
     * @param
     * @Desc:   desc
     */
    Integer countInvalidCoupon();

    /**
     * @date:   2019/8/7 15:17
     * @author: QinDaoFang
     * @version:version
     * @return: java.util.List<ops.school.api.entity.Coupon>
     * @param
     * @Desc:   desc
     */
    List<Coupon> limitFindInvalidUserCoupon();

    /**
     * @date:   2019/8/7 15:19
     * @author: QinDaoFang
     * @version:version
     * @return: java.lang.Integer
     * @param   list
     * @Desc:   desc
     */
    Integer batchUpdateToUnInvalidByIds(@Param("list") List<Long> list);
}