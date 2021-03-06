package ops.school.api.entity.card;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import ops.school.api.dto.card.CardUserDTO;

import java.math.BigDecimal;
import java.util.Date;
import java.io.Serializable;


public class CardUser implements Serializable {
    
    private static final long serialVersionUID = 1L;
    /**主键id*/
    @TableId(type = IdType.AUTO)
    private Long id;
    /**学校id*/
    private Long schoolId;
    /**用户id*/
    private Long userId;

    private Long cardId;
    /**卡购买后每天使用次数*/
    private Integer cardDayTime;
    /**卡购买后每天最大的使用金额*/
    private BigDecimal cardDayMoney;
    /**卡类型，1-配送费卡*/
    private Byte cardType;
    /**失效时间*/
    private Date cardFailureTime;
    /**是否删除 0：未删除 1：已删除*/
    private Byte isDelete;
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
    
    public Integer getCardDayTime() {
    return this.cardDayTime;
    }
    
    public void setCardDayTime(Integer cardDayTime) {
    this.cardDayTime = cardDayTime;
    }
    
    public BigDecimal getCardDayMoney() {
    return this.cardDayMoney;
    }
    
    public void setCardDayMoney(BigDecimal cardDayMoney) {
    this.cardDayMoney = cardDayMoney;
    }
    
    public Byte getCardType() {
    return this.cardType;
    }
    
    public void setCardType(Byte cardType) {
    this.cardType = cardType;
    }
    
    public Date getCardFailureTime() {
    return this.cardFailureTime;
    }
    
    public void setCardFailureTime(Date cardFailureTime) {
    this.cardFailureTime = cardFailureTime;
    }
    
    public Byte getIsDelete() {
    return this.isDelete;
    }
    
    public void setIsDelete(Byte isDelete) {
    this.isDelete = isDelete;
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

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    public CardUserDTO toDTO() {
        CardUserDTO dto = new CardUserDTO();
        dto.setId(this.id);
        dto.setSchoolId(this.schoolId);
        dto.setUserId(this.userId);
        dto.setCardDayTime(this.cardDayTime);
        dto.setCardDayMoney(this.cardDayMoney);
        dto.setCardType(this.cardType);
        dto.setCardFailureTime(this.cardFailureTime);
        dto.setIsDelete(this.isDelete);
        dto.setCreateId(this.createId);
        dto.setUpdateId(this.updateId);
        dto.setCreateTime(this.createTime);
        dto.setUpdateTime(this.updateTime);
        dto.setCardId(this.cardId);
        return dto;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("CardUser{");
        sb.append("id=").append(id);
        sb.append(", schoolId=").append(schoolId);
        sb.append(", userId=").append(userId);
        sb.append(", cardId=").append(cardId);
        sb.append(", cardDayTime=").append(cardDayTime);
        sb.append(", cardDayMoney=").append(cardDayMoney);
        sb.append(", cardType=").append(cardType);
        sb.append(", cardFailureTime=").append(cardFailureTime);
        sb.append(", isDelete=").append(isDelete);
        sb.append(", createId=").append(createId);
        sb.append(", updateId=").append(updateId);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append('}');
        return sb.toString();
    }
}