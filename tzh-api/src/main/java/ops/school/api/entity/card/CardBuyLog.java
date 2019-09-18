package ops.school.api.entity.card;

import ops.school.api.dto.card.CardBuyLogDTO;

import java.math.BigDecimal;
import java.util.Date;
import java.io.Serializable;


public class CardBuyLog implements Serializable {
    
    private static final long serialVersionUID = 1L;
    /**主键id*/
    private Long id;
    /**学校id*/
    private Long schoolId;
    /**用户id*/
    private Long userId;
    /**卡id*/
    private Long cardId;
    /**购买金额*/
    private BigDecimal money;
    /**微信充值流水号*/
    private String wxTradeNo;
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
    
    public BigDecimal getMoney() {
    return this.money;
    }
    
    public void setMoney(BigDecimal money) {
    this.money = money;
    }
    
    public String getWxTradeNo() {
    return this.wxTradeNo;
    }
    
    public void setWxTradeNo(String wxTradeNo) {
    this.wxTradeNo = wxTradeNo;
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

    public CardBuyLogDTO toDTO() {
        CardBuyLogDTO dto = new CardBuyLogDTO();
        dto.setId(this.id);
        dto.setSchoolId(this.schoolId);
        dto.setUserId(this.userId);
        dto.setCardId(this.cardId);
        dto.setMoney(this.money);
        dto.setWxTradeNo(this.wxTradeNo);
        dto.setCreateId(this.createId);
        dto.setUpdateId(this.updateId);
        dto.setCreateTime(this.createTime);
        dto.setUpdateTime(this.updateTime);
        return dto;
    }
}