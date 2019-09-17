package ops.school.api.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ops.school.api.entity.ProductAttribute;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductAttributeMapper extends BaseMapper<ProductAttribute> {

    ProductAttribute selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ProductAttribute record);

    int updateByPrimaryKey(ProductAttribute record);

    int deleteOne(int id);

    /**
     * @date:   2019/8/29 13:02
     * @author: QinDaoFang
     * @version:version
     * @return: java.util.List<ops.school.api.entity.ProductAttribute>
     * @param   proIds
     * @Desc:   desc
     */
    List<ProductAttribute> batchFindByProductIds(@Param("list") List<Long> proIds);
}