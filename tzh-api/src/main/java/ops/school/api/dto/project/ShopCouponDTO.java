package ops.school.api.dto.project;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import ops.school.api.entity.Coupon;
import ops.school.api.entity.ShopCoupon;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.io.Serializable;


public class ShopCouponDTO extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
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



    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

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


}