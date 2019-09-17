package ops.school.api.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTUtil {

	private static final long OUT_TIME=120*60*1000;
	
	private static final String SECRET="f3b9e9ef-08da-4934-901d-d121bd33f515";
	
	
	public static String sign(String userId,String loginName, String role){
		try {
			Date date=new Date(System.currentTimeMillis()+OUT_TIME);
			Algorithm algorithm=Algorithm.HMAC256(SECRET);
			Map<String,Object> header=new HashMap<>();
			header.put("typ","JWT");
			header.put("alg","HS256");
			return JWT.create().withClaim("userId", userId).withClaim("loginName", loginName).
					withClaim("msg", "{role:'"+role+"',userId:'"+userId+"'}").withExpiresAt(date).sign(algorithm);
		} catch (IllegalArgumentException | UnsupportedEncodingException e) {
			return null;
		}
	}
	
	public static boolean verify(String token){
		try {
			Algorithm algorithm=Algorithm.HMAC256(SECRET);
			JWT.require(algorithm).build().verify(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
