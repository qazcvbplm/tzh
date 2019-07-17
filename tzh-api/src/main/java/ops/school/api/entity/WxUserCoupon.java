package ops.school.api.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * wx_user_coupon
 * @author 
 */
@Table(name="wx_user_coupon")
public class WxUserCoupon implements Serializable {
    /**
     * 主键id
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * 用户id
     */
    @NotEmpty
    private Long wxUserId;

    /**
     * 优惠券id
     */
    @NotEmpty
    private Long couponId;

    /**
     * 优惠券领取时间
     */
    @NotEmpty
    private Date getTime;

    /**
     * 优惠券使用时间
     */
    private Date useTime;

    /**
     * 失效时间
     */
    @NotEmpty
    private Date failureTime;

    /**
     * 是否失效（0.未使用 1.已使用 2.已过期）
     */
    @NotEmpty
    private Integer isInvalid;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWxUserId() {
        return wxUserId;
    }

    public void setWxUserId(Long wxUserId) {
        this.wxUserId = wxUserId;
    }

    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }

    public Date getGetTime() {
        return getTime;
    }

    public void setGetTime(Date getTime) {
        this.getTime = getTime;
    }

    public Date getUseTime() {
        return useTime;
    }

    public void setUseTime(Date useTime) {
        this.useTime = useTime;
    }

    public Date getFailureTime() {
        return failureTime;
    }

    public void setFailureTime(Date failureTime) {
        this.failureTime = failureTime;
    }

    public Integer getIsInvalid() {
        return isInvalid;
    }

    public void setIsInvalid(Integer isInvalid) {
        this.isInvalid = isInvalid;
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
        WxUserCoupon other = (WxUserCoupon) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getWxUserId() == null ? other.getWxUserId() == null : this.getWxUserId().equals(other.getWxUserId()))
            && (this.getCouponId() == null ? other.getCouponId() == null : this.getCouponId().equals(other.getCouponId()))
            && (this.getGetTime() == null ? other.getGetTime() == null : this.getGetTime().equals(other.getGetTime()))
            && (this.getUseTime() == null ? other.getUseTime() == null : this.getUseTime().equals(other.getUseTime()))
            && (this.getFailureTime() == null ? other.getFailureTime() == null : this.getFailureTime().equals(other.getFailureTime()))
            && (this.getIsInvalid() == null ? other.getIsInvalid() == null : this.getIsInvalid().equals(other.getIsInvalid()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getWxUserId() == null) ? 0 : getWxUserId().hashCode());
        result = prime * result + ((getCouponId() == null) ? 0 : getCouponId().hashCode());
        result = prime * result + ((getGetTime() == null) ? 0 : getGetTime().hashCode());
        result = prime * result + ((getUseTime() == null) ? 0 : getUseTime().hashCode());
        result = prime * result + ((getFailureTime() == null) ? 0 : getFailureTime().hashCode());
        result = prime * result + ((getIsInvalid() == null) ? 0 : getIsInvalid().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", wxUserId=").append(wxUserId);
        sb.append(", couponId=").append(couponId);
        sb.append(", getTime=").append(getTime);
        sb.append(", useTime=").append(useTime);
        sb.append(", failureTime=").append(failureTime);
        sb.append(", isInvalid=").append(isInvalid);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}