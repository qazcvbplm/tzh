package ops.school.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.qcloudsms.httpclient.HTTPException;
import com.vdurmont.emoji.EmojiManager;
import com.vdurmont.emoji.EmojiParser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ops.school.api.auth.JWTUtil;
import ops.school.api.entity.ChargeLog;
import ops.school.api.entity.School;
import ops.school.api.entity.WxUser;
import ops.school.api.entity.WxUserBell;
import ops.school.api.enums.ResponseViewEnums;
import ops.school.api.exception.Assertions;
import ops.school.api.service.ChargeLogService;
import ops.school.api.service.SchoolService;
import ops.school.api.service.WxUserService;
import ops.school.api.util.RedisUtil;
import ops.school.api.util.ResponseObject;
import ops.school.api.util.Util;
import ops.school.api.wxutil.WXUtil;
import ops.school.api.wxutil.WxGUtil;
import ops.school.service.TWxUserCouponService;
import ops.school.service.TWxUserService;
import ops.school.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@Api(tags = "微信用户模块")
@RequestMapping("ops/user")
public class WxUserController {

    @Autowired
    private WxUserService wxUserService;
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private TWxUserService tWxUserService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private ChargeLogService chargeLogService;
    @Autowired
    private RedisUtil cache;
    @Autowired
    private TWxUserCouponService tWxUserCouponService;

    /**
     * 微信用户登录
     *
     * @param request
     * @param response
     * @param code     小程序端传来的code码，用于获取openid和sessionKey
     * @param schoolId 学校Id
     * @return
     */
    @ApiOperation(value = "微信用户登录", httpMethod = "POST")
    @GetMapping("/wx/login")
    public ResponseObject login(HttpServletRequest request, HttpServletResponse response, String code, String schoolId) {
        Integer sid;
        try {
            sid = Integer.parseInt(schoolId);
        } catch (Exception e) {
            return null;
        }
        School school = schoolService.findById(sid);
        String openid = null;
        String sessionKey = null;
        WxUser user = null;
        if (school != null) {
            Map<String, Object> map = WXUtil.wxlogin(school.getWxAppId(), school.getWxSecret(), code);
            openid = (String) map.get("openid");
            sessionKey = (String) map.get("sessionKey");
            String token = JWTUtil.sign(openid, "wx", "wxuser");
            user = wxUserService.login(openid, sid, school.getAppId(), "微信小程序");
//            WxUserBell wxUserBell = tWxUserService.getbell(openid);
            //todo 去掉login请求bell
//            if (wxUserBell == null){
//                user.setBell(new WxUserBell(NumConstants.INT_NUM_0,BigDecimal.ZERO,BigDecimal.ZERO));
//            }
            // user.setBell(wxUserBell);
            // logsMapper.insert(new Logs(request.getHeader("X-Real-IP") + "," + user.getNickName()));
            cache.userCountadd(sid);
            return new ResponseObject(true, "ok").push("token", token).push("user", user).push("sessionKey", sessionKey);
        } else {
            return new ResponseObject(false, "请选择学校");
        }
    }

    @ApiOperation(value = "获取微信用户session_key", httpMethod = "POST")
    @PostMapping("getSessionKey")
    public ResponseObject getSessionKey(HttpServletRequest request, HttpServletResponse response, String code, String schoolId) {
        Integer sid;
        try {
            sid = Integer.parseInt(schoolId);
        } catch (Exception e) {
            return null;
        }
        School school = schoolService.findById(sid);
        Map<String, Object> map = WXUtil.wxlogin(school.getWxAppId(), school.getWxSecret(), code);
        System.out.println(map);
        return new ResponseObject(true, "ok").push("map", map);
    }

    @ApiOperation(value = "获取钱包信息", httpMethod = "POST")
    @GetMapping("wx/get/bell")
    public ResponseObject getBell(HttpServletRequest request, HttpServletResponse response, String openId) {
        WxUserBell wxUserBell = tWxUserService.getbell(openId);
        return new ResponseObject(true, "ok").push("bell", wxUserBell);
    }

    @ApiOperation(value = "判断是否关注公众号", httpMethod = "POST")
    @GetMapping("wx/check/gz")
    public ResponseObject checkgz(HttpServletRequest request, HttpServletResponse response, String openId) {
        WxUser wxUser = wxUserService.findById(openId);
        WxUser wxGUser = wxUserService.findGzh(wxUser.getPhone());
        if (null == wxGUser) {
            return new ResponseObject(true, "ok").push("gz", false);
        } else {
            return new ResponseObject(true, "ok").push("gz", WxGUtil.checkGz(wxGUser.getOpenId()));
        }
    }

