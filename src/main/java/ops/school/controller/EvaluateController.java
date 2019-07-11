package ops.school.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ops.school.api.dto.redis.WxUserAddSourceDTO;
import ops.school.api.entity.Base;
import ops.school.api.entity.Evaluate;
import ops.school.api.service.EvaluateService;
import ops.school.api.service.OrdersService;
import ops.school.api.service.RunOrdersService;
import ops.school.api.util.ResponseObject;
import ops.school.api.util.Util;
import ops.school.config.RedisConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@Api(tags="评论模块")
@RequestMapping("ops/evaluate")
public class EvaluateController {


    @Autowired
    private EvaluateService evaluateService;
	@Autowired
    private OrdersService ordersService;
	@Autowired
    private RunOrdersService runOrdersService;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	@ApiOperation(value="添加",httpMethod="POST")
	@PostMapping("add")
	public ResponseObject add(HttpServletRequest request, HttpServletResponse response, @ModelAttribute @Valid Evaluate evaluate,
							  @RequestParam String userId, BindingResult result){
		              Util.checkParams(result);
        if (ordersService.pl(evaluate.getOrderid()) == 1) {
            evaluateService.save(evaluate);
                          stringRedisTemplate.convertAndSend(RedisConfig.SENDERBELL, new WxUserAddSourceDTO(userId, 3).toJsonString());
		              }
        if (runOrdersService.pl(evaluate.getOrderid()) == 1) {
            evaluateService.save(evaluate);
                          stringRedisTemplate.convertAndSend(RedisConfig.SENDERBELL, new WxUserAddSourceDTO(userId, 3).toJsonString());
		            	  }
		              return new ResponseObject(true, "添加成功");
	}
	
	
	@ApiOperation(value="查询",httpMethod="POST")
	@PostMapping("find")
	public ResponseObject find(HttpServletRequest request,HttpServletResponse response,Evaluate evaluate){
        QueryWrapper<Evaluate> query = new QueryWrapper<Evaluate>().setEntity(evaluate);
        List<Evaluate> list = evaluateService.list(query);
        return new ResponseObject(true, "ok").push("total",
                evaluateService.count(query)).push("list", list);
	}

    @ApiOperation(value = "按店铺查询", httpMethod = "POST")
    @PostMapping("findbyshopid")
    public ResponseObject find(HttpServletRequest request, HttpServletResponse response, int shopId,Base base) {
        QueryWrapper<Evaluate> query = new QueryWrapper<Evaluate>();
        query.lambda().eq(Evaluate::getShopId, shopId);
        List<Evaluate> list = evaluateService.page(new Page<>(base.getPage(), base.getSize()), query).getRecords();
        return new ResponseObject(true, "ok").push("list", list);
    }


}