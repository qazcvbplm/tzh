package ops.school.api.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ops.school.api.entity.Product;

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
}