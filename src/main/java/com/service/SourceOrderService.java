package com.service;

import ops.school.api.entity.SourceOrder;

import javax.validation.Valid;
import java.util.List;

public interface SourceOrderService {

	String add(Integer id, @Valid SourceOrder sourceOrder);

	List<SourceOrder> find(SourceOrder sourceOrder);

	int update(SourceOrder sourceOrder);

	int count(SourceOrder sourceOrder);

}
