package ops.school.controller;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ops.school.api.dto.wxgzh.Message;
import ops.school.api.entity.RunOrders;
import ops.school.api.entity.School;
import ops.school.api.entity.WxUser;
import ops.school.api.service.RunOrdersService;
import ops.school.api.service.SchoolService;
import ops.school.api.service.WxUserService;
import ops.school.api.util.LoggerUtil;
import ops.school.api.util.RedisUtil;
import ops.school.api.util.ResponseObject;
import ops.school.api.util.Util;
import ops.school.api.wxutil.WXpayUtil;
import ops.school.api.wxutil.WxGUtil;
import ops.school.service.TRunOrdersService;
import ops.school.util.MapUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@Api(tags="跑腿订单模块")
@RequestMapping("ops/runorders")
public class RunOrdersController {


    @Autowired
	private RunOrdersService runOrdersService;
	@Autowired
	private SchoolService schoolService;
	@Autowired
	private TRunOrdersService tRunOrdersService;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	@Autowired
	private WxUserService wxUserService;
	
	@ApiOperation(value="添加",httpMethod="POST")
	@PostMapping("add")
    public ResponseObject add(HttpServletRequest request, HttpServletResponse response,
                              @ModelAttribute @Valid RunOrders orders, BindingResult result) {
        Util.checkParams(result);
        orders.init();
        orders.setOpenId(request.getAttribute("Id").toString());
        School school = schoolService.findById(orders.getSchoolId());
        orders.setAppId(school.getAppId());
        //一、获取当前系统时间和日期并格式化输出:
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String dateTime = df.format(new Date()); // Formats a Date into a date/time string.
        System.out.println(dateTime);  // 2017-09-24 23:33:20
        orders.setCreateTime(dateTime);
        runOrdersService.add(orders);
        return new ResponseObject(true, orders.getId());
    }
	
	@ApiOperation(value="查询",httpMethod="POST")
	@PostMapping("find")
	public ResponseObject find(HttpServletRequest request,HttpServletResponse response,
			RunOrders orders){
		    List<RunOrders> list=runOrdersService.find(orders);
		    return new ResponseObject(true, "ok").push("list", list).push("total", runOrdersService.count(orders));
	}
	
	@ApiOperation(value="支付订单",httpMethod="POST")
	@PostMapping("pay")
	public ResponseObject pay(HttpServletRequest request,HttpServletResponse response,
			String orderId,String payment,String formid){
		 RunOrders orders=runOrdersService.findById(orderId);
		 WxUser wxUser = wxUserService.findById(orders.getOpenId());
		 if(payment.equals("微信支付")){
			 School school=schoolService.findById(orders.getSchoolId());
			  Object msg= WXpayUtil.payrequest(school.getWxAppId(), school.getMchId(), school.getWxPayId(),
					  "椰子-w", orders.getId(),orders.getTotalPrice().multiply(new BigDecimal(100)).intValue()+"", orders.getOpenId(),
					  request.getRemoteAddr(), "", OrdersNotify.URL+"notify/run");
			 Map<String,Object> map = (Map<String, Object>) msg;
			 if("SUCCESS".equals(map.get("return_code"))){
				 String[] formIds = formid.split(",");
				 if (formIds.length < 1){
					 LoggerUtil.logError("runOrder pay formid为空"+ orders.getId());
				 }
				 stringRedisTemplate.boundListOps("FORMID" + orders.getId()).leftPushAll(formIds);
				 stringRedisTemplate.boundListOps("FORMID" + orders.getId()).expire(24, TimeUnit.HOURS);
			 }
			  return new ResponseObject(true, "ok").push("msg", msg);
		 } 
		 if(payment.equals("余额支付")){
			 if (tRunOrdersService.pay(orders,formid) == 1) {
				 Map<String,Object> map=new HashMap<>();
				 map.put("schoolId", orders.getSchoolId());
				 map.put("amount", orders.getTotalPrice());
				 schoolService.chargeUse(map);
				 stringRedisTemplate.delete("SCHOOL_ID_" + orders.getId());
				 //todo 不能判断支付状态是void 这里redis存是在pay的方法里面
			 }
			 return new ResponseObject(true, orderId);
		 }
		 return null;
	}
	
	
	@ApiOperation(value="取消订单",httpMethod="POST")
	@PostMapping("cancel")
	public ResponseObject find(HttpServletRequest request,HttpServletResponse response,
			String id){
		    int i=tRunOrdersService.cancel(id);
		    if(i==1){
		    	return new ResponseObject(true, "ok");
		    }else if(i==2){
		    	return new ResponseObject(false, "5分钟后才可取消");
		    }
		    else
		    {
		    	return new ResponseObject(false, "请重试");
		    }
	}
}
