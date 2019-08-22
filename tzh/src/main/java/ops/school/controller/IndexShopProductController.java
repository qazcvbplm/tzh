package ops.school.controller;

import io.swagger.annotations.ApiOperation;
import ops.school.api.entity.IndexShopProduct;
import ops.school.api.entity.Notice;
import ops.school.api.enums.PublicErrorEnums;
import ops.school.api.enums.ResponseViewEnums;
import ops.school.api.exception.Assertions;
import ops.school.api.service.IndexShopProductService;
import ops.school.api.util.ResponseObject;
import ops.school.api.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * CreatebyFang
 * fangfor@outlook.com
 * 2019/8/22
 * 15:04
 * #
 */

@Controller
@RequestMapping("/ops/index")
public class IndexShopProductController {

    @Autowired
    private IndexShopProductService indexShopProductService;

    @ApiOperation(value="首页查询",httpMethod="POST")
    @RequestMapping(value = "/find",method = RequestMethod.POST)
    @ResponseBody
    public ResponseObject findIndexShopProBySchoolId(Long schoolId){
        Assertions.notNull(schoolId, ResponseViewEnums.INDEX_FIND_NO_SCHOOL);
        ResponseObject responseObject = indexShopProductService.findIndexShopProBySchoolId(schoolId);
        return responseObject;
    }

    @ApiOperation(value="添加",httpMethod="POST")
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @ResponseBody
    public ResponseObject addIndexShopProduct(String shops,String products,Integer schoolId){
        Assertions.notNull(schoolId, ResponseViewEnums.INDEX_ADD_NO_SCHOOL);
        ResponseObject responseObject = indexShopProductService.addIndexShopProduct(shops,products,schoolId);
        return responseObject;
    }

    @ApiOperation(value="后台查询",httpMethod="POST")
    @RequestMapping(value = "/string",method = RequestMethod.POST)
    @ResponseBody
    public ResponseObject findIndexStringBySchoolId(Long schoolId){
        Assertions.notNull(schoolId, ResponseViewEnums.INDEX_FIND_NO_SCHOOL);
        ResponseObject responseObject = indexShopProductService.findIndexStringBySchoolId(schoolId);
        return responseObject;
    }
}
