package ops.school.service;

import ops.school.api.entity.Product;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

public interface TProductService {
    @Transactional
    void add(BigDecimal[] attributePrice, String[] attributeName, Product product);
}
