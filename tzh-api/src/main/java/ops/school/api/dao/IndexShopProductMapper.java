package ops.school.api.dao;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ops.school.api.dto.project.IndexShopProductDTO;
import ops.school.api.entity.IndexShopProduct;
import org.apache.ibatis.annotations.Param;


public interface IndexShopProductMapper extends BaseMapper<IndexShopProduct> {

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: java.lang.Integer
     * @param   dto
     * @Desc:   desc 分页查询统计
     */
    Integer countLimitByDTO(IndexShopProductDTO dto);

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.LimitTableData<IndexShopProduct>
     * @param   dto
     * @Desc:   desc 分页查询
     */
    List<IndexShopProduct> selectLimitByDTO(IndexShopProductDTO dto);

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.LimitTableData<IndexShopProduct>
     * @param   ids
     * @Desc:   desc 批量查询
     */
    List<IndexShopProduct> batchFindByIds(@Param("list") List<Long> ids);

    /**
     * @date:   2019/8/22 15:17
     * @author: QinDaoFang
     * @version:version
     * @return: java.util.List<ops.school.api.entity.IndexShopProduct>
     * @param   schoolId
     * @Desc:   desc
     */
    List<IndexShopProduct> findIndexShopProBySchoolId(Long schoolId);

    /**
     * @date:   2019/8/22 16:45
     * @author: QinDaoFang
     * @version:version
     * @return: int
     * @param   indexShopProducts
     * @Desc:   desc
     */
    int batchInsert(@Param("list") List<IndexShopProduct> indexShopProducts);
}
