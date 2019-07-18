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
     *
     * @param coupon
     * @return
     */
    int insert(Coupon coupon);
}