package ops.school.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ops.school.api.dto.SenderTj;
import ops.school.api.entity.Orders;
import ops.school.api.entity.RunOrders;
import ops.school.api.entity.Sender;
import ops.school.api.service.SenderService;
import ops.school.api.util.ResponseObject;
import ops.school.api.util.Util;
import ops.school.service.TSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@Api(tags = "配送员模块")
@RequestMapping("ops/sender")
public class SenderController {

    @Autowired
    private SenderService senderService;
    @Autowired
    private TSenderService tSenderService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @ApiOperation(value = "添加", httpMethod = "POST")
    @PostMapping("add")
    public ResponseObject add(HttpServletRequest request, HttpServletResponse response, @ModelAttribute @Valid Sender sender, BindingResult result) {
        Util.checkParams(result);
        sender.setOpenId(request.getAttribute("Id").toString());
        senderService.add(sender);
        return new ResponseObject(true, "添加成功");
    }

    @ApiOperation(value = "查询", httpMethod = "POST")
    @PostMapping("find")
    public ResponseObject find(HttpServletRequest request, HttpServletResponse response, Sender sender) {
        sender.setQueryType(request.getAttribute("role").toString());
        sender.setQuery(request.getAttribute("Id").toString());
        List<Sender> list = senderService.find(sender);
        return new ResponseObject(true, "ok").push("list", list).push("total", senderService.count(sender));
    }

    @ApiOperation(value = "查询待审核人数", httpMethod = "POST")
    @PostMapping("finddsh")
    public ResponseObject finddsh(HttpServletRequest request, HttpServletResponse response, int
            schoolId) {
        int count = senderService.finddsh(schoolId);
        return new ResponseObject(true, "ok").push("total", count);
    }


    @ApiOperation(value = "更新", httpMethod = "POST")
    @PostMapping("update")
    public ResponseObject update(HttpServletRequest request, HttpServletResponse response, Sender sender) {
        int i = senderService.update(sender);
        return new ResponseObject(true, "");
    }


    @ApiOperation(value = "按手机号查询", httpMethod = "POST")
    @PostMapping("nocheck/findbyphone")
    public ResponseObject findbyphone(HttpServletRequest request, HttpServletResponse response, String phone) {
        Sender sender = senderService.findByPhone(phone);
        return new ResponseObject(true, "ok").push("sender", sender);
    }

    @ApiOperation(value = "配送员查询外卖订单", httpMethod = "POST")
    @PostMapping("nocheck/findorderbytakeout")
    public ResponseObject findorderbydjs(HttpServletRequest request, HttpServletResponse response, int senderId, int page, int size, String status) {
        List<Orders> list = tSenderService.findorderbydjs(senderId, page, size, status);
        return new ResponseObject(true, "ok").push("list", list);
    }

    @ApiOperation(value = "接手订单", httpMethod = "POST")
    @PostMapping("nocheck/senderaccept")
    public ResponseObject senderaccept(HttpServletRequest request, HttpServletResponse response, int senderId, String orderId) {
        int result = tSenderService.acceptOrder(senderId, orderId);
        if (result == 1){
            // 从所有商家已接手订单中删除
            stringRedisTemplate.boundHashOps("SHOP_YJS").delete(orderId);
            return new ResponseObject(true, "接手成功");
        }
        else {
            return new ResponseObject(false, "手慢了");
        }
    }

    @ApiOperation(value = "取件", httpMethod = "POST")
    @PostMapping("nocheck/senderget")
    public ResponseObject senderget(HttpServletRequest request, HttpServletResponse response, String orderId) {
        int result = tSenderService.sendergetorder(orderId);
        return new ResponseObject(true, "取件成功");
    }

    @ApiOperation(value = "送达订单", httpMethod = "POST")
    @PostMapping("nocheck/senderend")
    public ResponseObject senderend(HttpServletRequest request, HttpServletResponse response,
                                    @RequestParam String orderId, @RequestParam boolean end, @RequestParam int senderId) {
        tSenderService.end(orderId, end);
        return new ResponseObject(true, "送达完成");
    }


    @ApiOperation(value = "配送员查询跑腿订单", httpMethod = "POST")
    @PostMapping("nocheck/findorderbyrun")
    public ResponseObject findorderbyrun(HttpServletRequest request, HttpServletResponse response, int senderId, int page, int size, String status) {
        List<RunOrders> list = tSenderService.findorderbyrundjs(senderId, page, size, status);
        return new ResponseObject(true, "ok").push("list", list);
    }

    @ApiOperation(value = "接手跑腿订单", httpMethod = "POST")
    @PostMapping("nocheck/senderacceptrun")
    public ResponseObject senderacceptrun(HttpServletRequest request, HttpServletResponse response, int senderId, String orderId) {
        int result = tSenderService.acceptOrderRun(senderId, orderId);
        if (result == 1)
            return new ResponseObject(true, "接手成功");
        else
            return new ResponseObject(false, "手慢了");
    }

    @ApiOperation(value = "送达跑腿订单", httpMethod = "POST")
    @PostMapping("nocheck/senderendrun")
    public ResponseObject senderend(HttpServletRequest request, HttpServletResponse response,
                                    @RequestParam String orderId) {
        tSenderService.endRun(orderId);
        return new ResponseObject(true, "送达完成");
    }


    @ApiOperation(value = "配送员统计", httpMethod = "POST")
    @PostMapping("nocheck/senderstatistics")
    public ResponseObject senderstatistics(HttpServletRequest request, HttpServletResponse response,
                                           @RequestParam Integer senderId, @RequestParam String beginTime, @RequestParam String endTime) {
        SenderTj result = tSenderService.statistics(senderId, beginTime, endTime);
        return new ResponseObject(true, "ok").push("result", result);
    }

   /* @ApiOperation(value = "配送员提现", httpMethod = "POST")
    @PostMapping({"sendertx"})
    public ResponseObject sendertx(HttpServletRequest request, HttpServletResponse response, @RequestParam BigDecimal amount, @RequestParam String senderId) {
        if (amount.compareTo(new BigDecimal(1)) == -1) {
            return new ResponseObject(false, "金额至少大于1");
        }
        int result = this.senderService.tx(senderId, amount);
        if (result == 1) {
            return new ResponseObject(true, "提现成功");
        }
        return new ResponseObject(false, "提现失败");
    }*/

    @ApiOperation(value = "配送员提现2", httpMethod = "POST")
    @PostMapping({"sendertx2"})
    public ResponseObject sendertx2(HttpServletRequest request, HttpServletResponse response, @RequestParam String userId, @RequestParam String senderId) {
        int result = tSenderService.tx2(senderId, userId);
        if (result == 1) {
            return new ResponseObject(true, "提现成功");
        }
        return new ResponseObject(false, "提现失败");
    }
}
