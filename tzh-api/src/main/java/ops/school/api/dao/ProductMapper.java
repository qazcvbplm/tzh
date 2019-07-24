package ops.school.api.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ops.school.api.dto.project.ProductAndAttributeDTO;
import ops.school.api.dto.project.ProductOrderDTO;
import ops.school.api.entity.Product;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ProductMapper extends BaseMapper<Product> {

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);


    List<Product> findByCategoryId(int productCategoryId);

    List<Product> findByCategoryId_wxUser(int productCategoryId);

    BigDecimal minDiscount(Integer id);

    List<Product> findByShopAllDiscount(int productCategoryId);

    void sale(Map<String, Object> map);

    List<Product> findin(List<Integer> pids);

    int stock(Map<String, Object> map);

    /**
     * @date:   2019/7/20 16:33
     * @author: QinDaoFang
     * @version:version
     * @return: java.util.List<ops.school.api.dto.project.ProductAndAttributeDTO>
     * @param   pIdAndAIdMap
     * @Desc:   desc 根据商品id和商品规格id批量查询商品及规格 map<pid,aid>
     */
    List<ProductAndAttributeDTO> batchFindProdAttributeByIdS(@Param("map") Map<Long,Long> pIdAndAIdMap);
}