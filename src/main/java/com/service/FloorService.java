package com.service;


import ops.school.api.entity.Floor;

import java.util.List;

public interface FloorService {

	void add(Floor floor);

	List<Floor> find(Floor floor);

	int update(Floor floor);


}
