package ops.school.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ops.school.api.entity.ProductCategory;
import ops.school.api.exception.Assertions;
import ops.school.api.service.ProductCategoryService;
import ops.school.api.util.ResponseObject;
import ops.school.api.util.Util;
import ops.school.service.TProductCategoryService;
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
@Api(tags="商品分类模块")
@RequestMapping("ops/productcategory")
public class ProductCategoryController {


    @Autowired
	private ProductCategoryService productCategoryService;

    @Autowired
    private TProductCategoryService tProductCategoryService;

	@ApiOperation(value="添加",httpMethod="POST")
	@PostMapping("add")
	public ResponseObject add(HttpServletRequest request, HttpServletResponse response, @ModelAttribute @Valid ProductCategory productCategory, BindingResult result){
		              Util.checkParams(result);
		              productCategoryService.add(productCategory);
		              return new ResponseObject(true, "添加成功");
	}
	
	
	@ApiOperation(value="查询",httpMethod="POST")
    @RequestMapping("find")
	public ResponseObject find(HttpServletRequest request,HttpServletResponse response,int shopId){
		              List<ProductCategory> list = productCategoryService.findByShop(shopId);
		              return new ResponseObject(true, "ok").push("list", list);
	}
	
	@ApiOperation(value="更新",httpMethod="POST")
	@PostMapping("update")
	public ResponseObject update(HttpServletRequest request,HttpServletResponse response,ProductCategory pc){
        if (productCategoryService.updateById(pc)) {
            return new ResponseObject(true, "更新成功");
        } else {
            return new ResponseObject(false
                    , "更新失败");
        }
	}

	@ApiOperation(value="根据类目id删除类目和商品",httpMethod="POST")
	@PostMapping("delete")
	public ResponseObject deleteCategoryAndProdSById(HttpServletRequest request,HttpServletResponse response,ProductCategory pc){
		Assertions.notNull(pc,pc.getId());
		Integer categoryId = pc.getId();
		ResponseObject responseObject = tProductCategoryService.deleteCategoryAndProdSById(categoryId);
		return responseObject;
	}
}
