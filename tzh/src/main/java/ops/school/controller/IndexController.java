package ops.school.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ops.school.api.util.RedisUtil;
import ops.school.api.util.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@Api(tags="后台")
@RequestMapping("ops/admin")
public class IndexController {


	@Autowired
	  private RedisUtil cache;

	
	@ApiOperation(value="后台首页",httpMethod="POST")
	@PostMapping("index")
	public ResponseObject add(HttpServletRequest request, HttpServletResponse response, int  schoolId){
		              return new ResponseObject(true, "ok").push("msg", cache.get(schoolId));
	}
}
