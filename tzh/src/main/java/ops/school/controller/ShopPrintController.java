package ops.school.controller;



import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;
import ops.school.api.constants.RedisConstants;
import ops.school.api.dto.ShopPrintDTO;
import ops.school.api.dto.print.PrintDataDTO;
import ops.school.api.entity.Orders;
import ops.school.api.entity.ShopPrint;
import ops.school.api.enums.PublicErrorEnums;
import ops.school.api.enums.ResponseViewEnums;
import ops.school.api.exception.Assertions;
import ops.school.api.service.ShopPrintService;
import ops.school.api.util.LoggerUtil;
import ops.school.api.util.RedisClient;
import ops.school.api.util.ResponseObject;
import ops.school.api.util.TimeUtilS;
import ops.school.service.TOrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: QinDaoFang
 * @date:   2019/8/11 18:33 
 * @desc:   
 */
@Controller
@RequestMapping("/ops/shop/print")
public class ShopPrintController {

    @Autowired
    private ShopPrintService shopPrintService;


    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static CountDownLatch countDownLatch = new CountDownLatch(10);

    @Autowired
    private TOrdersService tOrderService;


    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.ResponseObject
     * @param   dto
     * @Desc:   desc 通过DTO新增
     */
    @ApiOperation(value="添加",httpMethod="POST")
    @ResponseBody
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public ResponseObject saveOneShopFeiEByDTO(ShopPrintDTO dto){
        Assertions.notNull(dto,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        Assertions.notNull(dto.getPrintBrand(),PublicErrorEnums.PULBIC_EMPTY_PARAM);
        Assertions.notNull(dto.getCreateId(),ResponseViewEnums.CREATE_ID_CANT_NULL);
        Assertions.notNull(dto.getFeiESn(),PublicErrorEnums.PULBIC_EMPTY_PARAM);
        Assertions.notNull(dto.getFeiEKey(),PublicErrorEnums.PULBIC_EMPTY_PARAM);
        Assertions.notNull(dto,dto.getShopId(),PublicErrorEnums.PULBIC_EMPTY_PARAM);
        ResponseObject view = shopPrintService.saveOneShopFeiEByDTO(dto);
        return view;
    }

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.ResponseObject
     * @param   dto
     * @Desc:   desc 通过DTO更新
     */
    @ApiOperation(value="通过参数更新",httpMethod="POST")
    @ResponseBody
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public ResponseObject updateOneShopFeiEByDTO(ShopPrintDTO dto){
        Assertions.notNull(dto,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        ResponseObject view = shopPrintService.updateOneShopFeiEByDTO(dto);
        return view;
    }

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.ResponseObject
     * @param   id
     * @Desc:   desc 通过id删除
     */
    @ApiOperation(value="通过id删除",httpMethod="POST")
    @ResponseBody
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public ResponseObject deleteOneShopFeiEById(Long id){
        Assertions.notNull(id,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        ResponseObject view = shopPrintService.deleteOneShopFeiEById(id);
        return view;
    }

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.ResponseObject
     * @param   id
     * @Desc:   desc 通过id查询一个
     */
    @ApiOperation(value="通过id查询一个",httpMethod="POST")
    @ResponseBody
    @RequestMapping(value = "/one",method = RequestMethod.POST)
    public ResponseObject findOneShopFeiEById(Long id){
        Assertions.notNull(id,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        ShopPrint resultVO = shopPrintService.findOneShopFeiEById(id);
        return new ResponseObject(true, ResponseViewEnums.FIND_SUCCESS).push("shopPrint",resultVO);
    }

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.ResponseObject
     * @param   shopId
     * @Desc:   desc 通过id查询一个
     */
    @ApiOperation(value="通过店铺id查询一个",httpMethod="POST")
    @ResponseBody
    @RequestMapping(value = "/find",method = RequestMethod.POST)
    public ResponseObject findOneShopFeiEByShopId(Long shopId){
        Assertions.notNull(shopId,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        List<ShopPrint> resultVO = shopPrintService.findOneShopFeiEByShopId(shopId);
        return new ResponseObject(true, ResponseViewEnums.FIND_SUCCESS).push("shopPrint",resultVO);
    }

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.ResponseObject
     * @param   ids
     * @Desc:   desc 通过ids查询多个
     */
    @ApiOperation(value="通过ids查询多个",httpMethod="POST")
    @ResponseBody
    @RequestMapping(value = "/more",method = RequestMethod.POST)
    public ResponseObject findMoreShopFeiEById(List<Long> ids){
        Assertions.notEmpty(ids,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        List<ShopPrint> resultVO = shopPrintService.batchFindShopFeiEByIds(ids);
        return new ResponseObject(true, ResponseViewEnums.FIND_SUCCESS).push("list",resultVO);
    }


    @ApiOperation(value="根据订单号打印订单和接手订单",httpMethod="POST")
    @ResponseBody
    @RequestMapping(value = "/accept",method = RequestMethod.POST)
    public ResponseObject printAndAcceptOrder(String orderId,Long shopId){
        Assertions.notNull(orderId,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        Assertions.notNull(shopId,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        String orderRedis = (String) stringRedisTemplate.boundHashOps("SHOP_DJS"+shopId.toString()).get(orderId);
        Assertions.notNull(orderRedis,PublicErrorEnums.PUBLIC_DATA_ERROR);
        ResponseObject view = tOrderService.printAndAcceptOneOrderByOId(orderId,shopId);
        return view;
    }

}
