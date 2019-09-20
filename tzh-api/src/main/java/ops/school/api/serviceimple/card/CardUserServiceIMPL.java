package ops.school.api.serviceimple.card;

import java.math.BigDecimal;
import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import ops.school.api.config.ServerConstants;
import ops.school.api.constants.NumConstants;
import ops.school.api.dao.card.CardUserMapper;
import ops.school.api.dao.card.ClubCardSendMapper;
import ops.school.api.dto.card.CardBuyLogDTO;
import ops.school.api.entity.School;
import ops.school.api.entity.WxUser;
import ops.school.api.exception.DisplayException;
import ops.school.api.service.SchoolService;
import ops.school.api.service.WxUserService;
import ops.school.api.service.card.CardBuyLogService;
import ops.school.api.service.card.CardPayLogService;
import ops.school.api.service.card.CardUserService;
import ops.school.api.util.*;
import ops.school.api.vo.card.CardBuyLogVO;
import ops.school.api.vo.card.CardPayLogVO;
import ops.school.api.vo.card.ClubCardSendVO;
import ops.school.api.wxutil.WXpayUtil;
import org.apache.commons.collections.CollectionUtils;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ops.school.api.dto.card.CardUserDTO;
import ops.school.api.vo.card.CardUserVO;
import ops.school.api.enums.ResponseViewEnums;
import ops.school.api.enums.PublicErrorEnums;
import ops.school.api.exception.Assertions;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "cardUserService")
public class CardUserServiceIMPL implements CardUserService {

    private final static String payDesc = "椰子校园-配送卡购买";

    @Autowired
    private CardUserMapper cardUserMapper;

    @Autowired
    private ClubCardSendMapper clubCardSendMapper;

    @Autowired
    private WxUserService wxUserService;

    @Autowired
    private SchoolService schoolService;

    @Autowired
    private CardPayLogService cardPayLogService;

    @Autowired
    private CardBuyLogService cardBuyLogService;


