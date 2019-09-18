package ops.school.api.entity.card;

import ops.school.api.dto.card.CardPayLogDTO;

import java.math.BigDecimal;
import java.util.Date;
import java.io.Serializable;


public class CardPayLog implements Serializable {
    
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

    public CardPayLogDTO toDTO() {
        CardPayLogDTO dto = new CardPayLogDTO();
        dto.setId(this.id);
        dto.setSchoolId(this.schoolId);
        dto.setUserId(this.userId);
        dto.setCardId(this.cardId);
        dto.setCardType(this.cardType);
        dto.setOrderId(this.orderId);
        dto.setUseMoney(this.useMoney);
        dto.setCreateId(this.createId);
        dto.setUpdateId(this.updateId);
        dto.setCreateTime(this.createTime);
        dto.setUpdateTime(this.updateTime);
        return dto;
    }
}