    @ApiOperation(value = "微信用户更新(必传id)", httpMethod = "POST")
    @PostMapping("wx/update")
    public ResponseObject update(HttpServletRequest request, HttpServletResponse response, @ModelAttribute WxUser wxUser) {
        wxUser.setOpenId(request.getAttribute("Id").toString());
        wxUser.setPhone(null);
        if (wxUser.getNickName() != null && EmojiManager.isEmoji(wxUser.getNickName())) {
            wxUser.setNickName(EmojiParser.removeAllEmojis(wxUser.getNickName()));
        }
        wxUser = wxUserService.update(wxUser);

        return new ResponseObject(true, "ok").push("user", wxUser);
    }

    @ApiOperation(value = "查询微信用户", httpMethod = "POST")
    @PostMapping("find")
    public ResponseObject find(HttpServletRequest request, HttpServletResponse response, WxUser wxUser) {
        wxUser.setQueryType(request.getAttribute("role").toString());
        wxUser.setQuery(request.getAttribute("Id").toString());
        wxUser.setPage((wxUser.getPage() - 1) * wxUser.getSize());
        List<WxUser> list = wxUserService.find(wxUser);
        wxUser.setTotal(list.size());
        return new ResponseObject(true, "ok").push("list", list).push("total", wxUserService.find(wxUser));
    }

    @ApiOperation(value = "获取验证码", httpMethod = "POST")
    @PostMapping("getcode")
    public ResponseObject getcode(HttpServletRequest request, HttpServletResponse response, @RequestParam String phone) {
        StringBuilder codes = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            codes.append((int) (Math.random() * 9));
        }
        try {
            Util.qqsms(1400169549, "0eb188f83ef4b2dc8976b5e76c70581e", phone, 244026, codes.toString(), null);
            stringRedisTemplate.opsForValue().set(phone, codes.toString(), 5, TimeUnit.MINUTES);
        } catch (HTTPException | IOException | org.json.JSONException e) {
            return new ResponseObject(false, "发送失败");
        }
        return new ResponseObject(true, "ok");
    }

    @ApiOperation(value = "绑定手机", httpMethod = "POST")
    @PostMapping("bind")
    public ResponseObject bind(HttpServletRequest request, HttpServletResponse response, @RequestParam String phone, @RequestParam String codes, Long wxId) {
        Assertions.notNull(wxId, ResponseViewEnums.WX_USER_FAILED_TO_WX);
        String code = stringRedisTemplate.opsForValue().get(phone);
        if (code != null) {
            if (codes.equals(code)) {
                String id = request.getAttribute("Id").toString();
                WxUser wxUser = new WxUser();
                wxUser.setPhone(phone);
                wxUser.setOpenId(id);
                wxUser.setId(wxId);
                stringRedisTemplate.delete(phone);
                WxUser user = wxUserService.update(wxUser);
                return new ResponseObject(true, "绑定成功").push("user", user);
            } else {
                return new ResponseObject(false, "验证码错误");
            }
        } else {
            return new ResponseObject(false, "验证码过期");
        }
    }

    @ApiOperation(value = "充值", httpMethod = "POST")
    @PostMapping("charges")
    public ResponseObject charges(HttpServletRequest request, HttpServletResponse response, int chargeId) {
        Object obj = tWxUserService.charge(request.getAttribute("Id").toString(), chargeId);
        return new ResponseObject(true, "ok").push("msg", obj);
    }

    @ApiOperation(value = "查询充值记录", httpMethod = "POST")
    @PostMapping("findcharges")
    public ResponseObject charges(HttpServletRequest request, HttpServletResponse response, String openId) {
        IPage<ChargeLog> res = chargeLogService.page(PageUtil.noPage(), new QueryWrapper<ChargeLog>()
                .lambda().eq(ChargeLog::getOpenId, openId).orderByDesc(ChargeLog::getCreateTime));
        return new ResponseObject(true, "ok").push("msg", res.getRecords());
    }

    /**
     * @param wxUserId 用户id
     * @return
     * @author Lee
     * @desc 查询用户所有优惠券
     */
    @RequestMapping(value = "findAllUserCoupons", method = RequestMethod.POST)
    public ResponseObject findAllUserCoupons(@RequestParam String wxUserId) {

        List<Map<String, Object>> wxUserCoupons = tWxUserCouponService.findAllUserCoupons(Long.valueOf(wxUserId));
        return new ResponseObject(true, "查询成功").push("list", wxUserCoupons);
    }

    /**
     * 对加密手机号进行解密
     *
     * @param decryptData 敏感参数
     * @param sessionKey  session_key
     * @param ivData      iv
     * @param openid      用户openid
     * @return
     */
    @RequestMapping(value = "decryptPhone", method = RequestMethod.POST)
    public ResponseObject decryptPhone(@RequestParam String decryptData, @RequestParam String sessionKey,
                                       @RequestParam String ivData, @RequestParam String openid) {
        // 对手机号码进行解密，并存进数据库
        int rs = tWxUserService.decryptPhone(decryptData, sessionKey, ivData, openid);
        if (rs == 0) {
            return new ResponseObject(false, "网络错误，请重试");
        }
        return new ResponseObject(true, "手机号绑定成功");
    }
}
