package ops.school.api.dao.card;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ops.school.api.dto.card.CardPayLogDTO;
import ops.school.api.vo.card.CardPayLogVO;
import org.apache.ibatis.annotations.Param;


public interface CardPayLogMapper extends BaseMapper<CardPayLogVO> {

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: java.lang.Integer
     * @param   dto
     * @Desc:   desc 分页查询统计
     */
    Integer countLimitByDTO(CardPayLogDTO dto);

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.LimitTableData<CardPayLogVO>
     * @param   dto
     * @Desc:   desc 分页查询
     */
    List<CardPayLogVO> selectLimitByDTO(CardPayLogDTO dto);

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.LimitTableData<CardPayLogVO>
     * @param   ids
     * @Desc:   desc 批量查询
     */
    List<CardPayLogVO> batchFindByIds(@Param("list") List<Long> ids);

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
     * @date:   2019/9/20 12:17
     * @author: QinDaoFang
     * @version:version
     * @return: java.util.List<ops.school.api.vo.card.CardPayLogVO>
     * @param   cardUserIdList
     * @Desc:   desc
     */
    List<CardPayLogVO> batchFindCardPayLogByCUIds(@Param("list") List<Long> cardUserIdList);
}
