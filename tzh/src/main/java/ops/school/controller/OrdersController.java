package ops.school.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ops.school.api.dto.project.ProductOrderDTO;
import ops.school.api.entity.Orders;
import ops.school.api.entity.School;
import ops.school.api.enums.PublicErrorEnums;
import ops.school.api.enums.ResponseViewEnums;
import ops.school.api.exception.Assertions;
import ops.school.api.exception.DisplayException;
import ops.school.api.service.*;
import ops.school.api.util.*;
import ops.school.api.wxutil.WXpayUtil;
import ops.school.api.constants.NumConstants;
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
import java.util.concurrent.TimeUnit;

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

    @Autowired
    private RedisUtil redisUtil;


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
        if (payment.equals("微信支付")) {
            School school = schoolService.findById(orders.getSchoolId());
            Object msg = WXpayUtil.payrequest(school.getWxAppId(), school.getMchId(), school.getWxPayId(),
                    "椰子-w", orders.getId(), orders.getPayPrice().multiply(new BigDecimal(100)).intValue() + "", orders.getOpenId(),
                    request.getRemoteAddr(), "", OrdersNotify.URL + "notify/takeout");
            HashMap<String, String> map = (HashMap<String, String>) msg;
            if (map.get("return_code").equals("SUCCESS")) {
                String[] formIds = formid.split(",");
                if (formIds.length < 1){
                    LoggerUtil.logError("order pay formid为空"+ orders.getId());
                }
                //扣除学校余额数据和粮票余额
                Integer disSCNum = schoolService.disScUserBellAllAndUserSBellByScId(BigDecimal.ZERO,orders.getPayFoodCoupon(),school.getId());
                if (disSCNum != NumConstants.INT_NUM_1){
                    DisplayException.throwMessageWithEnum(ResponseViewEnums.PAY_ERROR_SCHOOL_FAILED);
                }
                stringRedisTemplate.delete("SCHOOL_ID_" + school.getId());
                stringRedisTemplate.boundListOps("FORMID" + orders.getId()).leftPushAll(formIds);
                stringRedisTemplate.boundListOps("FORMID" + orders.getId()).expire(24, TimeUnit.HOURS);
            }
            return new ResponseObject(true, "ok").push("msg", msg);
        }
        if (payment.equals("余额支付")) {
            if (tOrdersService.pay(orders, formid) == 1) {
                //todo 不能判断支付状态是void 在pay里面存redis
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
        // 临时存储订单状态
        String status = orders.getStatus();
        int i = tOrdersService.cancel(id);
        if (i == 0){
            return new ResponseObject(false, "取消订单失败");
        }
        if (status.equals("待接手")) {
            stringRedisTemplate.boundHashOps("SHOP_DJS" + i).delete(id);
            stringRedisTemplate.boundHashOps("ALL_DJS").delete(id);
        }
        if (status.equals("商家已接手")) {
            stringRedisTemplate.boundHashOps("SHOP_YJS").delete(id);
        }
        //取消订单计入缓存
        redisUtil.cancelOrdersAdd(orders.getSchoolId());
        return new ResponseObject(true, "取消订单成功");
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
    @PostMapping("findOrdersByFloor")
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
