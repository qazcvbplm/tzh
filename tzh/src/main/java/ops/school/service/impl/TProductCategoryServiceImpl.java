package ops.school.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import ops.school.api.dao.ProductCategoryMapper;
import ops.school.api.dao.ProductMapper;
import ops.school.api.entity.Product;
import ops.school.api.entity.ProductCategory;
import ops.school.api.enums.ResponseViewEnums;
import ops.school.api.exception.Assertions;
import ops.school.api.util.PublicUtilS;
import ops.school.api.util.ResponseObject;
import ops.school.api.constants.NumConstants;
import ops.school.service.TProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * CreatebyFang
 * fangfor@outlook.com
 * 2019/7/26
 * 10:59
 * #
 */
@Service
public class TProductCategoryServiceImpl implements TProductCategoryService {

    @Autowired
    private ProductCategoryMapper productCategoryMapper;

    @Autowired
    private ProductMapper productMapper;

    /**
     * @date:   2019/7/26 11:17
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
        productCategory.setIsDelete(NumConstants.DB_TABLE_IS_DELETE_YES_DELETE);
        productCategory.setId(categoryId);
        int categoryNum = productCategoryMapper.updateById(productCategory);
        if (categoryNum != 1){
            return new ResponseObject(false, ResponseViewEnums.SUCCESS);
        }
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_category_id",categoryId)
                .eq("is_delete",NumConstants.DB_TABLE_IS_DELETE_NO);
        List<Product> productList = productMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(productList)){
            return new ResponseObject(true,ResponseViewEnums.SUCCESS);
        }
        List<Long> productIdS = PublicUtilS.getValueList(productList,"id");
        if (CollectionUtils.isEmpty(productIdS)){
            return new ResponseObject(true,ResponseViewEnums.SUCCESS);
        }
        Integer deleteNum = productMapper.deleteLogicBatchIds(productIdS);
        if (deleteNum < NumConstants.INT_NUM_1){
            return new ResponseObject(false,ResponseViewEnums.DELETE_FAILED);
        }
        return new ResponseObject(true,ResponseViewEnums.SUCCESS);
    }
}
