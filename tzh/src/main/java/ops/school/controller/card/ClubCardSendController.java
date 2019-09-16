package ops.school.controller.card;

import java.util.List;

import javax.annotation.Resource;

import ops.school.api.dto.card.ClubCardSendDTO;
import ops.school.api.enums.PublicErrorEnums;
import ops.school.api.exception.Assertions;
import ops.school.api.service.card.ClubCardSendService;
import ops.school.api.util.LimitTableData;
import ops.school.api.util.ResponseObject;
import ops.school.api.vo.card.ClubCardSendVO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/ops/card")
public class ClubCardSendController{

    @Resource(name="clubCardSendService")
    private ClubCardSendService clubCardSendService;


    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.LimitTableData<ClubCardSendVO>
     * @param   dto
     * @Desc:   desc 分页查询
     */
    @ResponseBody
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    public LimitTableData<ClubCardSendVO> limitTableData(ClubCardSendDTO dto){
        Assertions.notNull(dto, PublicErrorEnums.PULBIC_EMPTY_PARAM);
        LimitTableData<ClubCardSendVO> tableData = clubCardSendService.limitTableDataByDTO(dto);
        return tableData;
    }

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: java.util.List<ClubCardSendVO>
     * @param
     * @Desc:   desc 查询所有数据
     */
    @ResponseBody
    @RequestMapping(value = "/all",method = RequestMethod.POST)
    public List<ClubCardSendVO> findAllClubCardSendVOs(){
        List<ClubCardSendVO> allClubCardSendVOs = clubCardSendService.findAllClubCardSendVOs();
        return allClubCardSendVOs;
    }

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.ResponseObject
     * @param   dto
     * @Desc:   desc 通过DTO新增
     */
    @ResponseBody
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public ResponseObject saveOneClubCardSendByDTO(ClubCardSendDTO dto){
        Assertions.notNull(dto,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        ResponseObject view = clubCardSendService.saveOneClubCardSendByDTO(dto);
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
    @ResponseBody
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public ResponseObject updateOneClubCardSendByDTO(ClubCardSendDTO dto){
        Assertions.notNull(dto,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        ResponseObject view = clubCardSendService.updateOneClubCardSendByDTO(dto);
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
    @ResponseBody
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public ResponseObject deleteOneClubCardSendById(Long id){
        Assertions.notNull(id,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        ResponseObject view = clubCardSendService.deleteOneClubCardSendById(id);
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
    @ResponseBody
    @RequestMapping(value = "/one",method = RequestMethod.POST)
    public ClubCardSendVO findOneClubCardSendById(Long id){
        Assertions.notNull(id,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        ClubCardSendVO resultVO = clubCardSendService.findOneClubCardSendById(id);
        return resultVO;
    }

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.ResponseObject
     * @param   id
     * @Desc:   desc 通过id停用
     */
    @ResponseBody
    @RequestMapping(value = "/stop",method = RequestMethod.POST)
    public ResponseObject stopOneClubCardSendById(Long id){
        Assertions.notNull(id,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        ResponseObject view = clubCardSendService.stopOneClubCardSendById(id);
        return view;
    }

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.ResponseObject
     * @param   id
     * @Desc:   desc 通过id启用
     */
    @ResponseBody
    @RequestMapping(value = "/start",method = RequestMethod.POST)
    public ResponseObject startOneClubCardSendById(Long id){
        Assertions.notNull(id,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        ResponseObject view = clubCardSendService.startOneClubCardSendById(id);
        return view;
    }

}
