package ops.school.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ops.school.api.dao.NoticeMapper;
import ops.school.api.entity.Notice;
import ops.school.api.util.ResponseObject;
import ops.school.api.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
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
@Api(tags="公告模块")
@RequestMapping("ops/notice")
public class NoticeController {


    @Autowired
	private NoticeMapper noticeMapper;
	
	
	@ApiOperation(value="添加",httpMethod="POST")
	@PostMapping("add")
	public ResponseObject add(HttpServletRequest request, HttpServletResponse response,
							  @ModelAttribute @Valid Notice notice, BindingResult result){
		              Util.checkParams(result);
		              noticeMapper.insert(notice);
		              return new ResponseObject(true, "添加成功");
	}
	
	@ApiOperation(value="查询",httpMethod="POST")
	@PostMapping("find")
	public ResponseObject find(HttpServletRequest request,HttpServletResponse response,Notice notice){
		              List<Notice> list = noticeMapper.find(notice);
		              return new ResponseObject(true, "ok").push("list", list);
	}
	
	@ApiOperation(value="更新",httpMethod="POST")
	@PostMapping("update")
	public ResponseObject update(HttpServletRequest request,HttpServletResponse response,Notice notice){
		              int i= noticeMapper.updateByPrimaryKeySelective(notice);
		              return new ResponseObject(true, "更新"+i+"条记录").push("result", i);
	}
}
