package ops.school.api.dao.card;

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

}
