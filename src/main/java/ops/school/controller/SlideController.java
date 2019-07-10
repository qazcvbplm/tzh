package ops.school.controller;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ops.school.api.entity.Slide;
import ops.school.api.service.SlideService;
import ops.school.api.util.ResponseObject;
import ops.school.api.util.SpringUtil;
import ops.school.api.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@Api(tags="轮播图模块")
@RequestMapping("ops/slide")
public class SlideController {

	@Autowired
	private SlideService slideService;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@ApiOperation(value="添加",httpMethod="POST")
	@PostMapping("add")
	public ResponseObject add(HttpServletRequest request, HttpServletResponse response, @ModelAttribute @Valid Slide slide, BindingResult result){
		              Util.checkParams(result);
        if (SpringUtil.redisCache()) {
		            	  stringRedisTemplate.boundHashOps("SLIDE_LIST").delete(slide.getSchoolId()+"");
		              }
		slideService.save(slide);
		              return new ResponseObject(true, "添加成功");
	}
	
	@ApiOperation(value="查询",httpMethod="POST")
	@PostMapping("find")
	public ResponseObject find(HttpServletRequest request,HttpServletResponse response,int schoolId){
						List<Slide> list=null;
        if (SpringUtil.redisCache()) {
							if(stringRedisTemplate.boundHashOps("SLIDE_LIST").get(schoolId+"")!=null){
								 list=JSON.parseArray(stringRedisTemplate.boundHashOps("SLIDE_LIST").get(schoolId+"").toString(), Slide.class);
							}else{
								list = slideService.findBySchoolId(schoolId);
								stringRedisTemplate.boundHashOps("SLIDE_LIST").put(schoolId+"", JSON.toJSONString(list));
							}
						}
		              return new ResponseObject(true, "ok").push("list", list);
	}
	
	@ApiOperation(value="更新",httpMethod="POST")
	@PostMapping("update")
	public ResponseObject update(HttpServletRequest request,HttpServletResponse response,Slide slide){
		boolean rs = slideService.updateById(slide);
		if (SpringUtil.redisCache()) {
			Slide temp = slideService.getById(slide.getId());
		            	  stringRedisTemplate.boundHashOps("SLIDE_LIST").delete(temp.getSchoolId()+"");
		              }
		return new ResponseObject(true, "更新成功");
	}
}
