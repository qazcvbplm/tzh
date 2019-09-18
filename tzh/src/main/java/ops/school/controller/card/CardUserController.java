package ops.school.controller.card;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import ops.school.api.service.card.CardUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ops.school.api.dto.card.CardUserDTO;
import ops.school.api.entity.card.CardUser;
import ops.school.api.vo.card.CardUserVO;
import ops.school.api.enums.ResponseViewEnums;
import ops.school.api.util.LimitTableData;
import ops.school.api.enums.PublicErrorEnums;
import ops.school.api.util.ResponseObject;
import ops.school.api.exception.Assertions;


@Controller
@RequestMapping("/api/cardUser")
public class CardUserController {
    @Resource(name="ardUserService")
    private CardUserService ardUserService;


    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.LimitTableData<CardUserVO>
     * @param   dto
     * @Desc:   desc 分页查询
     */
    @ResponseBody
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    public ResponseObject limitTableData(CardUserDTO dto){
        Assertions.notNull(dto,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        LimitTableData<CardUserVO> tableData = ardUserService.limitTableDataByDTO(dto);
        return new ResponseObject(true, ResponseViewEnums.SUCCESS)
                .push("total",tableData.getRecordsTotal())
                .push("list",tableData.getData());
    }

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: java.util.List<CardUserVO>
     * @param
     * @Desc:   desc 查询所有数据
     */
    @ResponseBody
    @RequestMapping(value = "/all",method = RequestMethod.POST)
    public List<CardUserVO> findAllCardUserVOs(){
        List<CardUserVO> allCardUserVOs = ardUserService.findAllCardUserVOs();
        return allCardUserVOs;
    }

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.ResponseView
     * @param   dto
     * @Desc:   desc 通过DTO新增
     */
    @ResponseBody
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public ResponseObject saveOneCardUserByDTO(CardUserDTO dto){
        Assertions.notNull(dto,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        ResponseObject view = ardUserService.saveOneCardUserByDTO(dto);
        return view;
    }

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.ResponseView
     * @param   dto
     * @Desc:   desc 通过DTO更新
     */
    @ResponseBody
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public ResponseObject updateOneCardUserByDTO(CardUserDTO dto){
        Assertions.notNull(dto,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        ResponseObject view = ardUserService.updateOneCardUserByDTO(dto);
        return view;
    }

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.ResponseView
     * @param   id
     * @Desc:   desc 通过id删除
     */
    @ResponseBody
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public ResponseObject deleteOneCardUserById(Long id){
        Assertions.notNull(id,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        ResponseObject view = ardUserService.deleteOneCardUserById(id);
        return view;
    }

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.ResponseView
     * @param   id
     * @Desc:   desc 通过id查询一个
     */
    @ResponseBody
    @RequestMapping(value = "/one",method = RequestMethod.POST)
    public CardUserVO findOneCardUserById(Long id){
        Assertions.notNull(id,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        CardUserVO resultVO = ardUserService.findOneCardUserById(id);
        return resultVO;
    }

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.ResponseView
     * @param   id
     * @Desc:   desc 通过id停用
     */
    @ResponseBody
    @RequestMapping(value = "/stop",method = RequestMethod.POST)
    public ResponseObject stopOneCardUserById(Long id){
        Assertions.notNull(id,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        ResponseObject view = ardUserService.stopOneCardUserById(id);
        return view;
    }

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.ResponseView
     * @param   id
     * @Desc:   desc 通过id启用
     */
    @ResponseBody
    @RequestMapping(value = "/start",method = RequestMethod.POST)
    public ResponseObject startOneCardUserById(Long id){
        Assertions.notNull(id,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        ResponseObject view = ardUserService.startOneCardUserById(id);
        return view;
    }

}
