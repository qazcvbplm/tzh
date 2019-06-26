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
@RequestMapping("/ops/txlog")
public class TxLogController {

	@Autowired
	private TxLogMapper txLogMapper;


    @ApiOperation(value = "查询", httpMethod = "POST")
	@PostMapping("/find")
	public ResponseObject find(String appId,String schoolId,int page,int size){
		if (appId == null && schoolId == null) {
			return new ResponseObject(false, "");
		}
		QueryWrapper<TxLog> query=new QueryWrapper<>();
        query.select("id", "txer_id", "type", "create_time", "amount");
		if(schoolId!=null)
			query.lambda().eq(TxLog::getSchoolId, schoolId);
		if(appId!=null)
			query.lambda().eq(TxLog::getAppId, appId);
		query.lambda().orderByDesc(TxLog::getCreateTime);
		IPage<TxLog> rs=txLogMapper.selectPage(new Page<>(page, size), query);
		return new ResponseObject(true, "ok").push("list", rs.getRecords()).push("total", rs.getTotal());
	}

	@ApiOperation(value = "按照配送员查询", httpMethod = "GET")
	@GetMapping("/sender/find")
	public ResponseObject find(Integer id, int page, int size) {
		if (id == null || id <= 0) {
			return new ResponseObject(false, "");
		}
		QueryWrapper<TxLog> query = new QueryWrapper<>();
		query.lambda().eq(TxLog::getTxerId, id);
		query.lambda().eq(TxLog::getType, "配送员提现");
		query.lambda().eq(TxLog::getIshow, "0");
		query.select("id", "txer_id", "type", "create_time", "amount");
		query.lambda().orderByDesc(TxLog::getCreateTime);
		IPage<TxLog> rs = txLogMapper.selectPage(new Page<>(page, size), query);
		return new ResponseObject(true, "ok").push("list", rs.getRecords()).push("total", rs.getTotal());
	}
}
