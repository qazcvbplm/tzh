package ops.school.api.serviceimple;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ops.school.api.dao.ProductCategoryMapper;
import ops.school.api.dao.ProductMapper;
import ops.school.api.entity.Product;
import ops.school.api.entity.ProductCategory;
import ops.school.api.exception.Assertions;
import ops.school.api.service.ProductCategoryService;
import ops.school.api.util.PublicUtilS;
import ops.school.api.util.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.validation.Valid;
import java.util.List;

@Service
public class ProductCategoryServiceImple extends ServiceImpl<ProductCategoryMapper, ProductCategory> implements ProductCategoryService {

    @Autowired
    private ProductCategoryMapper productCategoryMapper;


    @Autowired
    private ProductMapper productMapper;

    @Override
    public void add(@Valid ProductCategory productCategory) {
        productCategory.setSort(System.currentTimeMillis());
        this.save(productCategory);
    }

    @Override
    public List<ProductCategory> findByShop(int shopId) {
        return productCategoryMapper.findByShop(shopId);
    }

    /**
     * @date:   2019/7/26 10:54
     * @author: QinDaoFang
     * @version:version
     * @return: ops.school.api.util.ResponseObject
     * @param   categoryId
     * @Desc:   desc 根据类目id删除类目和商品
     */
    @Override
    public ResponseObject deleteCategoryAndProdSById(Integer categoryId) {
        Assertions.notNull(categoryId);
        ProductCategory productCategory = new ProductCategory();
        productCategory.setId(categoryId);
        productCategory.setIsDelete(1);
        int categoryNum = productCategoryMapper.updateById(productCategory);
        if (categoryNum != 1){
            return new ResponseObject(false,"删除失败");
        }
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_category_id",categoryId)
                .eq("is_delete",0);
        List<Product> productList = productMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(productList)){
            return new ResponseObject(true,"删除成功");
        }
        List<Long> productIdS = PublicUtilS.getValueList(productList,"id");
        if (CollectionUtils.isEmpty(productIdS)){
            return new ResponseObject(true,"删除成功");
        }
        Integer deleteNum = productMapper.deleteLogicBatchIds(productIdS);
        if (deleteNum < 1){
            return new ResponseObject(false,"删除失败，请检查后操作");
        }
        return new ResponseObject(true,"删除成功");
    }
}
