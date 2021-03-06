package ops.school.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ops.school.api.entity.ShopCategory;
import ops.school.api.service.ShopCategoryService;
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
@Api(tags = "店铺分类模块")
@RequestMapping("ops/shopcategory")
public class ShopCategoryController {

    @Autowired
    private ShopCategoryService shopCategoryService;

    @ApiOperation(value = "添加", httpMethod = "POST")
    @PostMapping("add")
    public ResponseObject add(HttpServletRequest request, HttpServletResponse response, @ModelAttribute @Valid ShopCategory shopCategory, BindingResult result) {
        Util.checkParams(result);
        shopCategory.setSchoolId(Integer.valueOf(request.getAttribute("Id").toString()));
        shopCategoryService.add(shopCategory);
        return new ResponseObject(true, "添加成功");
    }

    @ApiOperation(value = "查询", httpMethod = "POST")
    @RequestMapping("find")
    public ResponseObject find(HttpServletRequest request, HttpServletResponse response, ShopCategory shopCategory) {
        shopCategory.setPage((shopCategory.getPage()-1) * shopCategory.getSize());
        List<ShopCategory> list = shopCategoryService.find(shopCategory);
        return new ResponseObject(true, "ok").push("list", list);
    }

    @ApiOperation(value = "更新", httpMethod = "POST")
    @PostMapping("update")
    public ResponseObject update(HttpServletRequest request, HttpServletResponse response, ShopCategory shopCategory) {
        if (shopCategoryService.updateById(shopCategory)) {
            return new ResponseObject(true, "更新成功");
        } else {
            return new ResponseObject(false, "更新失败");
        }

    }
}
