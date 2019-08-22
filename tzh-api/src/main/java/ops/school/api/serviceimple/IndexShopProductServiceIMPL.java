package ops.school.api.serviceimple;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import ops.school.api.constants.NumConstants;
import ops.school.api.dao.IndexShopProductMapper;
import ops.school.api.dao.ProductMapper;
import ops.school.api.dao.ShopMapper;
import ops.school.api.entity.IndexShopProduct;
import ops.school.api.entity.Product;
import ops.school.api.entity.Shop;
import ops.school.api.enums.PublicErrorEnums;
import ops.school.api.enums.ResponseViewEnums;
import ops.school.api.exception.Assertions;
import ops.school.api.service.IndexShopProductService;
import ops.school.api.util.PublicUtilS;
import ops.school.api.util.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * CreatebyFang
 * fangfor@outlook.com
 * 2019/8/22
 * 14:57
 * #
 */
@Service("indexShopProductService")
public class IndexShopProductServiceIMPL implements IndexShopProductService {

    @Autowired
    private IndexShopProductMapper indexShopProductMapper;

    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private ProductMapper productMapper;

    /**
     * @date:   2019/8/22 15:10
     * @author: QinDaoFang
     * @version:version
     * @return: java.util.List<ops.school.api.entity.IndexShopProduct>
     * @param   schoolId
     * @Desc:   desc
     */
    @Override
    public ResponseObject findIndexShopProBySchoolId(Long schoolId) {
        Assertions.notNull(schoolId, ResponseViewEnums.INDEX_FIND_NO_SCHOOL);
        List<IndexShopProduct> indexShopProducts = indexShopProductMapper.findIndexShopProBySchoolId(schoolId);
        if (indexShopProducts.size() < NumConstants.INT_NUM_1){
            return new ResponseObject(true, PublicErrorEnums.SUCCESS)
                    .push("shopList",new ArrayList<>())
                    .push("productList",new ArrayList<>());
        }
        List<Long> shopIds = new ArrayList<>();
        List<Long> productIds = new ArrayList<>();
        for (IndexShopProduct index : indexShopProducts) {
            if (index.getShopId() != null){
                shopIds.add(index.getShopId());
            }
            if (index.getProductId() != null){
                productIds.add(index.getProductId());
            }
        }
        List<Shop> shopList = new ArrayList<>();
        if (shopIds.size() > NumConstants.INT_NUM_0){
            shopList = shopMapper.selectBatchIds(shopIds);
        }
        List<Product> productList = new ArrayList<>();
        if (productIds.size() > NumConstants.INT_NUM_0){
            productList = productMapper.selectBatchIds(productIds);
        }
        return new ResponseObject(true, PublicErrorEnums.SUCCESS)
                .push("shopList",shopList)
                .push("productList",productList);
    }

    /**
     * @date:   2019/8/22 16:14
     * @author: QinDaoFang
     * @version:version
     * @return: ops.school.api.util.ResponseObject
     * @param   shops
     * @param   products
     * @param   schoolId
     * @Desc:   desc
     */
    @Override
    public ResponseObject addIndexShopProduct(String shops, String products, Integer schoolId) {
        Assertions.notNull(schoolId, ResponseViewEnums.INDEX_ADD_NO_SCHOOL);
        String regex = ",+|，+|\\s+";
        String[] shopArray = shops.split(regex);
        String[] productArray = products.split(regex);
        int len = Math.max(shopArray.length, productArray.length);
        List<IndexShopProduct> indexShopProducts = new ArrayList<>(len);
        for (int i = 0; i < len ;i++) {
            IndexShopProduct index = new IndexShopProduct();
            index.setCreateId(Long.valueOf(schoolId));
            index.setUpdateId(Long.valueOf(schoolId));
            index.setCreateTime(new Date());
            index.setUpdateTime(new Date());
            index.setSchoolId(Long.valueOf(schoolId));
            if (i < shopArray.length){
                index.setShopId(Long.valueOf(shopArray[i]));
            }
            if (i < productArray.length){
                index.setProductId(Long.valueOf(productArray[i]));
            }

            indexShopProducts.add(index);
        }
        if (indexShopProducts.size() < NumConstants.INT_NUM_1){
            return new ResponseObject(true,ResponseViewEnums.INDEX_ADD_ERROR_NULL);
        }
        //先删除
        QueryWrapper<IndexShopProduct> wrapper = new QueryWrapper<>();
        wrapper.eq("school_id",schoolId);
        int deleteNum = indexShopProductMapper.delete(wrapper);
        //再新增
        int addNum = indexShopProductMapper.batchInsert(indexShopProducts);
        return new ResponseObject(true,PublicErrorEnums.SUCCESS);
    }


    @Override
    public ResponseObject findIndexStringBySchoolId(Long schoolId) {
        Assertions.notNull(schoolId, ResponseViewEnums.INDEX_FIND_NO_SCHOOL);
        List<IndexShopProduct> indexShopProducts = indexShopProductMapper.findIndexShopProBySchoolId(schoolId);
        if (indexShopProducts.size() < NumConstants.INT_NUM_1){
            return new ResponseObject(true, PublicErrorEnums.SUCCESS)
                    .push("shopList",new ArrayList<>())
                    .push("productList",new ArrayList<>());
        }
        StringBuffer shopIds = new StringBuffer();
        StringBuffer productIds = new StringBuffer();
        for (IndexShopProduct index : indexShopProducts) {
            if (index.getShopId() != null){
                shopIds.append(index.getShopId());
                shopIds.append(",");
            }
            if (index.getProductId() != null){
                productIds.append(index.getProductId());
                productIds.append(",");
            }
        }
        return new ResponseObject(true, PublicErrorEnums.SUCCESS)
                .push("shopList",shopIds.toString())
                .push("productList",productIds.toString());
    }
}
