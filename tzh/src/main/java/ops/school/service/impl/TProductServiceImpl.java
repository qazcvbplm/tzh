package ops.school.service.impl;

import ops.school.api.entity.Product;
import ops.school.api.entity.ProductAttribute;
import ops.school.api.service.ProductAttributeService;
import ops.school.api.service.ProductService;
import ops.school.service.TProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class TProductServiceImpl implements TProductService {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductAttributeService productAttributeService;

    @Transactional
    @Override
    public void add(BigDecimal[] attributePrice, String[] attributeName, Product product) {
        productService.save(product);
        int length = attributePrice.length;
        for (int i = 0; i < length; i++) {
            ProductAttribute pa = new ProductAttribute(product.getId(), attributeName[i], attributePrice[i]);
            productAttributeService.save(pa);
        }
    }
}
