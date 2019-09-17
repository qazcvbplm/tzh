package ops.school.api.serviceimple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ops.school.api.dao.ProductAttributeMapper;
import ops.school.api.entity.ProductAttribute;
import ops.school.api.service.ProductAttributeService;
import org.springframework.stereotype.Service;

@Service
public class ProductAttributeServiceImpl extends ServiceImpl<ProductAttributeMapper, ProductAttribute> implements ProductAttributeService {
}
