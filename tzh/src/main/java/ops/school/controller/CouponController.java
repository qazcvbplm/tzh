package ops.school.controller;

import io.swagger.annotations.Api;
import ops.school.api.entity.Coupon;
import ops.school.api.exception.Assertions;
import ops.school.api.service.CouponService;
import ops.school.api.util.ResponseObject;
import ops.school.api.util.Util;
import ops.school.service.TCouponService;
import ops.school.service.TShopCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Api(tags = "优惠券模块")
@RequestMapping("ops/coupon")
public class CouponController {

    @Autowired
    private TCouponService tCouponService;
    @Autowired
    private CouponService couponService;
    @Autowired
    private TShopCouponService tShopCouponService;

    /**
     * 学校首页优惠券查询
     * @author Lee
     * @param schoolId 学校id
     * @param couponType 优惠券类型
     * @return ops.school.api.util.ResponseObject
     */
    @ResponseBody
    @RequestMapping(value = "findByIndex", method = RequestMethod.POST)
    public ResponseObject find(@RequestParam String schoolId, @RequestParam Integer couponType) {

        List<Coupon> list = tCouponService.findByIndex(Long.valueOf(schoolId),1);
        return new ResponseObject(true, "查询成功").push("list", list);
    }

    /**
     * 通过学校id和优惠券类型查询优惠券列表
     * @author Lee
     * @param schoolId 学校id
     * @param couponType 优惠券类型
     * @param page
     * @param size
     * @return ops.school.api.util.ResponseObject
     */
    @ResponseBody
    @RequestMapping(value = "findCoupons", method = RequestMethod.POST)
    public ResponseObject findCoupons(@RequestParam String schoolId, @RequestParam Integer couponType, int page, int size){

        List<Coupon> coupons = tCouponService.findCoupons(Long.valueOf(schoolId),couponType,page,size);
        Integer count = tCouponService.count(Long.valueOf(schoolId),couponType);
        return new ResponseObject(true,"查询成功").push("list",coupons).push("total",count);
    }

    /**
     * 新增优惠券
     * @author Lee
     * @param request 从request中获取学校id
     * @param coupon 前端数据封装成实体
     * @return ops.school.api.util.ResponseObject
     */
    @ResponseBody
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public ResponseObject add(HttpServletRequest request, Coupon coupon){
        tCouponService.insert(coupon);
        return new ResponseObject(true,"添加成功");
    }

    /**
     * 更新优惠券
     * @author Lee
     * @param coupon 封装成coupon实体
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public ResponseObject update(Coupon coupon){
        couponService.updateById(coupon);
        return new ResponseObject(true,"更新成功");
    }

    /**
     * 店铺绑定优惠券
     * @author Lee
     * @param couponId 优惠券id
     * @param shopIds 需要绑定的店铺id（字符串,拼接）
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "bindShop", method = RequestMethod.POST)
    public ResponseObject bindShop(@RequestParam String couponId, @RequestParam String shopIds){
        int rs = tShopCouponService.bindShopCoupon(couponId,shopIds);
        if(rs == 1){
            return new ResponseObject(true,"店铺添加优惠券成功");
        }
        return new ResponseObject(false,"店铺添加优惠券失败");
    }

    /**
     * @date:   2019/7/18 18:34
     * @author: QinDaoFang
     * @version:version
     * @return: ops.school.api.util.ResponseObject
     * @param   userId
     * @param   schoolId
     * @param   shopId
     * @param   couponId
     * @Desc:   desc 根据用户id，学校id，店铺id，优惠券id，让用户获取优惠券
     */
    @ResponseBody
    @RequestMapping(value = "getCoupons", method = RequestMethod.POST)
    public ResponseObject userGetCoupons(Long userId,Long schoolId,Long shopId,Long couponId){
        Assertions.notNull(userId,schoolId,shopId,couponId);
        Map map = new HashMap();
        map.put("userId",userId);
        map.put("shopId",shopId);
        map.put("couponId",couponId);
        map.put("schoolId",schoolId);
        ResponseObject responseObject = tCouponService.userGetCouponByIdMap(map);
        return  responseObject;
    }

}
