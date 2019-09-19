package ops.school.controller.card;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import ops.school.api.service.card.CardUserService;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/ops/card/user")
public class CardUserController {

    @Autowired
    private CardUserService cardUserService;


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
        LimitTableData<CardUserVO> tableData = cardUserService.limitTableDataByDTO(dto);
        return new ResponseObject(true, ResponseViewEnums.SUCCESS)
                .push("total",tableData.getRecordsTotal())
                .push("list",tableData.getData());
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
    @RequestMapping(value = "/get",method = RequestMethod.POST)
    public ResponseObject saveOneCardUserByDTO(CardUserDTO dto){
        Assertions.notNull(dto,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        Assertions.notNull(dto.getCardId(),PublicErrorEnums.PULBIC_EMPTY_PARAM);
        Assertions.notNull(dto.getOpenId(),PublicErrorEnums.PULBIC_EMPTY_PARAM);
        Assertions.notNull(dto.getSchoolId(),PublicErrorEnums.PULBIC_EMPTY_PARAM);
        ResponseObject view = cardUserService.saveOneCardUserByDTO(dto);
        return view;
    }

}
