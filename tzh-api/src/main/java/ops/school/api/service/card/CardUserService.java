package ops.school.api.service.card;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import ops.school.api.dto.card.CardBuyLogDTO;
import ops.school.api.dto.card.CardUserDTO;
import ops.school.api.entity.card.CardUser;
import ops.school.api.vo.card.CardPayLogVO;
import ops.school.api.vo.card.CardUserVO;
import ops.school.api.enums.ResponseViewEnums;
import ops.school.api.util.LimitTableData;
import ops.school.api.enums.PublicErrorEnums;
import ops.school.api.util.ResponseObject;
import ops.school.api.exception.Assertions;
import ops.school.api.vo.card.ClubCardSendVO;

public interface CardUserService {

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.LimitTableData<CardUserVO>
     * @param   dto
     * @Desc:   desc 分页查询
     */
    LimitTableData<CardUserVO> limitTableDataByDTO(CardUserDTO dto);

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: java.util.List<CardUserVO>
     * @param
     * @Desc:   desc 查询所有数据
     */
    List<CardUserVO> findAllCardUserVOs();

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.ResponseView
     * @param   dto
     * @Desc:   desc 通过DTO新增
     */
    ResponseObject saveOneCardUserByDTO(CardUserDTO dto);

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.ResponseView
     * @param   dto
     * @Desc:   desc 通过DTO更新
     */
    ResponseObject updateOneCardUserByDTO(CardUserDTO dto);

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.ResponseView
     * @param   id
     * @Desc:   desc 通过id删除
     */
    ResponseObject deleteOneCardUserById(Long id);

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.ResponseView
     * @param   id
     * @Desc:   desc 通过id查询一个
     */
    CardUserVO findOneCardUserById(Long id);

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: java.util.List<CardUserVO>
     * @param   ids
     * @Desc:   desc 通过id集合查询
     */
    List<CardUserVO> batchFindCardUserByIds(List<Long> ids);

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.ResponseView
     * @param   id
     * @Desc:   desc 通过id停用
     */
    ResponseObject stopOneCardUserById(Long id);

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.ResponseView
     * @param   id
     * @Desc:   desc 通过id启用
     */
    ResponseObject startOneCardUserById(Long id);

    /**
     * @date:   2019/9/19 15:51
     * @author: QinDaoFang
     * @version:version
     * @return: ops.school.api.util.ResponseObject
     * @param   dto
     * @Desc:   desc
     */
    ResponseObject payOneCardUserByDTO(CardUserDTO dto);

    /**
     * @date:   2019/9/19 16:56
     * @author: QinDaoFang
     * @version:version
     * @return: ops.school.api.util.ResponseObject
     * @param   cardBuyLogDTO
     * @Desc:   desc
     */
    ResponseObject notifyAndAddCardUserByBuyLogDTO(CardBuyLogDTO cardBuyLogDTO);


    /**
     * @date:   2019/9/20 10:44
     * @author: QinDaoFang
     * @version:version
     * @return: java.lang.Boolean
     * @param   clubCardSendVO
     * @param   cardUserVO
     * @param   payLogVOS
     * @param   schoolId
     * @Desc:   desc
     */
    Boolean checkUserCardTodayCanUseTrue(ClubCardSendVO clubCardSendVO, CardUserVO cardUserVO, List<CardPayLogVO> payLogVOS,Long schoolId);

    /**
     * @date:   2019/9/20 14:28
     * @author: QinDaoFang
     * @version:version
     * @return: java.lang.Boolean
     * @param   clubCardSendVO
     * @param   cardUserVO
     * @param   times
     * @param   sendPrice
     * @param   valueOf
     * @Desc:   desc
     */
    Boolean checkUserCardTodayCanUseTrueByMoney(ClubCardSendVO clubCardSendVO, CardUserVO cardUserVO, Integer times, BigDecimal sendPrice, Long valueOf);

    /**
     * @date:   2019/9/20 20:23
     * @author: QinDaoFang
     * @version:version
     * @return: java.util.List<ops.school.api.vo.card.CardUserVO>
     * @param   dto
     * @Desc:   desc
     */
    List<CardUserVO> findCardUserList(CardUserDTO dto);
}
