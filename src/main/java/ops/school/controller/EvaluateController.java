package ops.school.controller;

import ops.school.config.RedisConfig;
import ops.school.dto.redis.WxUserAddSourceDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ops.school.api.dao.EvaluateMapper;
import ops.school.api.dao.OrdersMapper;
import ops.school.api.dao.RunOrdersMapper;
import ops.school.api.entity.Base;
import ops.school.api.entity.Evaluate;
import ops.school.api.util.Util;
import ops.school.util.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api(tags="评论模块")
@RequestMapping("ops/evaluate")
public class EvaluateController {


    @Autowired
	private EvaluateMapper evaluateMapper;
	@Autowired
	private OrdersMapper ordersMapper;
	@Autowired
	private RunOrdersMapper runOrdersMapper;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	@ApiOperation(value="添加",httpMethod="POST")
	@PostMapping("add")
	public ResponseObject add(HttpServletRequest request, HttpServletResponse response, @ModelAttribute @Valid Evaluate evaluate,
							  @RequestParam String userId, BindingResult result){
		              Util.checkParams(result);
		              if(ordersMapper.pl(evaluate.getOrderid())==1){
		            	  evaluateMapper.insert(evaluate);
                          stringRedisTemplate.convertAndSend(RedisConfig.SENDERBELL, new WxUserAddSourceDTO(userId, 3).toJsonString());
		              }
		              if(runOrdersMapper.pl(evaluate.getOrderid())==1){
		            	  evaluateMapper.insert(evaluate);
                          stringRedisTemplate.convertAndSend(RedisConfig.SENDERBELL, new WxUserAddSourceDTO(userId, 3).toJsonString());
		            	  }
		              return new ResponseObject(true, "添加成功");
	}
	
	
	@ApiOperation(value="查询",httpMethod="POST")
	@PostMapping("find")
	public ResponseObject find(HttpServletRequest request,HttpServletResponse response,Evaluate evaluate){
		              List<Evaluate> list = evaluateMapper.find(evaluate);
		              return new ResponseObject(true, "ok").push("total", evaluateMapper.count(evaluate)).push("list", list);
	}

    @ApiOperation(value = "按店铺查询", httpMethod = "POST")
    @PostMapping("findbyshopid")
    public ResponseObject find(HttpServletRequest request, HttpServletResponse response, int shopId,Base base) {
    	Map<String, Object> map=new HashMap<String,Object>();
    	map.put("shopId", shopId);
    	map.put("page", base.getPage());
    	map.put("size", base.getSize());
        List<Evaluate> list = evaluateMapper.findByShopId(map);
        return new ResponseObject(true, "ok").push("list", list);
    }


}
