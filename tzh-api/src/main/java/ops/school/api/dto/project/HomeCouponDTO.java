package ops.school.api.dto.project;

    import com.baomidou.mybatisplus.annotation.IdType;
    import com.baomidou.mybatisplus.annotation.TableId;
    import ops.school.api.entity.HomeCoupon;

    import javax.validation.constraints.NotNull;
    import java.util.Date;
    import java.io.Serializable;


public class HomeCouponDTO extends BaseDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    /**
     * 主键id
     */
    private Long id;

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
     * 优惠券开始使用时间
     */
    @NotNull
    private Date beginTime;

    /**
     * 优惠券发放结束时间
     */
    @NotNull
    private Date endTime;

    /**
     * 是否删除（1.已删除 0.未删除）
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

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }
}