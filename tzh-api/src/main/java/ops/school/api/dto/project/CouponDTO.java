package ops.school.api.dto.project;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import ops.school.api.entity.Coupon;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.io.Serializable;
import java.util.List;


public class CouponDTO extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 主键id
     */
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
    @NotNull
    private Date createTime;

    /**
     * 创建人id
     */
    @NotNull
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
    @NotNull
    private Integer isInvalid;

    /**
     * 是否删除（1.删除 0.未删除）
     */
    @NotNull
    private Integer isDelete;

    private List<Coupon> couponList;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSchoolId(Long schoolId) {
        this.schoolId = schoolId;
    }

    public Long getSchoolId() {
        return schoolId;
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

    public List<Coupon> getCouponList() {
        return couponList;
    }

    public void setCouponList(List<Coupon> couponList) {
        this.couponList = couponList;
    }
}