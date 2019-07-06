package ops.school.controller;

import ops.school.service.AddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ops.school.api.entity.Address;
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
@Api(tags="收货地址模块")
@RequestMapping("ops/address")
public class AddressController {
	
	@Autowired
	private AddressService addressService;


	@ApiOperation(value="添加",httpMethod="POST")
	@PostMapping("add")
	public ResponseObject add(HttpServletRequest request, HttpServletResponse response, @ModelAttribute @Valid Address address, BindingResult result){
		              Util.checkParams(result);
		              address.setOpenId(request.getAttribute("Id").toString());
		              addressService.add(address);
		              return new ResponseObject(true, "添加成功");
	}
	
	
	@ApiOperation(value="查询",httpMethod="POST")
	@PostMapping("find")
	public ResponseObject find(HttpServletRequest request,HttpServletResponse response,Address address){
		              address.setQueryType(request.getAttribute("role").toString());
		              address.setQuery(request.getAttribute("Id").toString());
		              List<Address> list = addressService.find(address);
		              return new ResponseObject(true, "ok").push("list", list);
	}
	
	@ApiOperation(value="更新",httpMethod="POST")
	@PostMapping("update")
	public ResponseObject update(HttpServletRequest request,HttpServletResponse response,Address address){
		              int i= addressService.update(address);
		              return new ResponseObject(true, "更新"+i+"条记录").push("result", i);
	}
}
