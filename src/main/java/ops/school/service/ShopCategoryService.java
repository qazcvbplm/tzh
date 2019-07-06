package ops.school.service;

import ops.school.api.entity.ShopCategory;

import java.util.List;

public interface ShopCategoryService {

	void add(ShopCategory shopCategory);

	List<ShopCategory> find(ShopCategory shopCategory);

	int update(ShopCategory shopCategory);

}
