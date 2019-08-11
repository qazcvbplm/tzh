package ops.school.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.util.Date;
import java.io.Serializable;


/**
 * @author: QinDaoFang
 * @date:   2019/8/11 18:13
 * @desc:   
 */
public class ShopPrint implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**店铺id*/
    private Long shopId;
    /**店铺是否开启飞鹅GPRS打印 0-不开启，1-开启*/
    private Integer yesPrintGpr;
    /**打印机品牌 1-飞鹅，2-其他*/
    private Integer printBrand;
    /**店铺打印机的sn码*/
    private String feiESn;
    /**店铺打印机的key码*/
    private String feiEKey;
    /**创建人id*/
    private Long createId;
    /**修改人id*/
    private Long updateId;
    /**创建时间*/
    private Date createTime;
    /**修改时间*/
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

    public Integer getYesPrintGpr() {
        return yesPrintGpr;
    }

    public void setYesPrintGpr(Integer yesPrintGpr) {
        this.yesPrintGpr = yesPrintGpr;
    }

    public Integer getPrintBrand() {
        return printBrand;
    }

    public void setPrintBrand(Integer printBrand) {
        this.printBrand = printBrand;
    }

    public String getFeiESn() {
        return feiESn;
    }

    public void setFeiESn(String feiESn) {
        this.feiESn = feiESn;
    }

    public String getFeiEKey() {
        return feiEKey;
    }

    public void setFeiEKey(String feiEKey) {
        this.feiEKey = feiEKey;
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