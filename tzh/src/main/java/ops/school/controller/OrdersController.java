package ops.school.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ops.school.api.dto.project.ProductOrderDTO;
import ops.school.api.entity.Orders;
import ops.school.api.entity.School;
import ops.school.api.entity.WxUser;
import ops.school.api.enums.PublicErrorEnums;
import ops.school.api.exception.Assertions;
import ops.school.api.service.*;
import ops.school.api.util.ResponseObject;
import ops.school.api.util.SpringUtil;
import ops.school.api.util.Util;
import ops.school.api.wxutil.WXpayUtil;
import ops.school.service.TOrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api(tags = "订单模块")
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
    @Autowired
    private WxUserService wxUserService;
    @Autowired
    private OrderProductService orderProductService;


    @ApiOperation(value = "添加", httpMethod = "POST")
    @PostMapping("add")
    public ResponseObject add(HttpServletRequest request, HttpServletResponse response, @RequestBody Orders orders) {
        // Util.checkParams(result);
        List<ProductOrderDTO> productOrderDTOS = orders.getProductOrderDTOS();
        ResponseObject responseObject = tOrdersService.addOrder2(productOrderDTOS, orders);
        return responseObject;
    }

    @ApiOperation(value = "查询", httpMethod = "POST")
    @PostMapping("find")
    public ResponseObject find(HttpServletRequest request, HttpServletResponse response,
                               Orders orders) {
        IPage<Orders> list = ordersService.find(orders);
        return new ResponseObject(true, "ok").push("list", list.getRecords())
                .push("total", list.getTotal());
    }


    @ApiOperation(value = "支付订单", httpMethod = "POST")
    @PostMapping("pay")
    public ResponseObject find(HttpServletRequest request, HttpServletResponse response,
                               String orderId, String payment, String formid) {
        Orders orders = ordersService.findById(orderId);
        WxUser wxUser = wxUserService.findById(orders.getOpenId());
       /* QueryWrapper<OrderProduct> query = new QueryWrapper<>();
        query.lambda().eq(OrderProduct::getOrderId, orders.getId());
        List<OrderProduct> list = orderProductService.list(query);
        List<OrderProduct> ops = orders.getOp();
        List<Integer> pids = new ArrayList<>();
        List<Integer> counts = new ArrayList<>();
        for (OrderProduct temp : ops) {
            pids.add(temp.getProductId());
            counts.add(temp.getProductCount());
        }
        productService.sale(pids, counts);*/
        if (payment.equals("微信支付")) {
            School school = schoolService.findById(orders.getSchoolId());
            Object msg = WXpayUtil.payrequest(school.getWxAppId(), school.getMchId(), school.getWxPayId(),
                    "椰子-w", orders.getId(), orders.getPayPrice().multiply(new BigDecimal(100)).intValue() + "", orders.getOpenId(),
                    request.getRemoteAddr(), "", OrdersNotify.URL + "notify/takeout");
            HashMap<String, String> map = (HashMap<String, String>) msg;
            if (map.get("return_code").equals("SUCCESS")) {
//				  Message message = new Message(wxUser.getOpenId(), "AFavOESyzBju1s8Wjete1SNVUvJr-YixgR67v6yMxpg"
//						  , formid, "pages/mine/payment/payment", " 微信支付成功！", orders.getWaterNumber()+"", orders.getId(),
//						  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), list.get(0).getProductName(),
//						  "如有疑问请在小程序内联系客服人员！", null, null,
//						  null, null, null);
//				  WxGUtil.snedM(message.toJson());
                stringRedisTemplate.boundListOps("FORMID" + orders.getId()).leftPushAll(formid.split(","));
            }
            return new ResponseObject(true, "ok").push("msg", msg);
        }
        if (payment.equals("余额支付")) {
            if (tOrdersService.pay(orders, formid) == 1) {
                Map<String, Object> map = new HashMap<>();
                map.put("schoolId", orders.getSchoolId());
                map.put("amount", orders.getPayPrice());
                schoolService.chargeUse(map);
            }
            return new ResponseObject(true, orderId);
        }
        return null;
    }

    @ApiOperation(value = "取消订单", httpMethod = "POST")
    @PostMapping("cancel")
    public ResponseObject find(HttpServletRequest request, HttpServletResponse response,
                               String id) {
        Orders orders = ordersService.findById(id);
        int i = tOrdersService.cancel(id);
        if (i > 2) {
            if (orders.getStatus().equals("待接手")) {
                stringRedisTemplate.boundHashOps("SHOP_DJS" + i).delete(id);
                stringRedisTemplate.boundHashOps("ALL_DJS").delete(id);
            }
            if (orders.getStatus().equals("商家已接手")) {
                stringRedisTemplate.boundHashOps("SHOP_YJS").delete(id);
            }
            return new ResponseObject(true, "ok");
        } else {
            return new ResponseObject(false, "请重试");
        }
    }

    /**
     * @param request
     * @param response
     * @param buildId
     * @param beginTime
     * @param endTime
     * @date: 2019/7/15 18:12
     * @author: QinDaoFang
     * @version:version
     * @return: ops.school.api.util.ResponseObject
     * @Desc: desc
     */
    @ApiOperation(value = "根据楼栋和时间范围查询订单等信息", httpMethod = "POST")
    @PostMapping("orders2")
    public ResponseObject countKindsOrderByBIdAndTime(HttpServletRequest request, HttpServletResponse response, @RequestParam Integer buildId, @RequestParam String beginTime, @RequestParam String endTime) {
        Assertions.notNull(buildId, PublicErrorEnums.PULBIC_EMPTY_PARAM);
        Assertions.hasText(beginTime, PublicErrorEnums.PULBIC_EMPTY_PARAM);
        Assertions.hasText(endTime, PublicErrorEnums.PULBIC_EMPTY_PARAM);
        Map result = tOrdersService.countKindsOrderByBIdAndTime(buildId, beginTime, endTime);
        return new ResponseObject(true, "ok", result);
    }


    /////////////////////////////////////////////////////////////android/////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////android/////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////android/////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////android/////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////android/////////////////////////////////////////////////////////////////

    @ApiOperation(value = "商家查询待接手订单", httpMethod = "POST")
    @PostMapping("android/findDjs")
    public ResponseObject android_findDjs(HttpServletRequest request, HttpServletResponse response, int shopId) {
        List<Orders> list;
        if (SpringUtil.redisCache()) {
            list = JSON.parseArray(stringRedisTemplate.boundHashOps("SHOP_DJS" + shopId).values().toString(), Orders.class);
        } else {
            list = ordersService.findByShopByDjs(shopId);
        }
        return new ResponseObject(true, list.size() + "")
                .push("list", JSON.toJSONString(list));
    }

    @ApiOperation(value = "商家查询订单", httpMethod = "POST")
    @PostMapping("android/findorders")
    public ResponseObject android_findorders(HttpServletRequest request, HttpServletResponse response, int shopId, int page, int size) {
        List<Orders> list = ordersService.findByShop(shopId, page, size);
        return new ResponseObject(true, list.size() + "").push("list", Util.toJson(list));
    }

    @ApiOperation(value = "商家查询已接手订单", httpMethod = "POST")
    @PostMapping("android/findordersyjs")
    public ResponseObject android_findorders2(HttpServletRequest request, HttpServletResponse response, int shopId, int page, int size) {
        List<Orders> list = ordersService.findByShopYJS(shopId, page, size);
        return new ResponseObject(true, list.size() + "").push("list", Util.toJson(list));
    }

    @ApiOperation(value = "商家接手订单", httpMethod = "POST")
    @PostMapping("android/acceptorder")
    public ResponseObject android_findDjs(HttpServletRequest request, HttpServletResponse response, String orderId) {
        int i = tOrdersService.shopAcceptOrderById(orderId);
        Orders orders = ordersService.findById(orderId);
        if (i > 0) {
            if (SpringUtil.redisCache()) {
                stringRedisTemplate.boundHashOps("SHOP_DJS" + i).delete(orderId);
                // 从所有待接手订单中删除
                stringRedisTemplate.boundHashOps("ALL_DJS").delete(orderId);
                // 新建所有商家已接手的订单缓存
                stringRedisTemplate.boundHashOps("SHOP_YJS").put(orderId, JSON.toJSONString(orders));
            }
            return new ResponseObject(true, "接手成功").push("order", Util.toJson(ordersService.findById(orderId)));
        } else {
            return new ResponseObject(false, "已经接手");
        }
    }


    /////////////////////////////////////////////////////////////android/////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////android/////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////android/////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////android/////////////////////////////////////////////////////////////////

}
