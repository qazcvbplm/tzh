package com.serviceimple;

import com.service.ShopCategoryService;
import ops.school.api.dao.ShopCategoryMapper;
import ops.school.api.entity.ShopCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopCategoryServiceImple implements ShopCategoryService{

	@Autowired
	private ShopCategoryMapper shopCategoryMapper;

	@Override
	public void add(ShopCategory shopCategory) {
		shopCategory.setSort(System.currentTimeMillis());
		shopCategoryMapper.insert(shopCategory);
	}

	@Override
	public List<ShopCategory> find(ShopCategory shopCategory) {
		return shopCategoryMapper.find(shopCategory);
	}

	@Override
	public int update(ShopCategory shopCategory) {
		return shopCategoryMapper.updateByPrimaryKeySelective(shopCategory);
	}
	
	
}
