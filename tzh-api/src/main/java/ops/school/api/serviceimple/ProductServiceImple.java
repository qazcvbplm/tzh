package ops.school.api.serviceimple;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ops.school.api.dao.ProductAttributeMapper;
import ops.school.api.dao.ProductMapper;
import ops.school.api.entity.Product;
import ops.school.api.exception.YWException;
import ops.school.api.service.ProductService;
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


}
