package ops.school.controller.card;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.swagger.annotations.ApiOperation;
import ops.school.api.dto.card.CardBuyLogDTO;
import ops.school.api.exception.DisplayException;
import ops.school.api.service.card.CardUserService;
import ops.school.api.util.LoggerUtil;
import ops.school.api.wxutil.XMLUtil;
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
import org.xml.sax.SAXException;


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
        Assertions.notNull(dto.getSchoolId(),ResponseViewEnums.SCHOOL_CANT_BE_NULL);
        LimitTableData<CardUserVO> tableData = cardUserService.limitTableDataByDTO(dto);
        return new ResponseObject(true, ResponseViewEnums.SUCCESS)
                .push("total",tableData.getRecordsTotal())
                .push("list",tableData.getData());
    }

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.LimitTableData<CardUserVO>
     * @param   dto
     * @Desc:   desc 分页查询
     */
    @ResponseBody
    @RequestMapping(value = "/find",method = RequestMethod.POST)
    public ResponseObject findCardUserList(CardUserDTO dto){
        Assertions.notNull(dto,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        Assertions.notNull(dto.getSchoolId(),ResponseViewEnums.SCHOOL_CANT_BE_NULL);
        Assertions.notNull(dto.getUserId(),ResponseViewEnums.SCHOOL_CANT_BE_NULL);
        List<CardUserVO> tableData = cardUserService.findCardUserList(dto);
        return new ResponseObject(true, ResponseViewEnums.SUCCESS)
                .push("list",tableData);
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
    @RequestMapping(value = "/pay",method = RequestMethod.POST)
    public ResponseObject payOneCardUserByDTO(CardUserDTO dto){
        Assertions.notNull(dto,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        Assertions.notNull(dto.getCardId(),PublicErrorEnums.PULBIC_EMPTY_PARAM);
        Assertions.notNull(dto.getOpenId(),PublicErrorEnums.PULBIC_EMPTY_PARAM);
        Assertions.notNull(dto.getSchoolId(),PublicErrorEnums.PULBIC_EMPTY_PARAM);
        ResponseObject view = cardUserService.payOneCardUserByDTO(dto);
        return view;
    }

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.ResponseView
     * @Desc:   desc 通过DTO新增
     */
    @ResponseBody
    @RequestMapping(value = "/notify",method = RequestMethod.POST)
    public ResponseObject saveOneCardUserByDTO(HttpServletResponse response,HttpServletRequest request,CardBuyLogDTO paramDTO) throws IOException, SAXException {
        Map<String, String>  map = XMLUtil.parseXML(request);
        // 返回状态码
        String return_code = map.get("return_code");
        // 返回信息
        String return_msg = map.get("return_msg");
        // 业务结果,判断交易是否成功
        String result_code = map.get("result_code");
        if (!"SUCCESS".equals(return_code) || !"SUCCESS".equals(result_code)) {
            DisplayException.throwMessage("购买配送会员卡失败-来自微信回调-信息-"+map.toString());
        }
        //微信得流水号
        String orderId=map.get("out_trade_no");
        //用户openId
        String openId=map.get("openid");
        //cardBuyLog得id
        String attach=map.get("attach");
        Assertions.hasLength(attach,"购买配送会员卡失败-来自微信回调-信息-"+map.toString());
        CardBuyLogDTO cardBuyLogDTO = new CardBuyLogDTO();
        cardBuyLogDTO.setId(Long.valueOf(attach));
        cardBuyLogDTO.setWxTradeNo(orderId);
        cardBuyLogDTO.setOpenId(openId);
        ResponseObject view = cardUserService.notifyAndAddCardUserByBuyLogDTO(cardBuyLogDTO);
        return view;
    }

}
