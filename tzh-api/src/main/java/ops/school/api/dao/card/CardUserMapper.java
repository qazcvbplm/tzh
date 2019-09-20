package ops.school.api.dao.card;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ops.school.api.dto.card.CardUserDTO;
import ops.school.api.vo.card.CardUserVO;
import org.apache.ibatis.annotations.Param;


public interface CardUserMapper extends BaseMapper<CardUserVO> {

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: java.lang.Integer
     * @param   dto
     * @Desc:   desc 分页查询统计
     */
    Integer countLimitByDTO(CardUserDTO dto);

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.LimitTableData<CardUserVO>
     * @param   dto
     * @Desc:   desc 分页查询
     */
    List<CardUserVO> selectLimitByDTO(CardUserDTO dto);

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.LimitTableData<CardUserVO>
     * @param   ids
     * @Desc:   desc 批量查询
     */
    List<CardUserVO> batchFindByIds(@Param("list") List<Long> ids);

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
     * @date:   2019/9/20 20:24
     * @author: QinDaoFang
     * @version:version
     * @return: java.util.List<ops.school.api.vo.card.CardUserVO>
     * @param   dto
     * @Desc:   desc
     */
    List<CardUserVO> findCardUserList(CardUserDTO dto);
}
