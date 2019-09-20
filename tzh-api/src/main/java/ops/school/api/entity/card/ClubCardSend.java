package ops.school.api.entity.card;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import ops.school.api.dto.card.ClubCardSendDTO;

import java.math.BigDecimal;
import java.util.Date;
import java.io.Serializable;


public class ClubCardSend implements Serializable {
    
    private static final long serialVersionUID = 1L;
    /**主键id*/
    @TableId(type = IdType.AUTO)
    private Long id;
    /**学校id*/
    private Long schoolId;
    /**卡名*/
    private String name;
    /**卡描述*/
    private String description;
    /**卡购买后每天使用次数*/
    private Integer dayTime;
    /**卡购买价格，销售价*/
    private BigDecimal priceSale;
    /**卡购买原价，展示用*/
    private BigDecimal priceOriginal;
    /**卡购买后每天最大的使用金额*/
    private BigDecimal dayMoney;
    /**优惠券发放开始时间*/
    private Date sendBeginTime;
    /**优惠券发放结束时间*/
    private Date sendEndTime;
    /**卡购买后的有效天数*/
    private Integer effectiveDays;
    /**卡类型，1-配送费卡*/
    private Byte type;
    /**卡的状态，0-冻结不可用，1-可用*/
    private Byte status;
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
    
    public String getName() {
    return this.name;
    }
    
    public void setName(String name) {
    this.name = name;
    }
    
    public String getDescription() {
    return this.description;
    }
    
    public void setDescription(String description) {
    this.description = description;
    }
    
    public Integer getDayTime() {
    return this.dayTime;
    }
    
    public void setDayTime(Integer dayTime) {
    this.dayTime = dayTime;
    }
    
    public BigDecimal getPriceSale() {
    return this.priceSale;
    }
    
    public void setPriceSale(BigDecimal priceSale) {
    this.priceSale = priceSale;
    }
    
    public BigDecimal getPriceOriginal() {
    return this.priceOriginal;
    }
    
    public void setPriceOriginal(BigDecimal priceOriginal) {
    this.priceOriginal = priceOriginal;
    }
    
    public BigDecimal getDayMoney() {
    return this.dayMoney;
    }
    
    public void setDayMoney(BigDecimal dayMoney) {
    this.dayMoney = dayMoney;
    }
    
    public Date getSendBeginTime() {
    return this.sendBeginTime;
    }
    
    public void setSendBeginTime(Date sendBeginTime) {
    this.sendBeginTime = sendBeginTime;
    }
    
    public Date getSendEndTime() {
    return this.sendEndTime;
    }
    
    public void setSendEndTime(Date sendEndTime) {
    this.sendEndTime = sendEndTime;
    }
    
    public Integer getEffectiveDays() {
    return this.effectiveDays;
    }
    
    public void setEffectiveDays(Integer effectiveDays) {
    this.effectiveDays = effectiveDays;
    }
    
    public Byte getType() {
    return this.type;
    }
    
    public void setType(Byte type) {
    this.type = type;
    }
    
    public Byte getStatus() {
    return this.status;
    }
    
    public void setStatus(Byte status) {
    this.status = status;
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

    public ClubCardSendDTO toDTO() {
        ClubCardSendDTO dto = new ClubCardSendDTO();
        dto.setId(this.id);
        dto.setSchoolId(this.schoolId);
        dto.setName(this.name);
        dto.setDescription(this.description);
        dto.setDayTime(this.dayTime);
        dto.setPriceSale(this.priceSale);
        dto.setPriceOriginal(this.priceOriginal);
        dto.setDayMoney(this.dayMoney);
        dto.setSendBeginTime(this.sendBeginTime);
        dto.setSendEndTime(this.sendEndTime);
        dto.setEffectiveDays(this.effectiveDays);
        dto.setType(this.type);
        dto.setStatus(this.status);
        dto.setIsDelete(this.isDelete);
        dto.setCreateId(this.createId);
        dto.setUpdateId(this.updateId);
        dto.setCreateTime(this.createTime);
        dto.setUpdateTime(this.updateTime);
        return dto;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ClubCardSend{");
        sb.append("id=").append(id);
        sb.append(", schoolId=").append(schoolId);
        sb.append(", name='").append(name).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", dayTime=").append(dayTime);
        sb.append(", priceSale=").append(priceSale);
        sb.append(", priceOriginal=").append(priceOriginal);
        sb.append(", dayMoney=").append(dayMoney);
        sb.append(", sendBeginTime=").append(sendBeginTime);
        sb.append(", sendEndTime=").append(sendEndTime);
        sb.append(", effectiveDays=").append(effectiveDays);
        sb.append(", type=").append(type);
        sb.append(", status=").append(status);
        sb.append(", isDelete=").append(isDelete);
        sb.append(", createId=").append(createId);
        sb.append(", updateId=").append(updateId);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append('}');
        return sb.toString();
    }
}