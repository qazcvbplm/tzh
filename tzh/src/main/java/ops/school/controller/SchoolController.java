package ops.school.controller;

import com.github.qcloudsms.httpclient.HTTPException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ops.school.api.auth.JWTUtil;
import ops.school.api.dto.project.CouponDTO;
import ops.school.api.entity.Application;
import ops.school.api.entity.School;
import ops.school.api.service.ApplicationService;
import ops.school.api.service.SchoolService;
import ops.school.api.util.BaiduUtil;
import ops.school.api.util.ResponseObject;
import ops.school.api.util.Util;
import ops.school.api.constants.CouponConstants;
import ops.school.api.constants.NumConstants;
import ops.school.api.exception.Assertions;
import ops.school.service.TCouponService;
import ops.school.service.TSchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@Api(tags = "学校模块")
@RequestMapping("ops/school")
public class SchoolController {
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private TSchoolService tSchoolService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private TCouponService tCouponService;

    @ApiOperation(value = "添加", httpMethod = "POST")
    @PostMapping("add")
    public ResponseObject add(HttpServletRequest request, HttpServletResponse response, @ModelAttribute @Valid School school, BindingResult result) {
        Util.checkParams(result);
        try {
            schoolService.add(school);
        } catch (Exception e) {
            return new ResponseObject(false, e.getMessage());
        }
        return new ResponseObject(true, "添加成功");
    }

    @ApiOperation(value = "查询", httpMethod = "POST")
    @PostMapping("find")
    public ResponseObject find(HttpServletRequest request, HttpServletResponse response, School school) {
        List<School> list = schoolService.find(school);
        return new ResponseObject(true, "ok").push("list", list);
    }

    @ApiOperation(value = "更新", httpMethod = "POST")
    @PostMapping("update")
    public ResponseObject update(HttpServletRequest request, HttpServletResponse response, School school) {
        if (school.getLoginPassWord() != null) {
            school.setLoginPassWord(Util.EnCode(school.getLoginPassWord()));
        }
        int i = schoolService.update(school);
        stringRedisTemplate.delete("SCHOOL_ID_" + school.getId());
        return new ResponseObject(true, "更新" + i + "条记录");
    }

    @ApiOperation(value = "登录", httpMethod = "POST")
    @PostMapping("login")
    public ResponseObject login(HttpServletRequest request, HttpServletResponse response, String loginName, String loginPass, int type) {
        String token = "";
        if (type == 1) {
            School school = schoolService.login(loginName, Util.EnCode(loginPass));
            if (school != null) {
                token = JWTUtil.sign(school.getId() + "", school.getLoginName(), "ops");
                return new ResponseObject(true, "ok")
                        .push("token", token)
                        .push("school", school)
                        .push("type", "ops");
            } else {
                return new ResponseObject(false, "账号或密码错误");
            }
        } else {
            Application login = new Application();
            login.setLoginName(loginName);
            // Base64加密
            login.setLoginPass(Util.EnCode(loginPass));
            Application application = applicationService.login(login.getLoginName(), login.getLoginPass());
            if (application != null) {
                token = JWTUtil.sign(application.getId() + "", application.getLoginName(), "admin");
                return new ResponseObject(true, "ok").push("token", token).push("admin", application).push("type", "admin");
            } else {
                return new ResponseObject(false, "账号或密码错误");
            }
        }
    }


    @ApiOperation(value = "计算额外距离", httpMethod = "POST")
    @PostMapping("extra_send_price")
    public BigDecimal extra_send_price(HttpServletRequest request, HttpServletResponse response, int schoolId, String origin, String des) {
        int distance = 0;
        if (stringRedisTemplate.boundHashOps("extra_send_price").get(origin + "," + des) != null) {
            distance = Integer.valueOf(stringRedisTemplate.boundHashOps("extra_send_price").get(origin + "," + des).toString());
        } else {
            distance = BaiduUtil.DistanceAll(origin, des);
            stringRedisTemplate.boundHashOps("extra_send_price").put(origin + "," + des, distance + "");
        }
        School school = schoolService.findById(schoolId);
        if (distance > school.getSendMaxDistance()) {
            int per = ((distance - school.getSendMaxDistance()) / school.getSendPerOut()) + 1;
            BigDecimal rs = new BigDecimal(per).multiply(school.getSendPerMoney());
            return rs;
        } else {
            return new BigDecimal(0);
        }
    }

    @ApiOperation(value = "代理提现", httpMethod = "POST")
    @PostMapping("tx")
    public ResponseObject tx(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam String openId,
                             @RequestParam int schoolId,
                             @RequestParam BigDecimal amount,
                             @RequestParam String codes) {
        School school = schoolService.findById(schoolId);
        String cache = stringRedisTemplate.opsForValue().get(schoolId + school.getPhone());
        String res = "";
        if (cache != null && cache.equals(codes)) {
            stringRedisTemplate.delete(schoolId + school.getPhone());
            res = tSchoolService.tx(schoolId, amount, openId);
        } else {
            return new ResponseObject(false, "验证码错误或者失效");
        }
        return new ResponseObject(true, res);
    }

    @ApiOperation(value = "获取验证码", httpMethod = "POST")
    @PostMapping("getcode")
    public ResponseObject getcode(HttpServletRequest request, HttpServletResponse response, int schoolId) {
        School school = schoolService.findById(schoolId);
        StringBuilder codes = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            codes.append((int) (Math.random() * 9));
        }
        try {
            Util.qqsms(1400169549, "0eb188f83ef4b2dc8976b5e76c70581e", school.getPhone(), 244026, codes.toString(), null);
            stringRedisTemplate.opsForValue().set(schoolId + school.getPhone(), codes.toString());
            stringRedisTemplate.expire(schoolId + school.getPhone(), 3, TimeUnit.MINUTES);
        } catch (HTTPException | IOException | org.json.JSONException e) {
            return new ResponseObject(false, "发送失败");
        }
        return new ResponseObject(true, "验证码3分钟有效哦！亲");
    }

    /**
     * @param couponDTO 包括schoolid，ids，createid
     * @date: 2019/7/18 14:16
     * @author: QinDaoFang
     * @version:version
     * @return: ops.school.api.util.ResponseObject
     * @Desc: desc 首页优惠券绑定
     */
    @ApiOperation(value = "首页优惠券绑定", httpMethod = "POST")
    @RequestMapping(value = "bindCoupons", method = RequestMethod.POST)
    public ResponseObject bindHomeCoupons(@RequestBody CouponDTO couponDTO) {
        Assertions.notNull(couponDTO);
        Assertions.notEmpty(couponDTO.getCouponList());
        couponDTO.setCreateId(NumConstants.Long_NUM_0);
        couponDTO.setUpdateId(NumConstants.Long_NUM_0);
        couponDTO.setYesShowIndex(CouponConstants.COUPON_YES_SHOW_INDEX);
        ResponseObject responseObject = tCouponService.bindHomeCouponsBySIdAndIds(couponDTO);
        return responseObject;
    }
}
