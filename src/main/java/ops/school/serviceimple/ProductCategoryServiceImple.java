package ops.school.serviceimple;

import ops.school.service.ProductCategoryService;
import ops.school.api.dao.ProductCategoryMapper;
import ops.school.api.entity.ProductCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;

@Service
public class ProductCategoryServiceImple implements ProductCategoryService {

	@Autowired
	private ProductCategoryMapper productCategoryMapper;

	@Override
	public void add(@Valid ProductCategory productCategory) {
		productCategory.setSort(System.currentTimeMillis());
		productCategoryMapper.insert(productCategory);
	}

	@Override
	public List<ProductCategory> findByShop(int shopId) {
		return productCategoryMapper.findByShop(shopId);
	}

	@Override
	public int update(ProductCategory pc) {
		return productCategoryMapper.updateByPrimaryKeySelective(pc);
	}
}
