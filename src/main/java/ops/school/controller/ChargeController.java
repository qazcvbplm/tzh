package ops.school.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ops.school.api.entity.Charge;
import ops.school.api.service.ChargeLogService;
import ops.school.api.service.ChargeService;
import ops.school.api.util.ResponseObject;
import ops.school.api.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@Api(tags="充值项模块")
@RequestMapping("ops/charge")
public class ChargeController {


    @Autowired
	private ChargeService chargeService;
	@Autowired
	private ChargeLogService chargeLogService;
	
	@ApiOperation(value="添加",httpMethod="POST")
	@PostMapping("add")
	public ResponseObject add(HttpServletRequest request, HttpServletResponse response, @ModelAttribute @Valid Charge charge, BindingResult result){
		              Util.checkParams(result);
		chargeService.save(charge);
		              return new ResponseObject(true, "添加成功");
	}
	
	
	@ApiOperation(value="查询",httpMethod="POST")
	@PostMapping("find")
	public ResponseObject add(HttpServletRequest request,HttpServletResponse response){
		List<Charge> list = chargeService.list();
		              return new ResponseObject(true, "ok").push("list", list);
	}
	
	@ApiOperation(value="删除",httpMethod="POST")
	@PostMapping("remove")
	public ResponseObject add(HttpServletRequest request, HttpServletResponse response, int id) {
		if (chargeService.removeById(id)) {
			return new ResponseObject(true, "移除成功");
		} else {
			return new ResponseObject(true, "移除失败");
		}

	}
	
	/*@ApiOperation(value="按照appid统计",httpMethod="POST")
	@PostMapping("tj")
	public ResponseObject tj(HttpServletRequest request,HttpServletResponse response,int appId){
		              ChargeLog rs=chargeLogMapper.tj(appId);
		              return new ResponseObject(true, "ok").push("charge", rs.getPay()).push("send", rs.getSend()).push("surplus",chargeLogMapper.surplus(appId));
	}*/
	
	
	
}
