package ops.school.api.dao;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ops.school.api.dto.ShopPrintDTO;
import ops.school.api.entity.ShopPrint;
import org.apache.ibatis.annotations.Param;


public interface ShopPrintMapper extends BaseMapper<ShopPrint> {

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: java.lang.Integer
     * @param   dto
     * @Desc:   desc 分页查询统计
     */
    Integer countLimitByDTO(ShopPrintDTO dto);

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.LimitTableData<ShopFeiEVO>
     * @param   dto
     * @Desc:   desc 分页查询
     */
    List<ShopPrint> selectLimitByDTO(ShopPrintDTO dto);

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.LimitTableData<ShopFeiEVO>
     * @param   ids
     * @Desc:   desc 批量查询
     */
    List<ShopPrint> batchFindByIds(@Param("list") List<Long> ids);


}
