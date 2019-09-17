package ops.school.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ops.school.api.constants.RedisConstants;
import ops.school.api.entity.SecondHand;
import ops.school.api.service.SecondHandService;
import ops.school.api.util.RedisUtil;
import ops.school.api.util.ResponseObject;
import ops.school.api.util.TimeUtilS;
import ops.school.api.util.Util;
import ops.school.api.constants.NumConstants;
import ops.school.service.TSecondService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@Api(tags = "二手模块")
@RequestMapping("ops/secondhand")
public class SecondHandController {

    @Autowired
    private SecondHandService secondHandService;
    @Autowired
    private TSecondService tSecondService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisUtil redisUtil;

    @ApiOperation(value = "添加", httpMethod = "POST")
    @PostMapping("add")
    public ResponseObject add(HttpServletRequest request, HttpServletResponse response, @ModelAttribute @Valid SecondHand secondHand, BindingResult result) {
        Util.checkParams(result);
        secondHandService.save(secondHand);
        stringRedisTemplate.boundHashOps(RedisConstants.Index_Second_Hand_Shop_Hash).delete("all");
        return new ResponseObject(true, "添加成功");
    }


    @ApiOperation(value = "查询", httpMethod = "POST")
    @RequestMapping("find")
    public ResponseObject find(HttpServletRequest request, HttpServletResponse response, SecondHand secondHand) {
        secondHand.setIsDelete(NumConstants.DB_TABLE_IS_DELETE_NO);
        QueryWrapper<SecondHand> query = new QueryWrapper<SecondHand>().setEntity(secondHand).orderByDesc("create_time");
        IPage<SecondHand> iPage = secondHandService.page(new Page<>(secondHand.getPage(), secondHand.getSize()), query);
        List<SecondHand> secondHandList = iPage.getRecords();
        return new ResponseObject(true, "ok")
                .push("list", secondHandList)
                .push("total", iPage.getTotal());
    }


    @ApiOperation(value = "首页查询", httpMethod = "POST")
    @RequestMapping("index")
    public ResponseObject findIndex(HttpServletRequest request, HttpServletResponse response, SecondHand secondHand) {
        //先查缓存
        Object redisGet = stringRedisTemplate.boundHashOps(RedisConstants.Index_Second_Hand_Shop_Hash).get("all");
        Page<SecondHand> redisIPage = null;
        if (redisGet != null) {
            redisIPage = JSONObject.parseObject((String) redisGet, new TypeReference<Page<SecondHand>>(){});
        }
        if (redisIPage != null && redisIPage.getTotal() > NumConstants.INT_NUM_0 && redisIPage.getRecords().size() > NumConstants.INT_NUM_0) {
            return new ResponseObject(true, "ok")
                    .push("list", redisIPage.getRecords())
                    .push("total", redisIPage.getTotal());
        }
        secondHand.setIsDelete(NumConstants.DB_TABLE_IS_DELETE_NO);
        QueryWrapper<SecondHand> query = new QueryWrapper<SecondHand>().setEntity(secondHand).orderByDesc("create_time");

        IPage<SecondHand> iPage = secondHandService.page(new Page<>(secondHand.getPage(), secondHand.getSize()), query);
        List<SecondHand> secondHandList = iPage.getRecords();
        //查询存缓存
        Page<SecondHand> page = new Page<SecondHand>();
        page.setTotal(iPage.getTotal());
        page.setRecords(iPage.getRecords());
        stringRedisTemplate.boundHashOps(RedisConstants.Index_Second_Hand_Shop_Hash).put("all", JSON.toJSONString(page));
        redisUtil.keyNoExpireThenSetTimeAt(RedisConstants.Index_Second_Hand_Shop_Hash, TimeUtilS.getDayEnd());
        return new ResponseObject(true, "ok")
                .push("list", secondHandList)
                .push("total", iPage.getTotal());
    }


    @ApiOperation(value = "更新", httpMethod = "POST")
    @PostMapping("update")
    public ResponseObject update(HttpServletRequest request, HttpServletResponse response, SecondHand secondHand) {
        if (secondHandService.updateById(secondHand)) {
            stringRedisTemplate.boundHashOps(RedisConstants.Index_Second_Hand_Shop_Hash).delete("all");
            return new ResponseObject(true, "更新成功");
        } else {
            return new ResponseObject(false, "更新失败");
        }

    }

    /**
     * @param title 二手商品名称
     * @param page  分页 page
     * @param size  分页 size
     * @return
     * @author Lee
     */
    @RequestMapping(value = "fuzzyFindSecondHand", method = RequestMethod.POST)
    public ResponseObject fuzzyFindSecondHand(String title, Integer isShow, Integer schoolId,
                                              String category, Integer page, Integer size) {
        List<SecondHand> secondHands = tSecondService.fuzzyFind(title, isShow, schoolId, category, page, size);
        return new ResponseObject(true, "查询成功").push("list", secondHands);
    }
}
