package ops.school.controller;

import com.alibaba.fastjson.JSON;
import com.vdurmont.emoji.EmojiManager;
import com.vdurmont.emoji.EmojiParser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ops.school.api.entity.OrderProduct;
import ops.school.api.entity.Orders;
import ops.school.api.entity.School;
import ops.school.api.service.OrdersService;
import ops.school.api.service.ProductService;
import ops.school.api.service.SchoolService;
import ops.school.api.util.ResponseObject;
import ops.school.api.util.SpringUtil;
import ops.school.api.util.Util;
import ops.school.api.wxutil.WXpayUtil;
import ops.school.enums.PublicErrorEnums;
import ops.school.exception.Assertions;
import ops.school.service.TOrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api(tags="订单模块")
@RequestMapping("ops/orders")
public class OrdersController {

	@Autowired
	private OrdersService ordersService;
	@Autowired
    private TOrdersService tOrdersService;
    @Autowired
    private SchoolService schoolService;
	@Autowired
	private ProductService productService;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	@ApiOperation(value="添加",httpMethod="POST")
	@PostMapping("add")
	public ResponseObject add(HttpServletRequest request, HttpServletResponse response,
							  Integer[] productIds, Integer[] attributeIndex, Integer[] counts, @ModelAttribute @Valid Orders orders, BindingResult result){
		              Util.checkParams(result);
		              if(orders.getRemark()!=null&&EmojiManager.isEmoji(orders.getRemark())){
		            	  orders.setRemark(EmojiParser.removeAllEmojis(orders.getRemark()));
		              }
		              orders.init();
		              if((productIds.length==attributeIndex.length)&&(productIds.length==counts.length)){
		            	  if(productIds.length>0){
		            		  orders.setOpenId(request.getAttribute("Id").toString());
                              tOrdersService.addTakeout(productIds, attributeIndex, counts, orders);
		            		  return new ResponseObject(true, orders.getId());
		            	  }
		              }
		              return null;
	}
	
	@ApiOperation(value="查询",httpMethod="POST")
	@PostMapping("find")
	public ResponseObject find(HttpServletRequest request,HttpServletResponse response,
			Orders orders){
		    List<Orders> list=ordersService.find(orders);
		    return new ResponseObject(true, "ok").push("list", list).push("total", ordersService.count(orders));
	}
	
	@ApiOperation(value="支付订单",httpMethod="POST")
	@PostMapping("pay")
	public ResponseObject find(HttpServletRequest request,HttpServletResponse response,
			String orderId,String payment){
		 Orders orders=ordersService.findById(orderId);
		 List<OrderProduct> ops=orders.getOp();
		 List<Integer> pids=new ArrayList<>();
		 List<Integer> counts=new ArrayList<>();
		 for(OrderProduct temp:ops){
			pids.add(temp.getProductId());
			counts.add(temp.getProductCount());
		 }
		 productService.sale(pids,counts);
		 if(payment.equals("微信支付")){
			 School school=schoolService.findById(orders.getSchoolId());
			  Object msg= WXpayUtil.payrequest(school.getWxAppId(), school.getMchId(), school.getWxPayId(),
					  "椰子-w", orders.getId(),orders.getPayPrice().multiply(new BigDecimal(100)).intValue()+"", orders.getOpenId(),
					  request.getRemoteAddr(), "", OrdersNotify.URL+"notify/takeout");
			  return new ResponseObject(true, "ok").push("msg", msg);
		 }
		 if(payment.equals("余额支付")){
             if (tOrdersService.pay(orders) == 1) {
				 Map<String,Object> map=new HashMap<>();
				 map.put("schoolId", orders.getSchoolId());
				 map.put("amount", orders.getPayPrice());
				 schoolService.chargeUse(map);
			 }
			 return new ResponseObject(true, orderId);
		 }
		 return null;
	}
	
	
	@ApiOperation(value="取消订单",httpMethod="POST")
	@PostMapping("cancel")
	public ResponseObject find(HttpServletRequest request,HttpServletResponse response,
			String id){
        Orders orders = ordersService.findById(id);
        int i = tOrdersService.cancel(id);
        if (i > 2) {
            if (orders.getStatus().equals("待接手")) {
                if (stringRedisTemplate.boundHashOps("SHOP_DJS" + i).delete(id) <= 0 && stringRedisTemplate.boundHashOps("ALL_DJS").delete(id) <= 0) {
                    return new ResponseObject(false, "联系管理员");
                }
            } else if (orders.getStatus().equals("商家已接手")) {
                if (stringRedisTemplate.boundHashOps("SHOP_YJS").delete(id) <= 0) {
                    return new ResponseObject(false, "联系管理员");
                }
            }
            return new ResponseObject(true, "ok");
        } else if (i == 2) {
            return new ResponseObject(false, "5分钟后才能退款");
        } else {
            return new ResponseObject(false, "请重试");
        }
	}

