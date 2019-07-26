package ops.school.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import ops.school.api.entity.ProductCategory;
import ops.school.api.util.ResponseObject;

import java.util.List;

public interface ProductCategoryService extends IService<ProductCategory> {

    void add(ProductCategory productCategory);

    List<ProductCategory> findByShop(int shopId);


    /**
     * @date:   2019/7/26 10:53
     * @author: QinDaoFang
     * @version:version
     * @return: ops.school.api.util.ResponseObject
     * @param   categoryId
     * @Desc:   desc 根据类目id删除类目和商品
     */
    ResponseObject deleteCategoryAndProdSById(Integer categoryId);
}
