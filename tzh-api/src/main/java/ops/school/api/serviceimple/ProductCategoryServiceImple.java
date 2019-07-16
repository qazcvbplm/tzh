package ops.school.api.serviceimple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ops.school.api.dao.ProductCategoryMapper;
import ops.school.api.entity.ProductCategory;
import ops.school.api.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;

@Service
public class ProductCategoryServiceImple extends ServiceImpl<ProductCategoryMapper, ProductCategory> implements ProductCategoryService {

    @Autowired
    private ProductCategoryMapper productCategoryMapper;

    @Override
    public void add(@Valid ProductCategory productCategory) {
        productCategory.setSort(System.currentTimeMillis());
        this.save(productCategory);
    }

    @Override
    public List<ProductCategory> findByShop(int shopId) {
        return productCategoryMapper.findByShop(shopId);
    }

}
