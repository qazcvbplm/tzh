package ops.school.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ops.school.api.entity.DayLogTakeout;
import ops.school.api.service.DayLogTakeoutService;
import ops.school.api.util.ResponseObject;
import ops.school.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@Api(tags = "店铺日营业额统计")
@RequestMapping("ops/daylogtakeout")
public class DayLogController {


    @Autowired
    private DayLogTakeoutService dayLogTakeoutService;

    @ApiOperation(value = "查询", httpMethod = "POST")
    @PostMapping("find")
    public ResponseObject find(HttpServletRequest request, HttpServletResponse response,
                               String selfId, String day, String type, Integer parentId, int page, int size) {
        QueryWrapper<DayLogTakeout> query = new QueryWrapper<>();
        if (parentId != null)
            query.lambda().eq(DayLogTakeout::getParentId, parentId);
        if (selfId != null)
            query.lambda().eq(DayLogTakeout::getSelfId, selfId);
        if (type != null)
            query.lambda().eq(DayLogTakeout::getType, type);
        if (day != null)
            query.lambda().eq(DayLogTakeout::getDay, day);
        query.lambda().orderByDesc(DayLogTakeout::getDay);
        IPage<DayLogTakeout> list = dayLogTakeoutService.page(PageUtil.getPage(page, size), query);
        return new ResponseObject(true, "ok").push("total", list.getTotal()).push("list", list.getRecords());
    }
}
