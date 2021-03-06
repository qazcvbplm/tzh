package ops.school.api.dao.card;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ops.school.api.dto.card.CardBuyLogDTO;
import ops.school.api.vo.card.CardBuyLogVO;
import org.apache.ibatis.annotations.Param;


public interface CardBuyLogMapper extends BaseMapper<CardBuyLogVO> {

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: java.lang.Integer
     * @param   dto
     * @Desc:   desc 分页查询统计
     */
    Integer countLimitByDTO(CardBuyLogDTO dto);

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.LimitTableData<CardBuyLogVO>
     * @param   dto
     * @Desc:   desc 分页查询
     */
    List<CardBuyLogVO> selectLimitByDTO(CardBuyLogDTO dto);

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.LimitTableData<CardBuyLogVO>
     * @param   ids
     * @Desc:   desc 批量查询
     */
    List<CardBuyLogVO> batchFindByIds(@Param("list") List<Long> ids);

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: java.lang.Integer
     * @param   id
     * @Desc:   desc 通过id停用
     */
    Integer stopOneById(Long id);

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: java.lang.Integer
     * @param   id
     * @Desc:   desc 通过id启用
     */
    Integer startOneById(Long id);

    /**
     * @date:   2019/9/19 16:17
     * @author: QinDaoFang
     * @version:version
     * @return: java.lang.Long
     * @param   cardBuyLogDTO
     * @Desc:   desc
     */
    Integer saveOneCardBuyLogByDTOGetId(CardBuyLogDTO cardBuyLogDTO);

    /**
     * @date:   2019/9/20 16:25
     * @author: QinDaoFang
     * @version:version
     * @return: ops.school.api.vo.card.CardBuyLogVO
     * @param   schoolId
     * @param   dayBegin
     * @param   dayEnd
     * @Desc:   desc
     */
    CardBuyLogVO dayCountBuyMoneyByTime(@Param("schoolId") Integer schoolId,@Param("dayBegin") Date dayBegin, @Param("dayEnd") Date dayEnd);
}
