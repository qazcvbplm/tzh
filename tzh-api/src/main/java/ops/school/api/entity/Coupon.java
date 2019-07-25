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
public class Coupon extends Base implements Serializable {
    private static final long serialVersionUID = 1L;
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
     * 优惠券是否在首页展示（0不展示，1展示）
     */
    private Integer yesShowIndex;
    /**
     * 优惠券发放开始时间
     */
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date sendBeginTime;
    /**
     * 优惠券发放结束时间
     */
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date sendEndTime;
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

    public Integer getYesShowIndex() {
        return yesShowIndex;
    }

    public void setYesShowIndex(Integer yesShowIndex) {
        this.yesShowIndex = yesShowIndex;
    }

    public Date getSendBeginTime() {
        return sendBeginTime;
    }

    public void setSendBeginTime(Date sendBeginTime) {
        this.sendBeginTime = sendBeginTime;
    }

    public Date getSendEndTime() {
        return sendEndTime;
    }

    public void setSendEndTime(Date sendEndTime) {
        this.sendEndTime = sendEndTime;
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
        result = prime * result + ((getYesShowIndex() == null) ? 0 : getYesShowIndex().hashCode());
        result = prime * result + ((getSendBeginTime() == null) ? 0 : getSendBeginTime().hashCode());
        result = prime * result + ((getSendEndTime() == null) ? 0 : getSendEndTime().hashCode());
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
        sb.append(", yesShowIndex=").append(yesShowIndex);
        sb.append(", beginTime=").append(sendBeginTime);
        sb.append(", sendEndTime=").append(sendEndTime);
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