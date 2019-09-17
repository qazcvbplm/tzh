package ops.school.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ops.school.api.entity.Product;
import ops.school.api.entity.ProductAttribute;
import ops.school.api.service.ProductAttributeService;
import ops.school.api.service.ProductService;
import ops.school.api.util.ResponseObject;
import ops.school.api.util.Util;
import ops.school.service.TProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
@Api(tags="商品模块")
@RequestMapping("ops/product")
public class ProductController {


    @Autowired
	private ProductService productService;
    @Autowired
    private ProductAttributeService productAttributeService;
    @Autowired
    private TProductService tProductService;

	@ApiOperation(value="添加",httpMethod="POST")
	@PostMapping("add")
	public ResponseObject add(HttpServletRequest request, HttpServletResponse response, BigDecimal[] attributePrice,
							  String[] attributeName, @ModelAttribute @Valid Product product, BindingResult result){
		              Util.checkParams(result);
		              if(attributePrice.length>0){
		            	  if(attributePrice.length==attributeName.length){
                              tProductService.add(attributePrice, attributeName, product);
		            		  return new ResponseObject(true, "添加成功");
		            	  }
		              }
		              return new ResponseObject(false, "添加失败");
	}
	
	
	@ApiOperation(value="查询",httpMethod="POST")
    @RequestMapping("find")
	public ResponseObject find(HttpServletRequest request,HttpServletResponse response,int productCategoryId,Integer type){
		             List<Product> list;
		              if(request.getAttribute("role").toString().equals("wxuser")){
		            	  if(type==null)
		            	     list=productService.findByCategoryId_wxUser(productCategoryId);
		            	  else
		            		  list=productService.findByShopAllDiscount(productCategoryId);
		              }else{
		            	  list=productService.findByCategoryId(productCategoryId);
		              }
		             return new ResponseObject(true, "ok").push("list", list);
	}
	
	@ApiOperation(value="更新",httpMethod="POST")
	@PostMapping("update")
	public ResponseObject update(HttpServletRequest request,HttpServletResponse response,Product product){
        if (productService.updateById(product)) {
            return new ResponseObject(true, "更新成功");
        } else {
            return new ResponseObject(true, "更新失败");
        }

	}
	
	
	@ApiOperation(value="添加规格",httpMethod="POST")
	@PostMapping("adda")
	public ResponseObject adda(HttpServletRequest request,HttpServletResponse response,
			@RequestParam int pid,@RequestParam BigDecimal attributePrice,
			@RequestParam String attributeName ){
        productAttributeService.save(new ProductAttribute(pid, attributeName, attributePrice));
		             return new ResponseObject(true, "ok");
	}
	
	@ApiOperation(value="删除规格",httpMethod="POST")
	@PostMapping("removea")
	public ResponseObject removea(HttpServletRequest request,HttpServletResponse response,
			@RequestParam int id){
        if (productAttributeService.removeById(id)) {
            return new ResponseObject(true, "删除成功");
        } else {
            return new ResponseObject(false, "删除请重试");
        }
	}
	
}
