package ops.school.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ops.school.api.entity.Coupon;
import ops.school.api.entity.WxUserCoupon;
import ops.school.api.enums.ResponseViewEnums;
import ops.school.api.exception.Assertions;
import ops.school.api.service.CouponService;
import ops.school.api.util.ResponseObject;
import ops.school.service.TCouponService;
import ops.school.service.TShopCouponService;
import ops.school.service.TWxUserCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * CreatebyFang
 * fangfor@outlook.com
 * 2019/7/25
 * 15:07
 * #
 */
@Api(tags = "用户优惠券模块")
@Controller
@RequestMapping("ops/user/coupon")
public class WxUserCouponController {

    @Autowired
    private TWxUserCouponService tWxUserCouponService;

    /**
     * @date:   2019/7/25 15:29
     * @author: QinDaoFang
     * @version:version
     * @return: ops.school.api.util.ResponseObject
     * @param   userId
     * @param   wxUserCoupon
     * @Desc:   desc 分页查询用户所有的优惠券（isInvalid不传查所有，传0查有效）
     */
    @ApiOperation(value="分页查询用户所有的优惠券（isInvalid不传查所有，传0查有效）",httpMethod="POST")
    @ResponseBody
    @RequestMapping(value = "page", method = RequestMethod.POST)
    public ResponseObject pageFindALLCouponsByUserId(Long userId,WxUserCoupon wxUserCoupon) {
        Assertions.notNull(userId, ResponseViewEnums.WX_USER_NEED_USER_ID);
        IPage<WxUserCoupon> resultList= tWxUserCouponService.pageFindALLCouponsByUserId(userId,wxUserCoupon);
        return new ResponseObject(true, "查询成功").push("list", resultList.getRecords()).push("total",resultList.getTotal());
    }

}


