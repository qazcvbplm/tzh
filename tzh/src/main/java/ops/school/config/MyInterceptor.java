package ops.school.config;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import ops.school.api.auth.JWTUtil;
import ops.school.api.util.ResultUtil;
import ops.school.enums.PublicErrorEnums;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class MyInterceptor implements HandlerInterceptor {


	/*@Autowired
	private AuthController auth;*/

	public String verify(String token){
		if(JWTUtil.verify(token)){
			DecodedJWT de = JWT.decode(token);
			return de.getClaim("msg").asString();
		}else{
			return null;
		}
	}
	
	 @Override
	    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
	            throws Exception {
		 String token =request.getHeader("token");
		 String msg=null;
		    if((msg=verify(token))!=null){
		    	JSONObject json=JSON.parseObject(msg);
		    	request.setAttribute("Id", json.getString("userId"));
		    	request.setAttribute("role", json.getString("role"));
		    	return true;
		    }
		 new ResultUtil().error(request, response, PublicErrorEnums.LOGIN_TIME_OUT.getErrorMessage());
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