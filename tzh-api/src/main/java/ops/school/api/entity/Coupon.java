package ops.school.api.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * coupon
 * @author 
 */
@Table(name="coupon")
public class Coupon implements Serializable {
    /**
     * 主键id
     */
    @TableId(type = IdType.INPUT)
    private Long id;

    /**
     * 学校id
     */
    @NotNull
    private Long schoolId;

    /**
     * 优惠券名称
     */
    @NotNull
    private String couponName;

    /**
     * 优惠券描述
     */
    @NotNull
    private String couponDesc;

    /**
     * 优惠券面额（满额使用）
     */
    @NotNull
    private Integer fullAmount;

    /**
     * 优惠金额（减额）
     */
    @NotNull
    private Integer cutAmount;

    /**
     * 优惠券类型（0.店铺优惠券  1.首页优惠券）
     */
    @NotNull
    private Integer couponType;

    /**
     * 优惠券开始使用时间
     */
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;

    /**
     * 优惠券有效天数
     */
    @NotNull
    private Integer effectiveDays;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人id
     */
    private Long createId;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 修改人id
     */
    private Long updateId;

    /**
     * 是否失效（1.失效 0.有效）
     */
    private Integer isInvalid;

    /**
     * 是否删除（1.删除 0.未删除）
     */
    private Integer isDelete;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Long schoolId) {
        this.schoolId = schoolId;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public String getCouponDesc() {
        return couponDesc;
    }

    public void setCouponDesc(String couponDesc) {
        this.couponDesc = couponDesc;
    }

    public Integer getFullAmount() {
        return fullAmount;
    }

    public void setFullAmount(Integer fullAmount) {
        this.fullAmount = fullAmount;
    }

    public Integer getCutAmount() {
        return cutAmount;
    }

    public void setCutAmount(Integer cutAmount) {
        this.cutAmount = cutAmount;
    }

    public Integer getCouponType() {
        return couponType;
    }

    public void setCouponType(Integer couponType) {
        this.couponType = couponType;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Integer getEffectiveDays() {
        return effectiveDays;
    }

    public void setEffectiveDays(Integer effectiveDays) {
        this.effectiveDays = effectiveDays;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getCreateId() {
        return createId;
    }

    public void setCreateId(Long createId) {
        this.createId = createId;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getUpdateId() {
        return updateId;
    }

    public void setUpdateId(Long updateId) {
        this.updateId = updateId;
    }

    public Integer getIsInvalid() {
        return isInvalid;
    }

    public void setIsInvalid(Integer isInvalid) {
        this.isInvalid = isInvalid;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Coupon other = (Coupon) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getCouponName() == null ? other.getCouponName() == null : this.getCouponName().equals(other.getCouponName()))
            && (this.getCouponDesc() == null ? other.getCouponDesc() == null : this.getCouponDesc().equals(other.getCouponDesc()))
            && (this.getFullAmount() == null ? other.getFullAmount() == null : this.getFullAmount().equals(other.getFullAmount()))
            && (this.getCutAmount() == null ? other.getCutAmount() == null : this.getCutAmount().equals(other.getCutAmount()))
            && (this.getCouponType() == null ? other.getCouponType() == null : this.getCouponType().equals(other.getCouponType()))
            && (this.getBeginTime() == null ? other.getBeginTime() == null : this.getBeginTime().equals(other.getBeginTime()))
            && (this.getEffectiveDays() == null ? other.getEffectiveDays() == null : this.getEffectiveDays().equals(other.getEffectiveDays()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getCreateId() == null ? other.getCreateId() == null : this.getCreateId().equals(other.getCreateId()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getUpdateId() == null ? other.getUpdateId() == null : this.getUpdateId().equals(other.getUpdateId()))
            && (this.getIsInvalid() == null ? other.getIsInvalid() == null : this.getIsInvalid().equals(other.getIsInvalid()))
            && (this.getIsDelete() == null ? other.getIsDelete() == null : this.getIsDelete().equals(other.getIsDelete()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getCouponName() == null) ? 0 : getCouponName().hashCode());
        result = prime * result + ((getCouponDesc() == null) ? 0 : getCouponDesc().hashCode());
        result = prime * result + ((getFullAmount() == null) ? 0 : getFullAmount().hashCode());
        result = prime * result + ((getCutAmount() == null) ? 0 : getCutAmount().hashCode());
        result = prime * result + ((getCouponType() == null) ? 0 : getCouponType().hashCode());
        result = prime * result + ((getBeginTime() == null) ? 0 : getBeginTime().hashCode());
        result = prime * result + ((getEffectiveDays() == null) ? 0 : getEffectiveDays().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getCreateId() == null) ? 0 : getCreateId().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getUpdateId() == null) ? 0 : getUpdateId().hashCode());
        result = prime * result + ((getIsInvalid() == null) ? 0 : getIsInvalid().hashCode());
        result = prime * result + ((getIsDelete() == null) ? 0 : getIsDelete().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", couponName=").append(couponName);
        sb.append(", couponDesc=").append(couponDesc);
        sb.append(", fullAmount=").append(fullAmount);
        sb.append(", cutAmount=").append(cutAmount);
        sb.append(", couponType=").append(couponType);
        sb.append(", beginTime=").append(beginTime);
        sb.append(", effectiveDays=").append(effectiveDays);
        sb.append(", createTime=").append(createTime);
        sb.append(", createId=").append(createId);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", updateId=").append(updateId);
        sb.append(", isInvalid=").append(isInvalid);
        sb.append(", isDelete=").append(isDelete);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}