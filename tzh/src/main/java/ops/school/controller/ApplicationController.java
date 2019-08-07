package ops.school.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ops.school.api.dao.WxUserBellMapper;
import ops.school.api.dto.ReplyTextMsg;
import ops.school.api.entity.WxUser;
import ops.school.api.enums.ResponseViewEnums;
import ops.school.api.exception.DisplayException;
import ops.school.api.service.WxUserBellService;
import ops.school.api.service.WxUserService;
import ops.school.api.util.ResponseObject;
import ops.school.api.wxutil.XMLUtil;
import ops.school.config.RabbitMQConfig;
import ops.school.constants.NumConstants;
import ops.school.message.dto.WxUserAddSourceDTO;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@RestController
@Api(tags="总体信息模块")
@RequestMapping("ops/application")
public class ApplicationController {

	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	@Autowired
	private WxUserService wxUserService;
	@Autowired
	private WxUserController wxUserController;
	@Autowired
	private WxUserBellService wxUserBellService;
    @Autowired
    private RabbitTemplate rabbitTemplate;



	@RequestMapping("check")
	public String version(HttpServletRequest request,HttpServletResponse response) throws IOException, SAXException{
		Map<String, String>  map = XMLUtil.parseXML(request);
		String ToUserName=map.get("ToUserName");//	开发者微信号
		String FromUserName=map.get("FromUserName");//发送方帐号（一个OpenID）
		String MsgType=map.get("MsgType");
		String Content=map.get("Content");
		String Event=map.get("Event");
		WxUser user=checkopenId(FromUserName);
		if(MsgType.equals("text")){
			if(Content.startsWith("绑定")){
				if(user.getPhone()==null||user.getPhone().equals("")){
					Content=Content.replace("绑定", "");
					ReplyTextMsg re;
					List<WxUser> minUser=wxUserService.findByPhoneGZH(Content);
					if(minUser.size()==0){
						re=new ReplyTextMsg(ToUserName,FromUserName,"text","该号码未在客户端小程序中绑定手机号!");
						return re.Msg2Xml();
					}
					if(minUser.size()>1){
						re=new ReplyTextMsg(ToUserName,FromUserName,"text","验证码发送失败，等待客服与您回复!");
						return re.Msg2Xml();
					}
					ResponseObject rs=wxUserController.getcode(request, response, Content);
					stringRedisTemplate.opsForValue().set(user.getOpenId(), Content,5,TimeUnit.MINUTES);
					if(rs.isCode()){
						re=new ReplyTextMsg(ToUserName,FromUserName,"text","验证码已发送，回复验证＋验证码即可绑定成功，例：验证8888（勿加空格和额外字符）");
					}else{
						re=new ReplyTextMsg(ToUserName,FromUserName,"text",rs.getMsg());
					}
					return re.Msg2Xml();
				}else{
					ReplyTextMsg re=new ReplyTextMsg(ToUserName,FromUserName,"text","已经绑定了"+user.getPhone());
					return re.Msg2Xml();
				}
			}
			if(Content.startsWith("验证")){
				Content=Content.replace("验证", "");
				String phone=stringRedisTemplate.opsForValue().get(user.getOpenId());
				request.setAttribute("Id", user.getOpenId());
				List<WxUser> minUser=wxUserService.findByPhoneGZH(phone);
				ResponseObject rs=wxUserController.bind(request, response, phone, Content,user.getId());
				if(rs.isCode()){
					ReplyTextMsg re=new ReplyTextMsg(ToUserName,FromUserName,"text","绑定成功，恭喜您获得88积分奖励。奖励已发放至您的客户端小程序中，请自行前往查看！");
					//增加积分
					user=minUser.get(0);
					// rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_WX_USER_BELL, new WxUserAddSourceDTO(user.getOpenId(), 88).toJsonString());
					//增加用户积分
					//积分不保存小数位，向下取整
					Integer addSource = 88;
					Integer addUserSourceNum = wxUserBellService.addSourceByWxId(addSource,user.getId());
					if (addUserSourceNum != NumConstants.INT_NUM_1){
						DisplayException.throwMessageWithEnum(ResponseViewEnums.ORDER_COMPLETE_SOURCE_ERROR);
					}
//	        		Map<String,Object> ch=new HashMap<>();
//	        		ch.put("phone", user.getOpenId()+"-"+user.getPhone());
//	        	    ch.put("amount", "0.5");
//					wxUserBellService.charge(ch);
					return re.Msg2Xml();
				}else{
					ReplyTextMsg re=new ReplyTextMsg(ToUserName,FromUserName,"text","绑定失败请联系管理员");
					return re.Msg2Xml();
				}
			}
		}
		if(MsgType.equals("event")){
			if(Event.equals("subscribe")){
				ReplyTextMsg re=new ReplyTextMsg(ToUserName,FromUserName,"text",
						"来了老弟！为了与您的用户信息匹配，请先绑定与小程序一致的手机号。回复绑定＋手机号获取验证码，例：绑定13788889999（勿加空格和额外字符）");
				return re.Msg2Xml();
			}
			if(Event.equals("CLICK")){
				if(map.get("EventKey").equals("我要绑定")){
					ReplyTextMsg re=new ReplyTextMsg(ToUserName,FromUserName,"text",
							"来了老弟！为了与您的用户信息匹配，请先绑定与小程序一致的手机号。回复绑定＋手机号获取验证码，例：绑定13788889999（勿加空格和额外字符）");
					return re.Msg2Xml();
				}
				if(map.get("EventKey").equals("商家APP")){
					ReplyTextMsg re=new ReplyTextMsg(ToUserName,FromUserName,"text",
							"安卓版本：www.chuyinkeji.cn/yzxy/YeZiXiaoYuan-Shop.apk        苹果版本：https://itunes.apple.com/cn/app/id1457549508?mt=8");
					return re.Msg2Xml();
				}
				if(map.get("EventKey").equals("商务合作")){
					ReplyTextMsg re=new ReplyTextMsg(ToUserName,FromUserName,"text",
							"请加客服qq：2858385406");
					return re.Msg2Xml();
				}
			}
			if(Event.equals("unsubscribe")){
				if(user.getPhone()!=null){

				}
			}
		}
		ReplyTextMsg re=new ReplyTextMsg(ToUserName,FromUserName,"text","格式不正确");
		return re.Msg2Xml();
	}
	
	public WxUser checkopenId(String openId){
			 WxUser user=wxUserService.login(openId,0,0,"微信公众号");
			 return user;
	}

	
	
	@GetMapping("version")
	@ApiOperation(value="获取版本号",httpMethod="POST")
	public String version(){
		return stringRedisTemplate.opsForValue().get("min_version");
	}
	
	@GetMapping("setversion")
	@ApiOperation(value="设置版本号",httpMethod="POST")
	public String setversion(String version){
		stringRedisTemplate.opsForValue().set("min_version",version);
		return "ok";
	}
	
	@GetMapping("appversion")
	@ApiOperation(value="获取app版本号",httpMethod="POST")
	public ResponseObject appversion(){
		String version=stringRedisTemplate.opsForValue().get("app_version");
		return new ResponseObject(true, "")
				.push("version", version)
				.push("androidUrl", "http://www.chuyinkeji.cn/yzxy/new.apk")
				.push("iosUrl", "https://itunes.apple.com/cn/app/id1457549508?mt=8");
	}
	
	@GetMapping("setappversion")
	@ApiOperation(value="设置app版本号",httpMethod="POST")
	public String setappversion(String version){
		stringRedisTemplate.opsForValue().set("app_version",version);
		return "ok";
	}
	
}
