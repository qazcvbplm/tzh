package ops.school.api.service.card;

import java.util.List;
import java.util.Map;

import ops.school.api.entity.card.CardPayLog;
import ops.school.api.dto.card.CardPayLogDTO;
import ops.school.api.enums.ResponseViewEnums;
import ops.school.api.util.LimitTableData;
import ops.school.api.enums.PublicErrorEnums;
import ops.school.api.util.ResponseObject;
import ops.school.api.vo.card.CardPayLogVO;
import ops.school.api.exception.Assertions;

public interface CardPayLogService {

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.LimitTableData<CardPayLogVO>
     * @param   dto
     * @Desc:   desc 分页查询
     */
    LimitTableData<CardPayLogVO> limitTableDataByDTO(CardPayLogDTO dto);

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: java.util.List<CardPayLogVO>
     * @param
     * @Desc:   desc 查询所有数据
     */
    List<CardPayLogVO> findAllCardPayLogVOs();

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.ResponseView
     * @param   dto
     * @Desc:   desc 通过DTO新增
     */
    ResponseObject saveOneCardPayLogByDTO(CardPayLogDTO dto);

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.ResponseView
     * @param   dto
     * @Desc:   desc 通过DTO更新
     */
    ResponseObject updateOneCardPayLogByDTO(CardPayLogDTO dto);

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.ResponseView
     * @param   id
     * @Desc:   desc 通过id删除
     */
    ResponseObject deleteOneCardPayLogById(Long id);

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.ResponseView
     * @param   id
     * @Desc:   desc 通过id查询一个
     */
    CardPayLogVO findOneCardPayLogById(Long id);

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: java.util.List<CardPayLogVO>
     * @param   ids
     * @Desc:   desc 通过id集合查询
     */
    List<CardPayLogVO> batchFindCardPayLogByIds(List<Long> ids);

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.ResponseView
     * @param   id
     * @Desc:   desc 通过id停用
     */
    ResponseObject stopOneCardPayLogById(Long id);

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.ResponseView
     * @param   id
     * @Desc:   desc 通过id启用
     */
    ResponseObject startOneCardPayLogById(Long id);
}
