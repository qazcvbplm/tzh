package com.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dao.TxLogMapper;
import com.entity.TxLog;
import com.util.ResponseObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags="提现记录模块")
@RequestMapping("ops/txlog")
public class TxLogController {

	@Autowired
	private TxLogMapper txLogMapper;


    @ApiOperation(value = "查询", httpMethod = "POST")
	@PostMapping("find")
	public ResponseObject find(String appId,String schoolId,int page,int size){
		QueryWrapper<TxLog> query=new QueryWrapper<>();
		if(schoolId!=null)
		query.eq("school_id", schoolId);
		if(appId!=null)
		query.eq("app_id", appId);
		query.orderByDesc("create_time");
		IPage<TxLog> rs=txLogMapper.selectPage(new Page<>(page, size), query);
		return new ResponseObject(true, "ok").push("list", rs.getRecords()).push("total", rs.getTotal());
	}

	@ApiOperation(value = "按照配送员查询", httpMethod = "GET")
	@GetMapping("find")
	public ResponseObject find(Integer id, int page, int size) {
		QueryWrapper<TxLog> query = new QueryWrapper<>();
		query.eq("txer_id", id);
		query.eq("type", "配送员提现");
		query.eq("show", "0");
		query.select("id", "txer_id", "type", "create_time", "amount");
		query.orderByDesc("create_time");
		IPage<TxLog> rs = txLogMapper.selectPage(new Page<>(page, size), query);
		return new ResponseObject(true, "ok").push("list", rs.getRecords()).push("total", rs.getTotal());
	}
}
