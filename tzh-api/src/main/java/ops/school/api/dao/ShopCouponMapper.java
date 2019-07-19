package ops.school.api.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ops.school.api.dto.project.ShopCouponDTO;
import ops.school.api.entity.ShopCoupon;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ShopCouponDAO继承基类
 */
@Repository
public interface ShopCouponMapper extends BaseMapper<ShopCoupon> {


    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: java.lang.Integer
     * @param   dto
     * @Desc:   desc 分页查询统计
     */
    Integer countLimitByDTO(ShopCouponDTO dto);

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.LimitTableData<ShopCouponVO>
     * @param   dto
     * @Desc:   desc 分页查询
     */
    List<ShopCoupon> selectLimitByDTO(ShopCouponDTO dto);

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.LimitTableData<ShopCouponVO>
     * @param   ids
     * @Desc:   desc 批量查询
     */
    List<ShopCoupon> batchFindByIds(@Param("list") List<Long> ids);

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

    int insert(ShopCoupon shopCoupon);
}