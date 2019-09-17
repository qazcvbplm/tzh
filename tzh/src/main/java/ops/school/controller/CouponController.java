package ops.school.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ops.school.api.entity.Coupon;
import ops.school.api.entity.ShopCoupon;
import ops.school.api.enums.ResponseViewEnums;
import ops.school.api.exception.Assertions;
import ops.school.api.service.CouponService;
import ops.school.api.util.ResponseObject;
import ops.school.service.TCouponService;
import ops.school.service.TShopCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

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
     * @param yesShowIndex 优惠券类型
     * @return ops.school.api.util.ResponseObject
     */
    @ApiOperation(value="学校首页优惠券查询",httpMethod="POST")
    @ResponseBody
    @RequestMapping(value = "findByIndex", method = RequestMethod.POST)
    public ResponseObject find(Long schoolId, Integer yesShowIndex,Long userId) {
        Assertions.notNull(schoolId,yesShowIndex,userId);
        List<Coupon> list = tCouponService.findByIndex(schoolId,yesShowIndex,userId);
        return new ResponseObject(true, "查询成功").push("list", list);
    }

    /**
     * 通过学校id和优惠券类型查询优惠券列表
     * @author Lee
     * @return ops.school.api.util.ResponseObject
     */
    /**
     * @author: QinDaoFang
     * @date:   2019/7/25 16:02 
     * @desc:   
     */
    @ApiOperation(value="通过学校id和优惠券类型查询优惠券列表",httpMethod="POST")
    @ResponseBody
    @RequestMapping(value = "findCoupons", method = RequestMethod.POST)
    public ResponseObject findCoupons(Coupon coupon){
        Assertions.notNull(coupon,coupon.getSchoolId());
        ResponseObject responseObject = tCouponService.findCoupons(coupon.getSchoolId(),coupon.getCouponType(),coupon.getPage(),coupon.getSize());
        return responseObject;
    }

    /**
     * 新增优惠券
     * @author Lee
     * @param request 从request中获取学校id
     * @param coupon 前端数据封装成实体
     * @return ops.school.api.util.ResponseObject
     */
    @ApiOperation(value="新增优惠券",httpMethod="POST")
    @ResponseBody
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public ResponseObject add(HttpServletRequest request, Coupon coupon){
        Assertions.notNull(coupon,coupon.getSchoolId());
        coupon.setCreateId(coupon.getSchoolId());
        coupon.setUpdateId(coupon.getSchoolId());
        coupon.setCreateTime(new Date());
        coupon.setUpdateTime(new Date());
        tCouponService.insert(coupon);
        return new ResponseObject(true,"添加成功");
    }

    /**
     * 更新优惠券
     * @author Lee
     * @param coupon 封装成coupon实体
     * @return
     */
    @ApiOperation(value="更新优惠券",httpMethod="POST")
    @ResponseBody
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public ResponseObject update(Coupon coupon){
        ResponseObject responseObject = tCouponService.updateOneById(coupon);
        return responseObject;
    }



    /**
     * @date:   2019/7/27 11:56
     * @author: QinDaoFang
     * @version:version
     * @return: ops.school.api.util.ResponseObject
     * @param   coupon
     * @Desc:   desc
     */
    @ApiOperation(value="根据优惠券id删除优惠券",httpMethod="POST")
    @ResponseBody
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public ResponseObject deleteCouponAndShopByCId(Coupon coupon){
        Assertions.notNull(coupon,coupon.getId());
        ResponseObject responseObject = tCouponService.deleteCouponAndShopByCId(coupon);
        return responseObject;
    }

    /**
     * 店铺绑定优惠券
     * @author Lee
     * @param couponId 优惠券id
     * @param shopIds 需要绑定的店铺id（字符串,拼接）
     * @return
     */
    @ApiOperation(value="店铺绑定优惠券",httpMethod="POST")
    @ResponseBody
    @RequestMapping(value = "bindShop", method = RequestMethod.POST)
    public ResponseObject bindShop(@RequestParam String couponId, @RequestParam String shopIds) {
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
     * @Desc:   desc 根据用户id，学校id，店铺id，用户优惠券id，让用户获取优惠券
     */
    @ApiOperation(value="根据用户id，学校id，店铺id，用户优惠券id，让用户获取优惠券",httpMethod="POST")
    @ResponseBody
    @RequestMapping(value = "getCoupons", method = RequestMethod.POST)
    public ResponseObject userGetCoupons(Long userId,Long schoolId,Long shopId,Long couponId){
        Assertions.notNull(userId,schoolId,couponId);
        Map map = new HashMap();
        if (shopId != null){
            map.put("shopId",shopId);
        }
        map.put("userId",userId);
        map.put("couponId",couponId);
        map.put("schoolId",schoolId);
        ResponseObject responseObject = tCouponService.userGetCouponByIdMap(map);
        return  responseObject;
    }


    @ApiOperation(value="根据店铺id查询店铺发放的优惠券",httpMethod="POST")
    @ResponseBody
    @RequestMapping(value = "shopCoupons", method = RequestMethod.POST)
    public ResponseObject getAllShopCouponsByShopId(Long shopId,Integer couponType){
        Assertions.notNull(shopId,couponType);
        List<ShopCoupon> shopCouponList = tShopCouponService.getAllShopCouponsByShopId(shopId,couponType);
        return  new ResponseObject(true, ResponseViewEnums.FIND_SUCCESS)
                .push("shopCouponList",shopCouponList);
    }

}
