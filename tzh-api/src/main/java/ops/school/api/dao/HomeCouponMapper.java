package ops.school.api.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ops.school.api.dto.project.HomeCouponDTO;
import ops.school.api.entity.HomeCoupon;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface HomeCouponMapper extends BaseMapper<HomeCoupon> {

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: java.lang.Integer
     * @param   dto
     * @Desc:   desc 分页查询统计
     */
    Integer countLimitByDTO(HomeCouponDTO dto);

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.LimitTableData<HomeCouponVO>
     * @param   dto
     * @Desc:   desc 分页查询
     */
    List<HomeCoupon> selectLimitByDTO(HomeCouponDTO dto);

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.LimitTableData<HomeCouponVO>
     * @param   ids
     * @Desc:   desc 批量查询
     */
    List<HomeCoupon> batchFindByIds(@Param("list") List<Long> ids);

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
}
