package ops.school.api.dto.project;

import ops.school.api.entity.WxUserBell;

import java.math.BigDecimal;
import java.io.Serializable;


public class WxUserBellDTO extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 主键id
     */
    private Long id;
    /**
     * wxuser表主键id
     */
    private Long wxUserId;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 积分
     */
    private Integer source;
    /**
     * 余额
     */
    private BigDecimal money;
    /**
     * 会员标志
     */
    private Integer isVip;
    /**
     * 会员过期时间
     */
    private Long vipOutTime;
    /**
     * 粮票余额
     */
    private BigDecimal foodCoupon;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public Integer getIsVip() {
        return isVip;
    }

    public void setIsVip(Integer isVip) {
        this.isVip = isVip;
    }

    public Long getVipOutTime() {
        return vipOutTime;
    }

    public void setVipOutTime(Long vipOutTime) {
        this.vipOutTime = vipOutTime;
    }

    public BigDecimal getFoodCoupon() {
        return foodCoupon;
    }

    public void setFoodCoupon(BigDecimal foodCoupon) {
        this.foodCoupon = foodCoupon;
    }
}