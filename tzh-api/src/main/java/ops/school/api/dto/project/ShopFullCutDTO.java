package ops.school.api.dto.project;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import ops.school.api.entity.ShopFullCut;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.io.Serializable;


public class ShopFullCutDTO extends BaseDTO implements Serializable {

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
     * 满
     */
    private Integer fullAmount;

    /**
     * 减
     */
    private Integer cutAmount;

    /**
     * 是否删除 0：否 1：是
     */
    private Byte isDelete;

    /**
     * 创建人ID
     */
    @NotNull
    private Long createId;

    /**
     * 更新人ID
     */
    @NotNull
    private Long updateId;

    /**
     * 创建时间
     */
    @NotNull
    private Date createTime;

    /**
     * 更新时间
     */
    @NotNull
    private Date updateTime;

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

    public Byte getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Byte isDelete) {
        this.isDelete = isDelete;
    }

    public Long getCreateId() {
        return createId;
    }

    public void setCreateId(Long createId) {
        this.createId = createId;
    }

    public Long getUpdateId() {
        return updateId;
    }

    public void setUpdateId(Long updateId) {
        this.updateId = updateId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}