package ops.school.api.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

/**
 * wx_user_coupon
 * @author 
 */
@Table(name="wx_user_coupon")
public class WxUserCoupon extends PageQueryDTO implements Serializable {
    /**
     * 主键id
     */
    @TableId(type = IdType.INPUT)
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
     * 店铺id
     */
    private Long shopId;

    /**
     * 优惠券类型
     */
    private Integer couponType;

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

    @TableField(exist = false)
    private Coupon coupon;

    @TableField(exist = false)
    private Shop shop;

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


    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Integer getCouponType() {
        return couponType;
    }

    public void setCouponType(Integer couponType) {
        this.couponType = couponType;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }


    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WxUserCoupon that = (WxUserCoupon) o;
        return id.equals(that.id) &&
                wxUserId.equals(that.wxUserId) &&
                couponId.equals(that.couponId) &&
                Objects.equals(shopId, that.shopId) &&
                couponType.equals(that.couponType) &&
                getTime.equals(that.getTime) &&
                Objects.equals(useTime, that.useTime) &&
                failureTime.equals(that.failureTime) &&
                isInvalid.equals(that.isInvalid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, wxUserId, couponId, shopId, couponType, getTime, useTime, failureTime, isInvalid);
    }

    @Override
    public String toString() {
        return "WxUserCoupon{" +
                "id=" + id +
                ", wxUserId=" + wxUserId +
                ", couponId=" + couponId +
                ", shopId=" + shopId +
                ", couponType=" + couponType +
                ", getTime=" + getTime +
                ", useTime=" + useTime +
                ", failureTime=" + failureTime +
                ", isInvalid=" + isInvalid +
                '}';
    }
}