package ops.school.api.serviceimple.card;

import java.util.List;
import java.util.Map;
import java.util.Date;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import ops.school.api.constants.NumConstants;
import ops.school.api.dao.card.CardUserMapper;
import ops.school.api.exception.DisplayException;
import ops.school.api.service.card.CardUserService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ops.school.api.dto.card.CardUserDTO;
import ops.school.api.entity.card.CardUser;
import ops.school.api.vo.card.CardUserVO;
import ops.school.api.enums.ResponseViewEnums;
import ops.school.api.util.LimitTableData;
import ops.school.api.enums.PublicErrorEnums;
import ops.school.api.util.ResponseObject;
import ops.school.api.exception.Assertions;
import org.springframework.beans.BeanUtils;

@Service(value = "cardUserService")
public class CardUserServiceIMPL implements CardUserService {

    @Autowired
    private CardUserMapper cardUserMapper;

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.LimitTableData<CardUserVO>
     * @param   limitDTO
     * @Desc:   desc 分页查询
     */
    @Override
    public LimitTableData<CardUserVO> limitTableDataByDTO(CardUserDTO limitDTO) {
        LimitTableData<CardUserVO> tableData = new LimitTableData();
        Integer countNum = cardUserMapper.countLimitByDTO(limitDTO);
        if (countNum < NumConstants.INTEGER_NUM_1){
            return tableData;
        }
        List<CardUserVO> cardUserVOS = cardUserMapper.selectLimitByDTO(limitDTO);
        if (CollectionUtils.isEmpty(cardUserVOS)){
            tableData.setRecordsTotal(countNum);
            return tableData;
        }
        tableData.setRecordsTotal(countNum);
        tableData.setData(cardUserVOS);
        return tableData;
    }

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: java.util.List<CardUserVO>
     * @param
     * @Desc:   desc 查询所有数据
     */
     @Override
    public List<CardUserVO> findAllCardUserVOs() {
        List<CardUserVO> CardUserVOS = cardUserMapper.selectList(new QueryWrapper<CardUserVO>());
        return CardUserVOS;
    }

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.ResponseView
     * @param   saveDTO
     * @Desc:   desc 通过DTO新增
     */
    @Override
    public ResponseObject saveOneCardUserByDTO(@Valid CardUserDTO saveDTO) {
        Assertions.notNull(saveDTO,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        //设置日期格式
        saveDTO.setCreateTime(new Date());
        saveDTO.setUpdateTime(new Date());
        CardUserVO saveVO = new CardUserVO();
        BeanUtils.copyProperties(saveDTO,saveVO);
        Integer saveNum = cardUserMapper.insert(saveVO);
        if (saveNum != NumConstants.INT_NUM_1){
            return new ResponseObject(false,ResponseViewEnums.FAILED);
        }
        return new ResponseObject(true,ResponseViewEnums.SUCCESS);
    }

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.ResponseView
     * @param   updateDTO
     * @Desc:   desc 通过DTO更新
     */
    @Override
    public ResponseObject updateOneCardUserByDTO(CardUserDTO updateDTO) {
        Assertions.notNull(updateDTO,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        Assertions.notNull(updateDTO.getId(),PublicErrorEnums.PULBIC_EMPTY_PARAM);
        int updateNum = cardUserMapper.updateById(updateDTO.toVO());
        if (updateNum < NumConstants.INTEGER_NUM_1){
            DisplayException.throwMessageWithEnum(PublicErrorEnums.PUBLIC_DO_FAILED);
        }
        return new ResponseObject(true,ResponseViewEnums.SUCCESS);
    }
    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.ResponseView
     * @param   id
     * @Desc:   desc 通过id删除
     */
    @Override
    public ResponseObject deleteOneCardUserById(Long id){
        Assertions.notNull(id,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        int deleteNum = cardUserMapper.deleteById(id);
        if (deleteNum < NumConstants.INT_NUM_1){
            DisplayException.throwMessageWithEnum(PublicErrorEnums.PUBLIC_DO_FAILED);
        }
        return new ResponseObject(true,ResponseViewEnums.SUCCESS);
    }
    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: CardUserVO
     * @param   id
     * @Desc:   desc 通过id查询一个
     */
    @Override
    public CardUserVO findOneCardUserById(Long id) {
        Assertions.notNull(id,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        CardUserVO findVO = cardUserMapper.selectById(id);
        return findVO;
    }

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: java.util.List<CardUserVO>
     * @param   ids
     * @Desc:   desc 通过id集合查询
     */
    @Override
    public List<CardUserVO> batchFindCardUserByIds(List<Long> ids){
        if (CollectionUtils.isEmpty(ids)){
            DisplayException.throwMessageWithEnum(PublicErrorEnums.PULBIC_EMPTY_PARAM);
        }
        List<CardUserVO> batchFindVOSByIds = cardUserMapper.batchFindByIds(ids);
        return batchFindVOSByIds;
    }

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.ResponseView
     * @param   id
     * @Desc:   desc 通过id停用
     */
    @Override
    public ResponseObject stopOneCardUserById(Long id) {
        Assertions.notNull(id,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        Integer stopNum = cardUserMapper.stopOneById(id);
        if (stopNum < NumConstants.INT_NUM_1){
            DisplayException.throwMessageWithEnum(PublicErrorEnums.PUBLIC_DO_FAILED);
        }
        return new ResponseObject(true,ResponseViewEnums.SUCCESS);
    }

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.ResponseView
     * @param   id
     * @Desc:   desc 通过id启用
     */
    @Override
    public ResponseObject startOneCardUserById(Long id) {
        Assertions.notNull(id,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        Integer stopNum = cardUserMapper.startOneById(id);
        if (stopNum < NumConstants.INT_NUM_1){
            DisplayException.throwMessageWithEnum(PublicErrorEnums.PUBLIC_DO_FAILED);
        }
        return new ResponseObject(true,ResponseViewEnums.SUCCESS);
    }

}
