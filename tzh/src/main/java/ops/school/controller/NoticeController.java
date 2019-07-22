package ops.school.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ops.school.api.entity.Notice;
import ops.school.api.service.NoticeService;
import ops.school.api.util.ResponseObject;
import ops.school.api.util.Util;
import ops.school.constants.NumConstants;
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
	private NoticeService noticeService;
	
	
	@ApiOperation(value="添加",httpMethod="POST")
	@PostMapping("add")
	public ResponseObject add(HttpServletRequest request, HttpServletResponse response,
							  @ModelAttribute @Valid Notice notice, BindingResult result){
		              Util.checkParams(result);
		noticeService.save(notice);
		              return new ResponseObject(true, "添加成功");
	}
	
	@ApiOperation(value="查询",httpMethod="POST")
	@PostMapping("find")
	public ResponseObject find(HttpServletRequest request,HttpServletResponse response,Notice notice){
		notice.setIsDelete(NumConstants.DB_TABLE_IS_DELETE_NO);
		List<Notice> list = noticeService.list(new QueryWrapper<Notice>().setEntity(notice));
		return new ResponseObject(true, "ok")
				.push("list", list)
				.push("total",list.size());
	}
	
	@ApiOperation(value="更新",httpMethod="POST")
	@PostMapping("update")
	public ResponseObject update(HttpServletRequest request,HttpServletResponse response,Notice notice){
		if (noticeService.updateById(notice)) {
			return new ResponseObject(true, "更新成功");
		} else {
			return new ResponseObject(false, "更新失败");
		}

	}
}
