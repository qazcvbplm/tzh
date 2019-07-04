package com.service;

import ops.school.api.entity.ProductCategory;

import javax.validation.Valid;
import java.util.List;

public interface ProductCategoryService {

	void add(@Valid ProductCategory productCategory);

	List<ProductCategory> findByShop(int shopId);

	int update(ProductCategory pc);

}
