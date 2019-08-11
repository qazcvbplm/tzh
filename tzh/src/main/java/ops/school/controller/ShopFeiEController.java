package ops.school.controller;



import javax.annotation.Resource;

import io.swagger.annotations.ApiOperation;
import ops.school.api.dto.ShopFeiEDTO;
import ops.school.api.entity.ShopFeiE;
import ops.school.api.enums.PublicErrorEnums;
import ops.school.api.exception.Assertions;
import ops.school.api.service.ShopFeiEService;
import ops.school.api.util.ResponseObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: QinDaoFang
 * @date:   2019/8/11 18:33 
 * @desc:   
 */
@Controller
@RequestMapping("/ops/shop/fei")
public class ShopFeiEController{

    @Resource(name="shopFeiEService")
    private ShopFeiEService shopFeiEService;

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
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public ResponseObject saveOneShopFeiEByDTO(ShopFeiEDTO dto){
        Assertions.notNull(dto,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        ResponseObject view = shopFeiEService.saveOneShopFeiEByDTO(dto);
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
    public ResponseObject updateOneShopFeiEByDTO(ShopFeiEDTO dto){
        Assertions.notNull(dto,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        ResponseObject view = shopFeiEService.updateOneShopFeiEByDTO(dto);
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
        ResponseObject view = shopFeiEService.deleteOneShopFeiEById(id);
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
    public ShopFeiE findOneShopFeiEById(Long id){
        Assertions.notNull(id,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        ShopFeiE resultVO = shopFeiEService.findOneShopFeiEById(id);
        return resultVO;
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
    public List<ShopFeiE> findMoreShopFeiEById(List<Long> ids){
        Assertions.notEmpty(ids,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        List<ShopFeiE> resultVO = shopFeiEService.batchFindShopFeiEByIds(ids);
        return resultVO;
    }

}
