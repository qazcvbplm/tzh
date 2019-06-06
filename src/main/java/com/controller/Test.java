package com.controller;

import com.dao.*;
import com.dto.RunOrdersTj;
import com.entity.DayLogTakeout;
import com.entity.Orders;
import com.entity.School;
import com.entity.Shop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class Test {

	@Autowired
	private SchoolMapper schoolMapper;
	@Autowired
	private ShopMapper shopMapper;
	@Autowired
	private OrdersMapper ordersService;
	@Autowired
	private RunOrdersMapper runOrdersMapper;
	@Autowired
	private DayLogTakeoutMapper dayLogTakeoutMapper;


	@GetMapping("test")
	public void test(){
		List<School> schools=schoolMapper.find(new School());
		//////////////////////////////////////////////////跑腿日志///////////////////////////////////////////////////////////
		for(School schooltemp:schools){
			List<RunOrdersTj> runOrdersTjs=runOrdersMapper.tj(schooltemp.getId());
			DayLogTakeout runLog= new DayLogTakeout("2019-06-05",schooltemp,runOrdersTjs);
			dayLogTakeoutMapper.insert(runLog);
		}
	}
}
