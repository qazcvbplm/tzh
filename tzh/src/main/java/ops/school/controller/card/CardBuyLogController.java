package ops.school.controller.card;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import ops.school.api.service.card.CardBuyLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ops.school.api.enums.ResponseViewEnums;
import ops.school.api.util.LimitTableData;
import ops.school.api.vo.card.CardBuyLogVO;
import ops.school.api.dto.card.CardBuyLogDTO;
import ops.school.api.entity.card.CardBuyLog;
import ops.school.api.enums.PublicErrorEnums;
import ops.school.api.util.ResponseObject;
import ops.school.api.exception.Assertions;


@Controller
@RequestMapping("/api/cardBuyLog")
public class CardBuyLogController {

    @Resource(name="cardBuyLogService")
    private CardBuyLogService cardBuyLogService;


    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.LimitTableData<CardBuyLogVO>
     * @param   dto
     * @Desc:   desc 分页查询
     */
    @ResponseBody
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    public ResponseObject limitTableData(CardBuyLogDTO dto){
        Assertions.notNull(dto,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        LimitTableData<CardBuyLogVO> tableData = cardBuyLogService.limitTableDataByDTO(dto);
        return new ResponseObject(true, ResponseViewEnums.SUCCESS)
                .push("total",tableData.getRecordsTotal())
                .push("list",tableData.getData());
    }

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: java.util.List<CardBuyLogVO>
     * @param
     * @Desc:   desc 查询所有数据
     */
    @ResponseBody
    @RequestMapping(value = "/all",method = RequestMethod.POST)
    public List<CardBuyLogVO> findAllCardBuyLogVOs(){
        List<CardBuyLogVO> allCardBuyLogVOs = cardBuyLogService.findAllCardBuyLogVOs();
        return allCardBuyLogVOs;
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
    public ResponseObject saveOneCardBuyLogByDTO(CardBuyLogDTO dto){
        Assertions.notNull(dto,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        ResponseObject view = cardBuyLogService.saveOneCardBuyLogByDTO(dto);
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
    public ResponseObject updateOneCardBuyLogByDTO(CardBuyLogDTO dto){
        Assertions.notNull(dto,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        ResponseObject view = cardBuyLogService.updateOneCardBuyLogByDTO(dto);
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
    public ResponseObject deleteOneCardBuyLogById(Long id){
        Assertions.notNull(id,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        ResponseObject view = cardBuyLogService.deleteOneCardBuyLogById(id);
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
    public CardBuyLogVO findOneCardBuyLogById(Long id){
        Assertions.notNull(id,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        CardBuyLogVO resultVO = cardBuyLogService.findOneCardBuyLogById(id);
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
    public ResponseObject stopOneCardBuyLogById(Long id){
        Assertions.notNull(id,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        ResponseObject view = cardBuyLogService.stopOneCardBuyLogById(id);
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
    public ResponseObject startOneCardBuyLogById(Long id){
        Assertions.notNull(id,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        ResponseObject view = cardBuyLogService.startOneCardBuyLogById(id);
        return view;
    }

}
