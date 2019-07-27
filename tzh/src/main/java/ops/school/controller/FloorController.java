package ops.school.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ops.school.api.entity.Floor;
import ops.school.api.service.FloorService;
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

@RestController
@Api(tags = "楼栋模块")
@RequestMapping("ops/floor")
public class FloorController {


    @Autowired
    private FloorService floorService;

    @ApiOperation(value = "添加", httpMethod = "POST")
    @PostMapping("add")
    public ResponseObject add(HttpServletRequest request, HttpServletResponse response, @ModelAttribute @Valid Floor floor, BindingResult result) {
        Util.checkParams(result);
        floor.setSchoolId(Integer.valueOf(request.getAttribute("Id").toString()));
        floorService.add(floor);
        return new ResponseObject(true, "添加成功");
    }


    @ApiOperation(value = "查询", httpMethod = "POST")
    @PostMapping("find")
    public ResponseObject find(HttpServletRequest request, HttpServletResponse response, Floor floor) {
        floor.setQuery(request.getAttribute("Id").toString());
        floor.setQueryType(request.getAttribute("role").toString());
        QueryWrapper<Floor> query = new QueryWrapper<Floor>();
        query.lambda().eq(Floor::getIsDelete, 0);
        switch (floor.getQueryType()) {
            case "wxuser":
                if (floor.getSchoolId() == null) {
                    query.lambda().eq(Floor::getSchoolId, Integer.valueOf(floor.getQuery()));
                }
                break;
            case "ops":
                query.lambda().eq(Floor::getSchoolId, Integer.valueOf(floor.getQuery()));
                break;
            case "admin":
                break;
        }
        query.orderByDesc("sort");
        IPage<Floor> floorIPage = floorService.page(new Page<>(floor.getPage(), floor.getSize()), query);
        return new ResponseObject(true, "ok").push("list", floorIPage.getRecords()).
                push("total", floorIPage.getTotal());
    }


    @ApiOperation(value = "更新", httpMethod = "POST")
    @PostMapping("update")
    public ResponseObject update(HttpServletRequest request, HttpServletResponse response, Floor floor) {
        if (floorService.updateById(floor)) {
            return new ResponseObject(true, "更新成功");
        } else {
            return new ResponseObject(false, "更新失败");
        }

    }
}
