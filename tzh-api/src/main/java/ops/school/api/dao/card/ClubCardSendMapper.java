package ops.school.api.dao.card;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ops.school.api.dto.card.ClubCardSendDTO;
import ops.school.api.vo.card.ClubCardSendVO;
import org.apache.ibatis.annotations.Param;


public interface ClubCardSendMapper extends BaseMapper<ClubCardSendVO> {

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: java.lang.Integer
     * @param   dto
     * @Desc:   desc 分页查询统计
     */
    Integer countLimitByDTO(ClubCardSendDTO dto);

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.LimitTableData<ClubCardSendVO>
     * @param   dto
     * @Desc:   desc 分页查询
     */
    List<ClubCardSendVO> selectLimitByDTO(ClubCardSendDTO dto);

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.LimitTableData<ClubCardSendVO>
     * @param   ids
     * @Desc:   desc 批量查询
     */
    List<ClubCardSendVO> batchFindByIds(@Param("list") List<Long> ids);

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
     * @date:   2019/9/19 13:50
     * @author: QinDaoFang
     * @version:version
     * @return: ops.school.api.vo.card.ClubCardSendVO
     * @param   cardId
     * @Desc:   desc
     */
    ClubCardSendVO selectOneUsedCard(Long cardId);
}
