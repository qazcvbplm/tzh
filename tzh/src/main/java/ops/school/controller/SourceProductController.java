package ops.school.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ops.school.api.entity.SourceProduct;
import ops.school.api.service.SourceProductService;
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
@Api(tags="积分商品模块")
@RequestMapping("ops/source/product")
public class SourceProductController {
	
	@Autowired
	private SourceProductService sourceProductService;


    @ApiOperation(value = "添加", httpMethod = "POST")
	@PostMapping("add")
	public ResponseObject add(HttpServletRequest request, HttpServletResponse response,
							  @ModelAttribute @Valid SourceProduct sourceProduct, BindingResult result){
		              Util.checkParams(result);
        sourceProductService.save(sourceProduct);
		              return new ResponseObject(true, "添加成功");
	}
	
	@ApiOperation(value="查询",httpMethod="POST")
	@PostMapping("find")
	public ResponseObject find(HttpServletRequest request,HttpServletResponse response,SourceProduct sourceProduct){
		              List<SourceProduct> list = sourceProductService.find(sourceProduct);
		              QueryWrapper<SourceProduct> queryWrapper = new QueryWrapper<>();
		              Integer total = sourceProductService.count(queryWrapper
							  .lambda().eq(SourceProduct::getSchoolId,sourceProduct.getSchoolId()));
		              return new ResponseObject(true, "ok").push("list", list).push("total",total);
	}
	
	
	@ApiOperation(value="更新",httpMethod="POST")
	@PostMapping("update")
	public ResponseObject update(HttpServletRequest request,HttpServletResponse response,SourceProduct sourceProduct){
        return new ResponseObject(sourceProductService.updateById(sourceProduct), "");
	}
}
