package ops.school.api.serviceimple.card;

import java.util.List;
import java.util.Map;
import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import ops.school.api.constants.NumConstants;
import ops.school.api.dao.card.CardBuyLogMapper;
import ops.school.api.exception.DisplayException;
import ops.school.api.service.card.CardBuyLogService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ops.school.api.enums.ResponseViewEnums;
import ops.school.api.util.LimitTableData;
import ops.school.api.vo.card.CardBuyLogVO;
import ops.school.api.dto.card.CardBuyLogDTO;
import ops.school.api.entity.card.CardBuyLog;
import ops.school.api.enums.PublicErrorEnums;
import ops.school.api.util.ResponseObject;
import ops.school.api.exception.Assertions;
import org.springframework.beans.BeanUtils;

@Service(value = "cardBuyLogService")
public class CardBuyLogServiceIMPL implements CardBuyLogService {

    @Autowired
    private CardBuyLogMapper cardBuyLogMapper;

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.LimitTableData<CardBuyLogVO>
     * @param   limitDTO
     * @Desc:   desc 分页查询
     */
    @Override
    public LimitTableData<CardBuyLogVO> limitTableDataByDTO(CardBuyLogDTO limitDTO) {
        LimitTableData<CardBuyLogVO> tableData = new LimitTableData();
        Integer countNum = cardBuyLogMapper.countLimitByDTO(limitDTO);
        if (countNum < NumConstants.INTEGER_NUM_1){
            return tableData;
        }
        List<CardBuyLogVO> cardBuyLogVOS = cardBuyLogMapper.selectLimitByDTO(limitDTO);
        if (CollectionUtils.isEmpty(cardBuyLogVOS)){
            tableData.setRecordsTotal(countNum);
            return tableData;
        }
        tableData.setRecordsTotal(countNum);
        tableData.setData(cardBuyLogVOS);
        return tableData;
    }

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: java.util.List<CardBuyLogVO>
     * @param
     * @Desc:   desc 查询所有数据
     */
     @Override
    public List<CardBuyLogVO> findAllCardBuyLogVOs() {
        List<CardBuyLogVO> CardBuyLogVOS = cardBuyLogMapper.selectList(new QueryWrapper<CardBuyLogVO>());
        return CardBuyLogVOS;
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
    public ResponseObject saveOneCardBuyLogByDTO(@Valid CardBuyLogDTO saveDTO) {
        Assertions.notNull(saveDTO,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        //设置日期格式
        saveDTO.setCreateTime(new Date());
        saveDTO.setUpdateTime(new Date());
        CardBuyLogVO saveVO = new CardBuyLogVO();
        BeanUtils.copyProperties(saveDTO,saveVO);
        Integer saveNum = cardBuyLogMapper.insert(saveVO);
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
    public ResponseObject updateOneCardBuyLogByDTO(CardBuyLogDTO updateDTO) {
        Assertions.notNull(updateDTO,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        Assertions.notNull(updateDTO.getId(),PublicErrorEnums.PULBIC_EMPTY_PARAM);
        int updateNum = cardBuyLogMapper.updateById(updateDTO.toVO());
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
    public ResponseObject deleteOneCardBuyLogById(Long id){
        Assertions.notNull(id,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        int deleteNum = cardBuyLogMapper.deleteById(id);
        if (deleteNum < NumConstants.INT_NUM_1){
            DisplayException.throwMessageWithEnum(PublicErrorEnums.PUBLIC_DO_FAILED);
        }
        return new ResponseObject(true,ResponseViewEnums.SUCCESS);
    }
    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: CardBuyLogVO
     * @param   id
     * @Desc:   desc 通过id查询一个
     */
    @Override
    public CardBuyLogVO findOneCardBuyLogById(Long id) {
        Assertions.notNull(id,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        CardBuyLogVO findVO = cardBuyLogMapper.selectById(id);
        return findVO;
    }

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: java.util.List<CardBuyLogVO>
     * @param   ids
     * @Desc:   desc 通过id集合查询
     */
    @Override
    public List<CardBuyLogVO> batchFindCardBuyLogByIds(List<Long> ids){
        if (CollectionUtils.isEmpty(ids)){
            DisplayException.throwMessageWithEnum(PublicErrorEnums.PULBIC_EMPTY_PARAM);
        }
        List<CardBuyLogVO> batchFindVOSByIds = cardBuyLogMapper.batchFindByIds(ids);
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
    public ResponseObject stopOneCardBuyLogById(Long id) {
        Assertions.notNull(id,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        Integer stopNum = cardBuyLogMapper.stopOneById(id);
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
    public ResponseObject startOneCardBuyLogById(Long id) {
        Assertions.notNull(id,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        Integer stopNum = cardBuyLogMapper.startOneById(id);
        if (stopNum < NumConstants.INT_NUM_1){
            DisplayException.throwMessageWithEnum(PublicErrorEnums.PUBLIC_DO_FAILED);
        }
        return new ResponseObject(true,ResponseViewEnums.SUCCESS);
    }

    /**
     * @date:   2019/9/19 16:15
     * @author: QinDaoFang
     * @version:version
     * @return: java.lang.Long
     * @param   cardBuyLogDTO
     * @Desc:   desc
     */
    @Override
    public Integer saveOneCardBuyLogByDTOGetId(CardBuyLogDTO cardBuyLogDTO) {
        Assertions.notNull(cardBuyLogDTO);
        Assertions.notNull(cardBuyLogDTO.getSchoolId());
        Assertions.notNull(cardBuyLogDTO.getUserId());
        Assertions.notNull(cardBuyLogDTO.getCardId());
        Assertions.notNull(cardBuyLogDTO.getMoney());
        Assertions.notNull(cardBuyLogDTO.getCreateId());
        Assertions.notNull(cardBuyLogDTO.getUpdateId());
        Integer addId = cardBuyLogMapper.saveOneCardBuyLogByDTOGetId(cardBuyLogDTO);
        return addId;
    }
}
