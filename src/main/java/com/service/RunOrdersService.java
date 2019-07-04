package com.service;

import ops.school.api.entity.RunOrders;

import javax.validation.Valid;
import java.util.List;

public interface RunOrdersService {

	void add(@Valid RunOrders orders);

	List<RunOrders> find(RunOrders orders);

	int count(RunOrders orders);

	RunOrders findById(String orderId);

	int paySuccess(String orderId, String string);

	int pay(RunOrders orders);

	void remove();

	int cancel(String id);

	int countBySchoolId(int schoolId);


}
