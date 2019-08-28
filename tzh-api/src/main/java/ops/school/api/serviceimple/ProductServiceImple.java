package ops.school.api.serviceimple;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ops.school.api.constants.NumConstants;
import ops.school.api.dao.ProductAttributeMapper;
import ops.school.api.dao.ProductMapper;
import ops.school.api.dto.project.ProductAndAttributeDTO;
import ops.school.api.dto.project.ProductOrderDTO;
import ops.school.api.entity.Product;
import ops.school.api.entity.Shop;
import ops.school.api.enums.PublicErrorEnums;
import ops.school.api.enums.ResponseViewEnums;
import ops.school.api.exception.Assertions;
import ops.school.api.exception.YWException;
import ops.school.api.service.ProductService;
import ops.school.api.util.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImple extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductAttributeMapper productAttributeMapper;


    @Override
    public List<Product> findByCategoryId(int productCategoryId) {
        return productMapper.findByCategoryId(productCategoryId);
    }


    @Override
    public List<Product> findByCategoryId_wxUser(int productCategoryId) {
        return productMapper.findByCategoryId_wxUser(productCategoryId);
    }

    @Override
    public List<Product> findByShopAllDiscount(int productCategoryId) {
        return productMapper.findByShopAllDiscount(productCategoryId);
    }



    @Transactional
    @Override
    public void sale(List<Integer> pids, List<Integer> counts) {
        int size = pids.size();
        //List<Product> ps=productMapper.findin(pids);
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < size; i++) {
            Product temp = productMapper.selectByPrimaryKey(pids.get(i));
            map.put("id", temp.getId());
            map.put("count", counts.get(i));
            if (temp.getStockFlag() == 1) {
                if (productMapper.stock(map) != 1) {
                    throw new YWException("库存不足");
                }
            }
        }

    }

    @Override
    public Integer sales(Map<String, Object> map) {
        productMapper.sale(map);
        return 1;
    }

    /**
     * @date:   2019/7/20 16:31
     * @author: QinDaoFang
     * @version:version
     * @return: java.util.List<ops.school.api.dto.project.ProductAndAttributeDTO>
     * @param   pIdAndAIdMap
     * @Desc:   desc 根据商品id和商品规格id批量查询商品及规格
     */
    @Override
    public List<ProductAndAttributeDTO> batchFindProdAttributeByIdS(Map<Long,Long> pIdAndAIdMap) {
        Assertions.notEmpty(pIdAndAIdMap, PublicErrorEnums.PULBIC_EMPTY_PARAM);
        List<ProductAndAttributeDTO> productAndAttributes = productMapper.batchFindProdAttributeByIdS(pIdAndAIdMap);
        return productAndAttributes;
    }

    /**
     * @date:   2019/8/28 22:58
     * @author: QinDaoFang
     * @version:version
     * @return: ops.school.api.util.ResponseObject
     * @param   product
     * @Desc:   desc
     */
    @Override
    public ResponseObject getAllNeedChooseAndNoDelPros(Product product) {
        Assertions.notNull(product, ResponseViewEnums.SHOP_NEED_TO_ID);
        Assertions.notNull(product.getShopId(),ResponseViewEnums.SHOP_NEED_TO_ID);
        QueryWrapper<Product> wrapper = new QueryWrapper<>(product);
        wrapper.eq("shop_id",product.getShopId());
        wrapper.eq("is_delete", NumConstants.DB_TABLE_IS_DELETE_NO);
        List<Product> productList = productMapper.selectList(wrapper);
        return new ResponseObject(true,ResponseViewEnums.SUCCESS).push("list",productList);
    }
}