	/**
	 * @date:   2019/7/15 18:12
	 * @author: QinDaoFang
	 * @version:version
	 * @return: ops.school.api.util.ResponseObject
	 * @param   request
	 * @param   response
	 * @param   buildId
	 * @param   beginTime
	 * @param   endTime
	 * @Desc:   desc
	 */
	@ApiOperation(value="根据楼栋和时间范围查询订单等信息",httpMethod="POST")
	@PostMapping("orders2")
	public ResponseObject countKindsOrderByBIdAndTime(HttpServletRequest request, HttpServletResponse response,@RequestParam Integer buildId, @RequestParam String beginTime, @RequestParam String endTime){
		Assertions.notNull(buildId, PublicErrorEnums.PULBIC_EMPTY_PARAM);
		Assertions.hasText(beginTime, PublicErrorEnums.PULBIC_EMPTY_PARAM);
		Assertions.hasText(endTime, PublicErrorEnums.PULBIC_EMPTY_PARAM);
		Map result = tOrdersService.countKindsOrderByBIdAndTime(buildId,beginTime,endTime);
		return new ResponseObject(true, "ok",result);
	}
	
	
	
	
	/////////////////////////////////////////////////////////////android/////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////android/////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////android/////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////android/////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////android/////////////////////////////////////////////////////////////////
	
	@ApiOperation(value="商家查询待接手订单",httpMethod="POST")
	@PostMapping("android/findDjs")
    public ResponseObject android_findDjs(HttpServletRequest request, HttpServletResponse response, int shopId) {
			 List<Orders> list;
        if (SpringUtil.redisCache()) {
				 list=JSON.parseArray(stringRedisTemplate.boundHashOps("SHOP_DJS"+shopId).values().toString(),Orders.class);
			 }else{
				list=ordersService.findByShopByDjs(shopId);
			 }
		     return new ResponseObject(true,list.size()+"")
		    		 .push("list",JSON.toJSONString(list));
	}
	
	@ApiOperation(value="商家查询订单",httpMethod="POST")
	@PostMapping("android/findorders")
	public ResponseObject android_findorders(HttpServletRequest request,HttpServletResponse response,int shopId,int page,int size){
		     List<Orders> list=ordersService.findByShop(shopId,page,size);
		     return new ResponseObject(true, list.size()+"").push("list", Util.toJson(list));
	}
	
	@ApiOperation(value="商家查询已接手订单",httpMethod="POST")
	@PostMapping("android/findordersyjs")
	public ResponseObject android_findorders2(HttpServletRequest request,HttpServletResponse response,int shopId,int page,int size){
		     List<Orders> list=ordersService.findByShopYJS(shopId,page,size);
		     return new ResponseObject(true, list.size()+"").push("list", Util.toJson(list));
	}
	
	@ApiOperation(value="商家接手订单",httpMethod="POST")
	@PostMapping("android/acceptorder")
	public ResponseObject android_findDjs(HttpServletRequest request,HttpServletResponse response,String orderId){
        int i = tOrdersService.shopAcceptOrderById(orderId);
		Orders orders = ordersService.findById(orderId);
		     if(i>0){
                 if (SpringUtil.redisCache()) {
		    		 stringRedisTemplate.boundHashOps("SHOP_DJS"+i).delete(orderId);
		    		 // 从所有待接手订单中删除
		    		 stringRedisTemplate.boundHashOps("ALL_DJS").delete(orderId);
		    		 // 新建所有商家已接手的订单缓存
		    		 stringRedisTemplate.boundHashOps("SHOP_YJS").put(orderId, JSON.toJSONString(orders));
		    	 }
		    	 return new ResponseObject(true, "接手成功").push("order",Util.toJson(ordersService.findById(orderId)));
		     }
		     else{
		    	 return new ResponseObject(false, "已经接手");
		     }
	}
	/////////////////////////////////////////////////////////////android/////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////android/////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////android/////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////android/////////////////////////////////////////////////////////////////
	
}