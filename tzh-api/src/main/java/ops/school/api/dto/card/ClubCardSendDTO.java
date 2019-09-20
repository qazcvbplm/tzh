package ops.school.api.dto.card;

import java.math.BigDecimal;
import java.util.Date;

import ops.school.api.dto.BaseDTOCompute;
import ops.school.api.entity.BaseDTOMP;
import ops.school.api.entity.card.ClubCardSend;
import ops.school.api.vo.card.ClubCardSendVO;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;


public class ClubCardSendDTO extends BaseDTOCompute implements Serializable {
    
    private static final long serialVersionUID = 1L;
    /**主键id*/
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
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date sendBeginTime;
    /**优惠券发放结束时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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

    public ClubCardSendVO toVO() {
        ClubCardSendVO vo = new ClubCardSendVO();
        vo.setId(this.id);
        vo.setSchoolId(this.schoolId);
        vo.setName(this.name);
        vo.setDescription(this.description);
        vo.setDayTime(this.dayTime);
        vo.setPriceSale(this.priceSale);
        vo.setPriceOriginal(this.priceOriginal);
        vo.setDayMoney(this.dayMoney);
        vo.setSendBeginTime(this.sendBeginTime);
        vo.setSendEndTime(this.sendEndTime);
        vo.setEffectiveDays(this.effectiveDays);
        vo.setType(this.type);
        vo.setStatus(this.status);
        vo.setIsDelete(this.isDelete);
        vo.setCreateId(this.createId);
        vo.setUpdateId(this.updateId);
        vo.setCreateTime(this.createTime);
        vo.setUpdateTime(this.updateTime);
        return vo;
    }
    public ClubCardSend toModel() {
        ClubCardSend model = new ClubCardSend();
        model.setId(this.id);
        model.setSchoolId(this.schoolId);
        model.setName(this.name);
        model.setDescription(this.description);
        model.setDayTime(this.dayTime);
        model.setPriceSale(this.priceSale);
        model.setPriceOriginal(this.priceOriginal);
        model.setDayMoney(this.dayMoney);
        model.setSendBeginTime(this.sendBeginTime);
        model.setSendEndTime(this.sendEndTime);
        model.setEffectiveDays(this.effectiveDays);
        model.setType(this.type);
        model.setStatus(this.status);
        model.setIsDelete(this.isDelete);
        model.setCreateId(this.createId);
        model.setUpdateId(this.updateId);
        model.setCreateTime(this.createTime);
        model.setUpdateTime(this.updateTime);
        return model;
    }
}