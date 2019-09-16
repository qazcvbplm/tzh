package ops.school.api.service.card;

import java.util.List;
import java.util.Map;

import ops.school.api.dto.card.ClubCardSendDTO;
import ops.school.api.util.LimitTableData;
import ops.school.api.util.ResponseObject;
import ops.school.api.vo.card.ClubCardSendVO;

public interface ClubCardSendService {

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.LimitTableData<ClubCardSendVO>
     * @param   dto
     * @Desc:   desc 分页查询
     */
    LimitTableData<ClubCardSendVO> limitTableDataByDTO(ClubCardSendDTO dto);

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: java.util.List<ClubCardSendVO>
     * @param
     * @Desc:   desc 查询所有数据
     */
    List<ClubCardSendVO> findAllClubCardSendVOs();

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.ResponseObject
     * @param   dto
     * @Desc:   desc 通过DTO新增
     */
    ResponseObject saveOneClubCardSendByDTO(ClubCardSendDTO dto);

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.ResponseObject
     * @param   dto
     * @Desc:   desc 通过DTO更新
     */
    ResponseObject updateOneClubCardSendByDTO(ClubCardSendDTO dto);

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.ResponseObject
     * @param   id
     * @Desc:   desc 通过id删除
     */
    ResponseObject deleteOneClubCardSendById(Long id);

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.ResponseObject
     * @param   id
     * @Desc:   desc 通过id查询一个
     */
    ClubCardSendVO findOneClubCardSendById(Long id);

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: java.util.List<ClubCardSendVO>
     * @param   ids
     * @Desc:   desc 通过id集合查询
     */
    List<ClubCardSendVO> batchFindClubCardSendByIds(List<Long> ids);

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.ResponseObject
     * @param   id
     * @Desc:   desc 通过id停用
     */
    ResponseObject stopOneClubCardSendById(Long id);

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.ResponseObject
     * @param   id
     * @Desc:   desc 通过id启用
     */
    ResponseObject startOneClubCardSendById(Long id);
}
