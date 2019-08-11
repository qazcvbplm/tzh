package ops.school.api.dao;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ops.school.api.dto.ShopFeiEDTO;
import ops.school.api.entity.ShopFeiE;
import org.apache.ibatis.annotations.Param;


public interface ShopFeiEMapper extends BaseMapper<ShopFeiE> {

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: java.lang.Integer
     * @param   dto
     * @Desc:   desc 分页查询统计
     */
    Integer countLimitByDTO(ShopFeiEDTO dto);

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.LimitTableData<ShopFeiEVO>
     * @param   dto
     * @Desc:   desc 分页查询
     */
    List<ShopFeiE> selectLimitByDTO(ShopFeiEDTO dto);

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.LimitTableData<ShopFeiEVO>
     * @param   ids
     * @Desc:   desc 批量查询
     */
    List<ShopFeiE> batchFindByIds(@Param("list") List<Long> ids);


}
