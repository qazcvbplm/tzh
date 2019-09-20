package ops.school.controller.card;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import ops.school.api.service.card.CardPayLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ops.school.api.entity.card.CardPayLog;
import ops.school.api.dto.card.CardPayLogDTO;
import ops.school.api.enums.ResponseViewEnums;
import ops.school.api.util.LimitTableData;
import ops.school.api.enums.PublicErrorEnums;
import ops.school.api.util.ResponseObject;
import ops.school.api.vo.card.CardPayLogVO;
import ops.school.api.exception.Assertions;


@Controller
@RequestMapping("/api/card/pay")
public class CardPayLogController {

    @Autowired
    private CardPayLogService cardPayLogService;


    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.LimitTableData<CardPayLogVO>
     * @param   dto
     * @Desc:   desc 分页查询
     */
    @ResponseBody
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    public ResponseObject limitTableData(CardPayLogDTO dto){
        Assertions.notNull(dto,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        LimitTableData<CardPayLogVO> tableData = cardPayLogService.limitTableDataByDTO(dto);
        return new ResponseObject(true, ResponseViewEnums.SUCCESS)
                .push("total",tableData.getRecordsTotal())
                .push("list",tableData.getData());
    }

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: java.util.List<CardPayLogVO>
     * @param
     * @Desc:   desc 查询所有数据
     */
    @ResponseBody
    @RequestMapping(value = "/all",method = RequestMethod.POST)
    public List<CardPayLogVO> findAllCardPayLogVOs(){
        List<CardPayLogVO> allCardPayLogVOs = cardPayLogService.findAllCardPayLogVOs();
        return allCardPayLogVOs;
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
    public ResponseObject saveOneCardPayLogByDTO(CardPayLogDTO dto){
        Assertions.notNull(dto,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        ResponseObject view = cardPayLogService.saveOneCardPayLogByDTO(dto);
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
    public ResponseObject updateOneCardPayLogByDTO(CardPayLogDTO dto){
        Assertions.notNull(dto,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        ResponseObject view = cardPayLogService.updateOneCardPayLogByDTO(dto);
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
    public ResponseObject deleteOneCardPayLogById(Long id){
        Assertions.notNull(id,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        ResponseObject view = cardPayLogService.deleteOneCardPayLogById(id);
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
    public CardPayLogVO findOneCardPayLogById(Long id){
        Assertions.notNull(id,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        CardPayLogVO resultVO = cardPayLogService.findOneCardPayLogById(id);
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
    public ResponseObject stopOneCardPayLogById(Long id){
        Assertions.notNull(id,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        ResponseObject view = cardPayLogService.stopOneCardPayLogById(id);
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
    public ResponseObject startOneCardPayLogById(Long id){
        Assertions.notNull(id,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        ResponseObject view = cardPayLogService.startOneCardPayLogById(id);
        return view;
    }

}
