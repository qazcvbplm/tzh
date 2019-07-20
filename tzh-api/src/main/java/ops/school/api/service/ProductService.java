package ops.school.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import ops.school.api.dto.project.ProductAndAttributeDTO;
import ops.school.api.dto.project.ProductOrderDTO;
import ops.school.api.entity.Product;

import java.util.List;
import java.util.Map;

public interface ProductService extends IService<Product> {


    List<Product> findByCategoryId(int productCategoryId);

    List<Product> findByCategoryId_wxUser(int productCategoryId);

    List<Product> findByShopAllDiscount(int productCategoryId);


    void sale(List<Integer> pids, List<Integer> counts);

    Integer sales(Map<String, Object> map);

    /**
     * @date:   2019/7/20 16:30
     * @author: QinDaoFang
     * @version:version
     * @return: java.util.List<ops.school.api.dto.project.ProductAndAttributeDTO>
     * @param   pIdAndAIdMap
     * @Desc:   desc 根据商品id和商品规格id批量查询商品及规格
     */
    List<ProductAndAttributeDTO> batchFindProdAttributeByIdS(Map<Long,Long> pIdAndAIdMap);
}
