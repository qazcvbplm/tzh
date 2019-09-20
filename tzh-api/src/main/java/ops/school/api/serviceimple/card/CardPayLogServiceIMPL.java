package ops.school.api.serviceimple.card;

import java.util.List;
import java.util.Map;
import java.util.Date;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import ops.school.api.constants.NumConstants;
import ops.school.api.dao.card.CardPayLogMapper;
import ops.school.api.exception.DisplayException;
import ops.school.api.service.card.CardPayLogService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ops.school.api.entity.card.CardPayLog;
import ops.school.api.dto.card.CardPayLogDTO;
import ops.school.api.enums.ResponseViewEnums;
import ops.school.api.util.LimitTableData;
import ops.school.api.enums.PublicErrorEnums;
import ops.school.api.util.ResponseObject;
import ops.school.api.vo.card.CardPayLogVO;
import ops.school.api.exception.Assertions;
import org.springframework.beans.BeanUtils;

@Service(value = "cardPayLogService")
public class CardPayLogServiceIMPL implements CardPayLogService {

    @Autowired
    private CardPayLogMapper cardPayLogMapper;

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.LimitTableData<CardPayLogVO>
     * @param   limitDTO
     * @Desc:   desc 分页查询
     */
    @Override
    public LimitTableData<CardPayLogVO> limitTableDataByDTO(CardPayLogDTO limitDTO) {
        LimitTableData<CardPayLogVO> tableData = new LimitTableData();
        Integer countNum = cardPayLogMapper.countLimitByDTO(limitDTO);
        if (countNum < NumConstants.INTEGER_NUM_1){
            return tableData;
        }
        List<CardPayLogVO> cardPayLogVOS = cardPayLogMapper.selectLimitByDTO(limitDTO);
        if (CollectionUtils.isEmpty(cardPayLogVOS)){
            tableData.setRecordsTotal(countNum);
            return tableData;
        }
        tableData.setRecordsTotal(countNum);
        tableData.setData(cardPayLogVOS);
        return tableData;
    }

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: java.util.List<CardPayLogVO>
     * @param
     * @Desc:   desc 查询所有数据
     */
     @Override
    public List<CardPayLogVO> findAllCardPayLogVOs() {
        List<CardPayLogVO> CardPayLogVOS = cardPayLogMapper.selectList(new QueryWrapper<CardPayLogVO>());
        return CardPayLogVOS;
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
    public ResponseObject saveOneCardPayLogByDTO(@Valid CardPayLogDTO saveDTO) {
        Assertions.notNull(saveDTO,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        //设置日期格式
        saveDTO.setCreateTime(new Date());
        saveDTO.setUpdateTime(new Date());
        CardPayLogVO saveVO = new CardPayLogVO();
        BeanUtils.copyProperties(saveDTO,saveVO);
        Integer saveNum = cardPayLogMapper.insert(saveVO);
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
    public ResponseObject updateOneCardPayLogByDTO(CardPayLogDTO updateDTO) {
        Assertions.notNull(updateDTO,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        Assertions.notNull(updateDTO.getId(),PublicErrorEnums.PULBIC_EMPTY_PARAM);
        int updateNum = cardPayLogMapper.updateById(updateDTO.toVO());
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
    public ResponseObject deleteOneCardPayLogById(Long id){
        Assertions.notNull(id,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        int deleteNum = cardPayLogMapper.deleteById(id);
        if (deleteNum < NumConstants.INT_NUM_1){
            DisplayException.throwMessageWithEnum(PublicErrorEnums.PUBLIC_DO_FAILED);
        }
        return new ResponseObject(true,ResponseViewEnums.SUCCESS);
    }
    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: CardPayLogVO
     * @param   id
     * @Desc:   desc 通过id查询一个
     */
    @Override
    public CardPayLogVO findOneCardPayLogById(Long id) {
        Assertions.notNull(id,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        CardPayLogVO findVO = cardPayLogMapper.selectById(id);
        return findVO;
    }

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: java.util.List<CardPayLogVO>
     * @param   ids
     * @Desc:   desc 通过id集合查询
     */
    @Override
    public List<CardPayLogVO> batchFindCardPayLogByIds(List<Long> ids){
        if (CollectionUtils.isEmpty(ids)){
            DisplayException.throwMessageWithEnum(PublicErrorEnums.PULBIC_EMPTY_PARAM);
        }
        List<CardPayLogVO> batchFindVOSByIds = cardPayLogMapper.batchFindByIds(ids);
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
    public ResponseObject stopOneCardPayLogById(Long id) {
        Assertions.notNull(id,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        Integer stopNum = cardPayLogMapper.stopOneById(id);
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
    public ResponseObject startOneCardPayLogById(Long id) {
        Assertions.notNull(id,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        Integer stopNum = cardPayLogMapper.startOneById(id);
        if (stopNum < NumConstants.INT_NUM_1){
            DisplayException.throwMessageWithEnum(PublicErrorEnums.PUBLIC_DO_FAILED);
        }
        return new ResponseObject(true,ResponseViewEnums.SUCCESS);
    }

    /**
     * @date:   2019/9/20 12:16
     * @author: QinDaoFang
     * @version:version
     * @return: java.util.List<ops.school.api.vo.card.CardPayLogVO>
     * @param   cardUserIdList
     * @Desc:   desc
     */
    @Override
    public List<CardPayLogVO> batchFindCardPayLogByCUIds(List<Long> cardUserIdList) {
        Assertions.notEmpty(cardUserIdList);
        return cardPayLogMapper.batchFindCardPayLogByCUIds(cardUserIdList);
    }
}
