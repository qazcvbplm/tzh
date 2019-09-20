package ops.school.api.service.card;

import java.util.Date;
import java.util.List;
import java.util.Map;

import ops.school.api.enums.ResponseViewEnums;
import ops.school.api.util.LimitTableData;
import ops.school.api.vo.card.CardBuyLogVO;
import ops.school.api.dto.card.CardBuyLogDTO;
import ops.school.api.entity.card.CardBuyLog;
import ops.school.api.enums.PublicErrorEnums;
import ops.school.api.util.ResponseObject;
import ops.school.api.exception.Assertions;

public interface CardBuyLogService {

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.LimitTableData<CardBuyLogVO>
     * @param   dto
     * @Desc:   desc 分页查询
     */
    LimitTableData<CardBuyLogVO> limitTableDataByDTO(CardBuyLogDTO dto);

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: java.util.List<CardBuyLogVO>
     * @param
     * @Desc:   desc 查询所有数据
     */
    List<CardBuyLogVO> findAllCardBuyLogVOs();

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.ResponseView
     * @param   dto
     * @Desc:   desc 通过DTO新增
     */
    ResponseObject saveOneCardBuyLogByDTO(CardBuyLogDTO dto);

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.ResponseView
     * @param   dto
     * @Desc:   desc 通过DTO更新
     */
    ResponseObject updateOneCardBuyLogByDTO(CardBuyLogDTO dto);

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.ResponseView
     * @param   id
     * @Desc:   desc 通过id删除
     */
    ResponseObject deleteOneCardBuyLogById(Long id);

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.ResponseView
     * @param   id
     * @Desc:   desc 通过id查询一个
     */
    CardBuyLogVO findOneCardBuyLogById(Long id);

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: java.util.List<CardBuyLogVO>
     * @param   ids
     * @Desc:   desc 通过id集合查询
     */
    List<CardBuyLogVO> batchFindCardBuyLogByIds(List<Long> ids);

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.ResponseView
     * @param   id
     * @Desc:   desc 通过id停用
     */
    ResponseObject stopOneCardBuyLogById(Long id);

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.ResponseView
     * @param   id
     * @Desc:   desc 通过id启用
     */
    ResponseObject startOneCardBuyLogById(Long id);

    /**
     * @date:   2019/9/19 16:15
     * @author: QinDaoFang
     * @version:version
     * @return: java.lang.Long
     * @param   cardBuyLogDTO
     * @Desc:   desc
     */
    Integer saveOneCardBuyLogByDTOGetId(CardBuyLogDTO cardBuyLogDTO);

    /**
     * @date:   2019/9/20 16:24
     * @author: QinDaoFang
     * @version:version
     * @return: ops.school.api.vo.card.CardBuyLogVO
     * @param   id
     * @param   dayBegin
     * @param   dayEnd
     * @Desc:   desc
     */
    CardBuyLogVO dayCountBuyMoneyByTime(Integer id, Date dayBegin, Date dayEnd);
}
