package ops.school.config;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import ops.school.api.auth.JWTUtil;
import ops.school.api.util.ResultUtil;
import ops.school.api.enums.PublicErrorEnums;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

@Component
public class MyInterceptor implements HandlerInterceptor {

    private static Map<String, String> authMap = new HashMap<>();

    private final static String whitePropertiesName = "authWhiteList";

    public String verify(String token) {
        if (JWTUtil.verify(token)) {
            DecodedJWT de = JWT.decode(token);
            return de.getClaim("msg").asString();
        } else {
            return null;
        }
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        Long startTime = System.currentTimeMillis();
        String token = request.getHeader("token");
        //先校验白名单
		// 暂时不用
//        String requestUri = request.getServletPath();
//        if (isFilterToPass(requestUri)) {
//            LoggerUtil.log("SSOClientFilter-在白名单中" + requestUri);
//            return true;
//        }
        String msg = null;
        if ((msg = verify(token)) != null) {
            JSONObject json = JSON.parseObject(msg);
            request.setAttribute("Id", json.getString("userId"));
            request.setAttribute("role", json.getString("role"));
            return true;
        }
        Long endTime = System.currentTimeMillis();
        System.out.println("拦截器pre处理时间(ms)：" + (endTime - startTime));
        new ResultUtil().error(request, response, "1100");
            return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
            throws Exception {

    }

    @Override
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
            throws Exception {

    }
}
