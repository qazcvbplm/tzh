package ops.school.api.serviceimple.card;

import java.util.List;
import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ops.school.api.constants.NumConstants;
import ops.school.api.dao.card.ClubCardSendMapper;
import ops.school.api.dto.card.ClubCardSendDTO;
import ops.school.api.entity.card.ClubCardSend;
import ops.school.api.enums.PublicErrorEnums;
import ops.school.api.enums.ResponseViewEnums;
import ops.school.api.exception.Assertions;
import ops.school.api.exception.DisplayException;
import ops.school.api.service.card.ClubCardSendService;
import ops.school.api.util.LimitTableData;
import ops.school.api.util.ResponseObject;
import ops.school.api.vo.card.ClubCardSendVO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.stereotype.Service;

@Service(value = "clubCardSendService")
public class ClubCardSendServiceIMPL implements ClubCardSendService {

    @Resource(name="clubCardSendMapper")
    private ClubCardSendMapper clubCardSendMapper;

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.LimitTableData<ClubCardSendVO>
     * @param   limitDTO
     * @Desc:   desc 分页查询
     */
    @Override
    public LimitTableData<ClubCardSendVO> limitTableDataByDTO(ClubCardSendDTO limitDTO) {
        LimitTableData<ClubCardSendVO> tableData = new LimitTableData();
        Integer countNum = clubCardSendMapper.countLimitByDTO(limitDTO);
        if (countNum < NumConstants.INTEGER_NUM_1){
            return tableData;
        }
        IPage<ClubCardSendVO> page = new Page<>();
        List<ClubCardSendVO> clubCardSendVOS = clubCardSendMapper.selectLimitByDTO(limitDTO);
        if (CollectionUtils.isEmpty(clubCardSendVOS)){
            tableData.setRecordsTotal(countNum);
            return tableData;
        }
        tableData.setRecordsTotal(countNum);
        tableData.setData(clubCardSendVOS);
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
     @Override
    public List<ClubCardSendVO> findAllClubCardSendVOs() {
        List<ClubCardSendVO> ClubCardSendVOS = clubCardSendMapper.selectList(new QueryWrapper<ClubCardSendVO>());
        return ClubCardSendVOS;
    }

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.ResponseObject
     * @param   saveDTO
     * @Desc:   desc 通过DTO新增
     */
    @Override
    public ResponseObject saveOneClubCardSendByDTO(@Valid ClubCardSendDTO saveDTO) {
        Assertions.notNull(saveDTO, PublicErrorEnums.PULBIC_EMPTY_PARAM);
        Assertions.notNull(saveDTO.getWxUserId(),ResponseViewEnums.WX_USER_NEED_USER_ID);
        //设置日期格式
        saveDTO.setCreateId(saveDTO.getWxUserId());
        saveDTO.setUpdateId(saveDTO.getWxUserId());
        ClubCardSendVO saveVO = saveDTO.toVO();
        Integer saveNum = clubCardSendMapper.insert(saveVO);
        if (saveNum != NumConstants.INT_NUM_1){
            return new ResponseObject(false, ResponseViewEnums.FAILED);
        }
        return new ResponseObject(true,ResponseViewEnums.SUCCESS);
    }

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.ResponseObject
     * @param   updateDTO
     * @Desc:   desc 通过DTO更新
     */
    @Override
    public ResponseObject updateOneClubCardSendByDTO(ClubCardSendDTO updateDTO) {
        Assertions.notNull(updateDTO,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        Assertions.notNull(updateDTO.getId(),PublicErrorEnums.PULBIC_EMPTY_PARAM);
        ClubCardSendVO clubCardSendVO = new ClubCardSendVO();
        BeanUtils.copyProperties(updateDTO,clubCardSendVO);
        //设置日期格式
        updateDTO.setCreateTime(new Date());
        updateDTO.setUpdateTime(new Date());
        int updateNum = clubCardSendMapper.updateById(clubCardSendVO);
        if (updateNum < NumConstants.INTEGER_NUM_1){
            DisplayException.throwMessageWithEnum(PublicErrorEnums.PUBLIC_DO_FAILED);
        }
        return new ResponseObject();
    }
    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.ResponseObject
     * @param   id
     * @Desc:   desc 通过id删除
     */
    @Override
    public ResponseObject deleteOneClubCardSendById(Long id){
        Assertions.notNull(id,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        int deleteNum = clubCardSendMapper.deleteById(id);
        if (deleteNum < NumConstants.INT_NUM_1){
            DisplayException.throwMessageWithEnum(PublicErrorEnums.PUBLIC_DO_FAILED);
        }
        return new ResponseObject();
    }
    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: ClubCardSendVO
     * @param   id
     * @Desc:   desc 通过id查询一个
     */
    @Override
    public ClubCardSendVO findOneClubCardSendById(Long id) {
        Assertions.notNull(id,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        ClubCardSendVO findVO = clubCardSendMapper.selectById(id);
        return findVO;
    }

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: java.util.List<ClubCardSendVO>
     * @param   ids
     * @Desc:   desc 通过id集合查询
     */
    @Override
    public List<ClubCardSendVO> batchFindClubCardSendByIds(List<Long> ids){
        if (CollectionUtils.isEmpty(ids)){
            DisplayException.throwMessageWithEnum(PublicErrorEnums.PULBIC_EMPTY_PARAM);
        }
        List<ClubCardSendVO> batchFindVOSByIds = clubCardSendMapper.batchFindByIds(ids);
        return batchFindVOSByIds;
    }

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.ResponseObject
     * @param   id
     * @Desc:   desc 通过id停用
     */
    @Override
    public ResponseObject stopOneClubCardSendById(Long id) {
        Assertions.notNull(id,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        Integer stopNum = clubCardSendMapper.stopOneById(id);
        if (stopNum < NumConstants.INT_NUM_1){
            DisplayException.throwMessageWithEnum(PublicErrorEnums.PUBLIC_DO_FAILED);
        }
        return new ResponseObject();
    }

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.ResponseObject
     * @param   id
     * @Desc:   desc 通过id启用
     */
    @Override
    public ResponseObject startOneClubCardSendById(Long id) {
        Assertions.notNull(id,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        Integer stopNum = clubCardSendMapper.startOneById(id);
        if (stopNum < NumConstants.INT_NUM_1){
            DisplayException.throwMessageWithEnum(PublicErrorEnums.PUBLIC_DO_FAILED);
        }
        return new ResponseObject();
    }

}