    private final Logger logger = LoggerFactory.getLogger(CardUserServiceIMPL.class);

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
        Assertions.notNull(limitDTO.getSchoolId(),ResponseViewEnums.SCHOOL_CANT_BE_NULL);
        //1-分页查询
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
        //-连表查询卡
        List<Long> cardIdList = PublicUtilS.getValueList(cardUserVOS,"cardId");
        if (cardIdList == null || cardIdList.size() < NumConstants.INT_NUM_1){
            tableData.setRecordsTotal(countNum);
            tableData.setData(cardUserVOS);
            return tableData;
        }
        Collection<ClubCardSendVO> clubCardSendVOS = clubCardSendMapper.selectBatchIds(cardIdList);
        Map<Long,ClubCardSendVO> clubCardSendVOMap = PublicUtilS.listForMapValueE(clubCardSendVOS,"id");
        //查询使用日志
        List<Long> cardUserIdList = PublicUtilS.getValueList(cardUserVOS,"id");
        List<CardPayLogVO> cardPayLogVOList = cardPayLogService.findCardPayLogByCUIdsAndTime(cardUserIdList,TimeUtilS.getDayBegin(),TimeUtilS.getDayEnd());
        Map<Long,List<CardPayLogVO>> payLogMap = PublicUtilS.listforEqualKeyListMap(cardPayLogVOList,"cardUserId");
        for (CardUserVO cardUserVO : cardUserVOS) {
            ClubCardSendVO clubCardSendVO = clubCardSendVOMap.get(cardUserVO.getCardId());
            //2-判断卡今日是否可用
            List<CardPayLogVO> payLogVOS = payLogMap.get(cardUserVO.getId());
            if (payLogVOS == null){
                payLogVOS = new ArrayList<>();
            }
            Boolean canUseTrue = this.checkUserCardTodayCanUseTrue(clubCardSendVO,cardUserVO,payLogVOS,limitDTO.getSchoolId());
            cardUserVO.setCanUseTrue(canUseTrue);
            if (clubCardSendVO != null){
                cardUserVO.setClubCardSendVO(clubCardSendVO);
            }
        }
        tableData.setRecordsTotal(countNum);
        tableData.setData(cardUserVOS);
        return tableData;
    }

    /**
     * @date:   2019/9/19 15:51
     * @author: QinDaoFang
     * @version:version
     * @return: ops.school.api.util.ResponseObject
     * @param   saveDTO
     * @Desc:   desc
     */

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseObject payOneCardUserByDTO(CardUserDTO saveDTO) {
        Assertions.notNull(saveDTO,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        Assertions.notNull(saveDTO.getCardId(),PublicErrorEnums.PULBIC_EMPTY_PARAM);
        Assertions.notNull(saveDTO.getOpenId(),PublicErrorEnums.PULBIC_EMPTY_PARAM);
        Assertions.notNull(saveDTO.getSchoolId(),PublicErrorEnums.PULBIC_EMPTY_PARAM);
        //1-先查询
        ClubCardSendVO clubCardSendVO = clubCardSendMapper.selectOneUsedCard(saveDTO.getCardId());
        Assertions.notNull(clubCardSendVO,ResponseViewEnums.CARD_CAN_NOT_USED);
        School school = schoolService.findById(saveDTO.getSchoolId().intValue());
        Assertions.notNull(school,ResponseViewEnums.SCHOOL_HAD_CHANGE);
        WxUser wxUser = wxUserService.findById(saveDTO.getOpenId());
        Assertions.notNull(wxUser,ResponseViewEnums.WX_USER_NO_EXIST2);
        //2-先下订单
        CardBuyLogDTO cardBuyLogDTO = new CardBuyLogDTO();
        cardBuyLogDTO.setSchoolId(saveDTO.getSchoolId());
        cardBuyLogDTO.setUserId(wxUser.getId());
        cardBuyLogDTO.setCardId(clubCardSendVO.getId());
        cardBuyLogDTO.setMoney(clubCardSendVO.getPriceSale());
        cardBuyLogDTO.setCreateId(wxUser.getId());
        cardBuyLogDTO.setUpdateId(wxUser.getId());
        Integer addId = cardBuyLogService.saveOneCardBuyLogByDTOGetId(cardBuyLogDTO);
        Assertions.notNull(cardBuyLogDTO.getId(),ResponseViewEnums.CARD_BUY_ERROR);
        if (addId != NumConstants.INT_NUM_1){
            DisplayException.throwMessageWithEnum(ResponseViewEnums.CARD_BUY_ERROR);
        }
        String payOrderId = Util.GenerateOrderIdByLock();
        //3-先支付
        Map<String, String> payMap = WXpayUtil.payRequest(school.getWxAppId(),
                school.getMchId(),
                school.getWxPayId(),
                payDesc,
                payOrderId,
                String.valueOf(clubCardSendVO.getPriceSale().multiply(new BigDecimal(100 )).intValue()),
                saveDTO.getOpenId(),
                ServerConstants.Local_Host_Url,
                cardBuyLogDTO.getId().toString(),
                ServerConstants.BUY_CARD_SENDER_NOTIFY_URL);
        if (!"SUCCESS".equalsIgnoreCase(payMap.get("return_code"))){
            DisplayException.throwMessageWithEnum(ResponseViewEnums.WX_PAY_ERROR);
        }
        return new ResponseObject(true,ResponseViewEnums.SUCCESS);
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
     * @date:   2019/9/19 16:57
     * @author: QinDaoFang
     * @version:version
     * @return: ops.school.api.util.ResponseObject
     * @param   cardBuyLogDTO
     * @Desc:   desc
     */
    @Override
    public ResponseObject notifyAndAddCardUserByBuyLogDTO(CardBuyLogDTO cardBuyLogDTO) {
        Assertions.notNull(cardBuyLogDTO,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        Assertions.notNull(cardBuyLogDTO.getId(),PublicErrorEnums.PULBIC_EMPTY_PARAM);
        Assertions.notNull(cardBuyLogDTO.getWxTradeNo(),PublicErrorEnums.PULBIC_EMPTY_PARAM);
        Assertions.notNull(cardBuyLogDTO.getOpenId(),ResponseViewEnums.WX_USER_NEED_USER_ID);
        //1-先查询
        CardBuyLogVO buyLogVO = cardBuyLogService.findOneCardBuyLogById(cardBuyLogDTO.getId());
        Assertions.notNull(buyLogVO,"购买配送会员卡失败-来自微信回调-查询CardBuyLogVO空"+cardBuyLogDTO.toString());
        ClubCardSendVO clubCardSendVO = clubCardSendMapper.selectOneUsedCard(buyLogVO.getCardId());
        Assertions.notNull(clubCardSendVO,ResponseViewEnums.CARD_CAN_NOT_USED);
        WxUser wxUser = wxUserService.findById(cardBuyLogDTO.getOpenId());
        Assertions.notNull(wxUser,ResponseViewEnums.WX_USER_NO_EXIST2);
        School school = schoolService.findById(buyLogVO.getSchoolId().intValue());
        Assertions.notNull(wxUser,ResponseViewEnums.SCHOOL_HAD_CHANGE);
        //3-业务处理
        CardUserVO saveVO = new CardUserVO();
        saveVO.setSchoolId(buyLogVO.getSchoolId());
        saveVO.setUserId(wxUser.getId());
        saveVO.setCardId(clubCardSendVO.getId());
        saveVO.setCardDayTime(clubCardSendVO.getDayTime());
        saveVO.setCardDayMoney(clubCardSendVO.getDayMoney());
        saveVO.setCardType(clubCardSendVO.getType());
        Date failureTime = TimeUtilS.getNextDay(new Date(),clubCardSendVO.getEffectiveDays());
        saveVO.setCardFailureTime(failureTime);
        saveVO.setIsDelete(ClubCardSendVO.IsDelete.NO_DELETED.getValue());
        saveVO.setCreateId(wxUser.getId());
        saveVO.setUpdateId(wxUser.getId());
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
     * @param   saveDTO
     * @Desc:   desc 通过DTO新增
     */
    @Override
    public ResponseObject saveOneCardUserByDTO(@Valid CardUserDTO saveDTO) {
        Assertions.notNull(saveDTO,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        Assertions.notNull(saveDTO.getCardId(),PublicErrorEnums.PULBIC_EMPTY_PARAM);
        Assertions.notNull(saveDTO.getOpenId(),PublicErrorEnums.PULBIC_EMPTY_PARAM);
        Assertions.notNull(saveDTO.getSchoolId(),PublicErrorEnums.PULBIC_EMPTY_PARAM);
        //1-先查询
        ClubCardSendVO clubCardSendVO = clubCardSendMapper.selectOneUsedCard(saveDTO.getCardId());
        Assertions.notNull(clubCardSendVO,ResponseViewEnums.CARD_CAN_NOT_USED);
        WxUser wxUser = wxUserService.findById(saveDTO.getOpenId());
        Assertions.notNull(wxUser,ResponseViewEnums.WX_USER_NO_EXIST2);
        School school = schoolService.findById(saveDTO.getSchoolId().intValue());
        Assertions.notNull(wxUser,ResponseViewEnums.SCHOOL_HAD_CHANGE);
        //3-业务处理
        CardUserVO saveVO = new CardUserVO();
        saveVO.setSchoolId(saveDTO.getSchoolId());
        saveVO.setUserId(wxUser.getId());
        saveVO.setCardId(clubCardSendVO.getId());
        saveVO.setCardDayTime(clubCardSendVO.getDayTime());
        saveVO.setCardDayMoney(clubCardSendVO.getDayMoney());
        saveVO.setCardType(clubCardSendVO.getType());
        Date failureTime = TimeUtilS.getNextDay(new Date(),clubCardSendVO.getEffectiveDays());
        saveVO.setCardFailureTime(failureTime);
        saveVO.setIsDelete(ClubCardSendVO.IsDelete.NO_DELETED.getValue());
        saveVO.setCreateId(wxUser.getId());
        saveVO.setUpdateId(wxUser.getId());
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


    /**
     * @date:   2019/9/20 10:39
     * @author: QinDaoFang
     * @version:version
     * @return: java.lang.Boolean
     * @param   clubCardSendVO
     * @param   userCardVO
     * @param   payLogVOS
     * @Desc:   desc
     */
    @Override
    public Boolean checkUserCardTodayCanUseTrue(ClubCardSendVO clubCardSendVO, CardUserVO userCardVO, List<CardPayLogVO> payLogVOS,Long schoolId) {
        Assertions.notNull(clubCardSendVO);
        Assertions.notNull(userCardVO);
        Assertions.notNull(payLogVOS);
        Assertions.notNull(schoolId);
        //同一学校范围
        if(schoolId.intValue() != userCardVO.getSchoolId().intValue()){
            return false;
        }
        //删除了
        if (clubCardSendVO.getIsDelete().intValue() == ClubCardSendVO.IsDelete.HAS_DELETED.getIntValue() || userCardVO.getIsDelete().intValue() == CardUserVO.IsDelete.HAS_DELETED.getIntValue()){
            return false;
        }
        //禁用
        if (clubCardSendVO.getStatus().intValue() == ClubCardSendVO.Status.NO.getIntValue()){
            return false;
        }
        //user
        if (userCardVO.getCardFailureTime() == null || userCardVO.getCreateTime() == null || clubCardSendVO.getEffectiveDays() == null){
            logger.error("判断用户配送卡日志-getCardFailureTime空-检查用户获取时时间插入-信息"+userCardVO.toString()+"clubCardSendVO"+clubCardSendVO.toString());
            return false;
        }
        //时间比较
        Date nowTime = new Date();
        if(!(userCardVO.getCardFailureTime().compareTo(nowTime) >= 0)){
            return false;
        }
        if (!(TimeUtilS.getNextDay(userCardVO.getCreateTime(),clubCardSendVO.getEffectiveDays()).compareTo(nowTime) >= 0)){
            return false;
        }
        //使用次数和使用金额
        Integer useTimeToday = 0;
        BigDecimal useMoneyToday = new BigDecimal(0);
        for (CardPayLogVO payLogVO : payLogVOS) {
            useTimeToday++;
            useMoneyToday = useMoneyToday.add(payLogVO.getUseMoney());
        }
        if (!(useTimeToday < clubCardSendVO.getDayTime())){
            return false;
        }
        if (!(useMoneyToday.compareTo(clubCardSendVO.getDayMoney()) < 0)){
            return false;
        }
        return true;
    }

    /**
     * @date:   2019/9/20 14:28
     * @author: QinDaoFang
     * @version:version
     * @return: java.lang.Boolean
     * @param   clubCardSendVO
     * @param   userCardVO
     * @param   times
     * @param   money
     * @param   schoolId
     * @Desc:   desc
     */
    @Override
    public Boolean checkUserCardTodayCanUseTrueByMoney(ClubCardSendVO clubCardSendVO, CardUserVO userCardVO, Integer times, BigDecimal money, Long schoolId) {
        Assertions.notNull(clubCardSendVO);
        Assertions.notNull(userCardVO);
        Assertions.notNull(times);
        Assertions.notNull(money);
        Assertions.notNull(schoolId);
        //同一学校范围
        if(schoolId.intValue() != userCardVO.getSchoolId().intValue()){
            return false;
        }
        //删除了
        if (clubCardSendVO.getIsDelete().intValue() == ClubCardSendVO.IsDelete.HAS_DELETED.getIntValue() || userCardVO.getIsDelete().intValue() == CardUserVO.IsDelete.HAS_DELETED.getIntValue()){
            return false;
        }
        //禁用
        if (clubCardSendVO.getStatus().intValue() == ClubCardSendVO.Status.NO.getIntValue()){
            return false;
        }
        //user
        if (userCardVO.getCardFailureTime() == null || userCardVO.getCreateTime() == null || clubCardSendVO.getEffectiveDays() == null){
            logger.error("判断用户配送卡日志-getCardFailureTime空-检查用户获取时时间插入-信息"+userCardVO.toString()+"clubCardSendVO"+clubCardSendVO.toString());
            return false;
        }
        //时间比较
        Date nowTime = new Date();
        if(!(userCardVO.getCardFailureTime().compareTo(nowTime) >= 0)){
            return false;
        }
        if (!(TimeUtilS.getNextDay(userCardVO.getCreateTime(),clubCardSendVO.getEffectiveDays()).compareTo(nowTime) >= 0)){
            return false;
        }
        //使用次数和使用金额
        if (!(times < clubCardSendVO.getDayTime())){
            return false;
        }
        if (!(money.compareTo(clubCardSendVO.getDayMoney()) <= 0)){
            return false;
        }
        return true;
    }

    /**
     * @date:   2019/9/20 20:23
     * @author: QinDaoFang
     * @version:version
     * @return: java.util.List<ops.school.api.vo.card.CardUserVO>
     * @param   dto
     * @Desc:   desc
     */
    @Override
    public List<CardUserVO> findCardUserList(CardUserDTO dto) {
        Assertions.notNull(dto,PublicErrorEnums.PULBIC_EMPTY_PARAM);
        Assertions.notNull(dto.getSchoolId(),ResponseViewEnums.SCHOOL_CANT_BE_NULL);
        Assertions.notNull(dto.getUserId(),ResponseViewEnums.SCHOOL_CANT_BE_NULL);
        List<CardUserVO> cardUserVOS = cardUserMapper.findCardUserList(dto);
        //-连表查询卡
        List<Long> cardIdList = PublicUtilS.getValueList(cardUserVOS,"cardId");
        if (cardIdList == null || cardIdList.size() < NumConstants.INT_NUM_1){
            for (CardUserVO cardUserVO : cardUserVOS) {
                cardUserVO.setCanUseTrue(false);
            }
            return cardUserVOS;
        }
        Collection<ClubCardSendVO> clubCardSendVOS = clubCardSendMapper.selectBatchIds(cardIdList);
        Map<Long,ClubCardSendVO> clubCardSendVOMap = PublicUtilS.listForMapValueE(clubCardSendVOS,"id");
        //查询使用日志
        List<Long> cardUserIdList = PublicUtilS.getValueList(cardUserVOS,"id");
        List<CardPayLogVO> cardPayLogVOList = cardPayLogService.findCardPayLogByCUIdsAndTime(cardUserIdList,TimeUtilS.getDayBegin(),TimeUtilS.getDayEnd());
        Map<Long,List<CardPayLogVO>> payLogMap = PublicUtilS.listforEqualKeyListMap(cardPayLogVOList,"cardUserId");
        for (CardUserVO cardUserVO : cardUserVOS) {
            ClubCardSendVO clubCardSendVO = clubCardSendVOMap.get(cardUserVO.getCardId());
            //2-判断卡今日是否可用
            List<CardPayLogVO> payLogVOS = payLogMap.get(cardUserVO.getId());
            if (payLogVOS == null){
                payLogVOS = new ArrayList<>();
            }
            Boolean canUseTrue = this.checkUserCardTodayCanUseTrue(clubCardSendVO,cardUserVO,payLogVOS,dto.getSchoolId());
            cardUserVO.setCanUseTrue(canUseTrue);
            if (clubCardSendVO != null){
                cardUserVO.setClubCardSendVO(clubCardSendVO);
            }
        }
        return cardUserVOS;
    }
}
