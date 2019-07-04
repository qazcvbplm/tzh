package com.service;

import ops.school.api.entity.SourceProduct;

import javax.validation.Valid;
import java.util.List;

public interface SourceProductService {

	void add(@Valid SourceProduct sourceProduct);

	List<SourceProduct> find(SourceProduct sourceProduct);

	int update(SourceProduct sourceProduct);

}
