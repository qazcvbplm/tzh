package ops.school.api.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

/**
 * shop_coupon
 * @author 
 */
@Table(name="shop_coupon")
public class ShopCoupon implements Serializable {
    /**
     * 主键id
     */
    @TableId(type = IdType.INPUT)
    private Long id;

    /**
     * 店铺id
     */
    @NotNull
    private Long shopId;

    /**
     * 优惠券id
     */
    @NotNull
    private Long couponId;

    /**
     * 创建时间
     */
    @NotNull
    private Date createTime;

    /**
     * 创建人id
     */
    @NotNull
    private Long createId;

    /**
     * 是否删除（1.删除 0.未删除）
     */
    @NotNull
    private Integer isDelete;

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

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
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

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
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
        ShopCoupon that = (ShopCoupon) o;
        return id.equals(that.id) &&
                shopId.equals(that.shopId) &&
                couponId.equals(that.couponId) &&
                createTime.equals(that.createTime) &&
                createId.equals(that.createId) &&
                isDelete.equals(that.isDelete) &&
                Objects.equals(coupon, that.coupon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, shopId, couponId, createTime, createId, isDelete, coupon);
    }

    @Override
    public String toString() {
        return "ShopCoupon{" +
                "id=" + id +
                ", shopId=" + shopId +
                ", couponId=" + couponId +
                ", createTime=" + createTime +
                ", createId=" + createId +
                ", isDelete=" + isDelete +
                ", coupon=" + coupon +
                '}';
    }
}