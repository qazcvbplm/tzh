package ops.school.api.dto.project;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import ops.school.api.entity.WxUserCoupon;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.io.Serializable;


public class WxUserCouponDTO extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 用户id
     */
    @NotNull
    private Long wxUserId;

    /**
     * 优惠券id
     */
    @NotNull
    private Long couponId;

    /**
     * 优惠券领取时间
     */
    @NotNull
    private Date getTime;

    /**
     * 优惠券使用时间
     */
    private Date useTime;

    /**
     * 失效时间
     */
    @NotNull
    private Date failureTime;

    /**
     * 是否失效（0.未使用 1.已使用 2.已过期）
     */
    @NotNull
    private Integer isInvalid;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

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
}