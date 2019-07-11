package ops.school.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.qcloudsms.httpclient.HTTPException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ops.school.api.entity.Application;
import ops.school.api.entity.SourceOrder;
import ops.school.api.service.ApplicationService;
import ops.school.api.service.SourceOrderService;
import ops.school.api.util.ResponseObject;
import ops.school.api.util.Util;
import ops.school.service.TSourceOrderService;
import ops.school.util.PageUtil;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@Api(tags = "积分订单模块")
@RequestMapping("ops/source/order")
public class SourceOrderController {

    @Autowired
    private SourceOrderService sourceOrderService;
    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private TSourceOrderService tSourceOrderService;


    @ApiOperation(value = "积分兑换商品", httpMethod = "POST")
    @PostMapping("add")
    public ResponseObject add(HttpServletRequest request, HttpServletResponse response, @RequestParam Integer id,
                              @ModelAttribute @Valid SourceOrder sourceOrder, BindingResult result) throws JSONException, HTTPException, IOException {
        Util.checkParams(result);
        String oid = tSourceOrderService.add(id, sourceOrder);
        Application application = applicationService.getById(sourceOrder.getAppId());
        Util.qqsms(1400169549, "0eb188f83ef4b2dc8976b5e76c70581e", application.getPhone(), 317887, sourceOrder.getProductName(), null);
        return new ResponseObject(true, "兑换成功");
    }


    @ApiOperation(value = "查询", httpMethod = "POST")
    @PostMapping("find")
    public ResponseObject find(HttpServletRequest request, HttpServletResponse response, SourceOrder sourceOrder) {
        QueryWrapper<SourceOrder> query = new QueryWrapper<SourceOrder>().setEntity(sourceOrder);
        IPage<SourceOrder> list = sourceOrderService.page(PageUtil.noPage(), query);
        return new ResponseObject(true, "ok").push("total", list.getTotal())
                .push("list", list.getRecords());
    }

    @ApiOperation(value = "更新", httpMethod = "POST")
    @PostMapping("update")
    public ResponseObject update(HttpServletRequest request, HttpServletResponse response, SourceOrder sourceOrder) {
        if (sourceOrderService.updateById(sourceOrder)) {
            return new ResponseObject(true, "更新成功");
        } else {
            return new ResponseObject(false, "更新失败");
        }
    }


}
