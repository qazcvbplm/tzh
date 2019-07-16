package ops.school.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ops.school.api.entity.SecondHand;
import ops.school.api.service.SecondHandService;
import ops.school.api.util.ResponseObject;
import ops.school.api.util.Util;
import ops.school.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@Api(tags="二手模块")
@RequestMapping("ops/secondhand")
public class SecondHandController {


    @Autowired
	private SecondHandService secondHandService;
	
	@ApiOperation(value="添加",httpMethod="POST")
	@PostMapping("add")
	public ResponseObject add(HttpServletRequest request, HttpServletResponse response, @ModelAttribute @Valid SecondHand secondHand, BindingResult result){
		              Util.checkParams(result);
		secondHandService.save(secondHand);
		              return new ResponseObject(true, "添加成功");
	}
	
	
	@ApiOperation(value="查询",httpMethod="POST")
	@PostMapping("find")
	public ResponseObject find(HttpServletRequest request,HttpServletResponse response,SecondHand secondHand){
		IPage list = secondHandService.page(PageUtil.noPage(),
				new QueryWrapper<SecondHand>().setEntity(secondHand));
		return new ResponseObject(true, "ok").push("total",
				list.getTotal()).push("list", list.getRecords());
	}
	
	
	@ApiOperation(value="更新",httpMethod="POST")
	@PostMapping("update")
	public ResponseObject update(HttpServletRequest request,HttpServletResponse response,SecondHand secondHand){
		if (secondHandService.updateById(secondHand)) {
			return new ResponseObject(true, "更新成功");
		} else {
			return new ResponseObject(false, "更新失败");
		}

	}
}
