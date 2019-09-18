package ops.school.api.dto.card;

import java.math.BigDecimal;
import java.util.Date;

import ops.school.api.entity.BaseDTOMP;
import ops.school.api.entity.card.CardPayLog;
import ops.school.api.vo.card.CardPayLogVO;

import java.io.Serializable;


public class CardPayLogDTO  extends BaseDTOMP implements Serializable {
    
    private static final long serialVersionUID = 1L;
    /**主键id*/
    private Long id;
    /**学校id*/
    private Long schoolId;
    /**用户id*/
    private Long userId;
    /**卡id*/
    private Long cardId;
    /**卡类型，1-配送费卡*/
    private Byte cardType;
    /**使用的外卖订单id*/
    private String orderId;
    /**订单中使用的金额*/
    private BigDecimal useMoney;
    /**创建人id*/
    private Long createId;
    /**修改人id*/
    private Long updateId;
    /**创建时间*/
    private Date createTime;
    /**修改时间*/
    private Date updateTime;
    
    public Long getId() {
    return this.id;
    }
    
    public void setId(Long id) {
    this.id = id;
    }
    
    public Long getSchoolId() {
    return this.schoolId;
    }
    
    public void setSchoolId(Long schoolId) {
    this.schoolId = schoolId;
    }
    
    public Long getUserId() {
    return this.userId;
    }
    
    public void setUserId(Long userId) {
    this.userId = userId;
    }
    
    public Long getCardId() {
    return this.cardId;
    }
    
    public void setCardId(Long cardId) {
    this.cardId = cardId;
    }
    
    public Byte getCardType() {
    return this.cardType;
    }
    
    public void setCardType(Byte cardType) {
    this.cardType = cardType;
    }
    
    public String getOrderId() {
    return this.orderId;
    }
    
    public void setOrderId(String orderId) {
    this.orderId = orderId;
    }
    
    public BigDecimal getUseMoney() {
    return this.useMoney;
    }
    
    public void setUseMoney(BigDecimal useMoney) {
    this.useMoney = useMoney;
    }
    
    public Long getCreateId() {
    return this.createId;
    }
    
    public void setCreateId(Long createId) {
    this.createId = createId;
    }
    
    public Long getUpdateId() {
    return this.updateId;
    }
    
    public void setUpdateId(Long updateId) {
    this.updateId = updateId;
    }
    
    public Date getCreateTime() {
    return this.createTime;
    }
    
    public void setCreateTime(Date createTime) {
    this.createTime = createTime;
    }
    
    public Date getUpdateTime() {
    return this.updateTime;
    }
    
    public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
    }

    public CardPayLogVO toVO() {
        CardPayLogVO vo = new CardPayLogVO();
        vo.setId(this.id);
        vo.setSchoolId(this.schoolId);
        vo.setUserId(this.userId);
        vo.setCardId(this.cardId);
        vo.setCardType(this.cardType);
        vo.setOrderId(this.orderId);
        vo.setUseMoney(this.useMoney);
        vo.setCreateId(this.createId);
        vo.setUpdateId(this.updateId);
        vo.setCreateTime(this.createTime);
        vo.setUpdateTime(this.updateTime);
        return vo;
    }
    public CardPayLog toModel() {
        CardPayLog model = new CardPayLog();
        model.setId(this.id);
        model.setSchoolId(this.schoolId);
        model.setUserId(this.userId);
        model.setCardId(this.cardId);
        model.setCardType(this.cardType);
        model.setOrderId(this.orderId);
        model.setUseMoney(this.useMoney);
        model.setCreateId(this.createId);
        model.setUpdateId(this.updateId);
        model.setCreateTime(this.createTime);
        model.setUpdateTime(this.updateTime);
        return model;
    }
}