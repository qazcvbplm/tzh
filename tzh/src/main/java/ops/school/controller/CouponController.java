package ops.school.controller;

import io.swagger.annotations.Api;
import ops.school.api.entity.Coupon;
import ops.school.api.util.ResponseObject;
import ops.school.service.TCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@Api(tags = "优惠券模块")
@RequestMapping("ops/coupon")
public class CouponController {

    @Autowired
    private TCouponService tCouponService;

    @ResponseBody
    @RequestMapping(value = "findByIndex", method = RequestMethod.POST)
    public ResponseObject find(@RequestParam String schoolId, @RequestParam Integer couponType) {

        return new ResponseObject(true, "查询成功").push("list", tCouponService.findByIndex(Long.valueOf(schoolId), couponType));
    }

}
