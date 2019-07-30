package ops.school.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ops.school.api.entity.RichText;
import ops.school.api.service.RichTextService;
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
@Api(tags = "富文本模块")
@RequestMapping("ops/rich/text")
public class RichTextController {

    @Autowired
    private RichTextService richTextService;


    @ApiOperation(value = "添加", httpMethod = "POST")
    @PostMapping("add")
    public ResponseObject add(HttpServletRequest request, HttpServletResponse response, @ModelAttribute @Valid RichText richText, BindingResult result) {
        Util.checkParams(result);
        richTextService.save(richText);
        return new ResponseObject(true, "添加成功");
    }


    @ApiOperation(value = "查询", httpMethod = "POST")
    @PostMapping("find")
    public ResponseObject find(HttpServletRequest request, HttpServletResponse response, Integer parentId, Integer richTextId) {

        List<RichText> richTexts = richTextService.findByIdAndParentId(richTextId,parentId);
        return new ResponseObject(true, "ok").push("msg", richTexts);
    }

    @ApiOperation(value = "更新", httpMethod = "POST")
    @PostMapping("update")
    public ResponseObject update(HttpServletRequest request, HttpServletResponse response, RichText richText) {
        if (richTextService.updateById(richText)) {
            return new ResponseObject(true, "更新成功");
        } else {
            return new ResponseObject(false, "更新失败");
        }

    }

    @ApiOperation(value = "更新", httpMethod = "POST")
    @PostMapping("delete")
    public ResponseObject delete(HttpServletRequest request, HttpServletResponse response, Integer id) {
        if (richTextService.removeById(id)) {
            return new ResponseObject(true, "更新成功");
        } else {
            return new ResponseObject(false, "更新失败");
        }

    }
}
