package ops.school.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * wx_user_bell
 * @author
 */
@Table(name="wx_user_bell")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WxUserBell extends Base implements Serializable {
    /**
     * 手机号
     */
    @TableId(type = IdType.INPUT)
    private String phone;

    /**
     * 主键id
     */
    @NotNull
    private Long id;

    /**
     * wxuser表主键id
     */
    @NotNull
    private Long wxUserId;

    /**
     * 积分
     */
    @NotNull
    private Integer source;

    /**
     * 余额
     */
    @NotNull
    private BigDecimal money;

    /**
     * 会员标志
     */
    @NotNull
    private Integer isVip;

    /**
     * 会员过期时间
     */
    @NotNull
    private Long vipOutTime;

    /**
     * 粮票余额
     */
    @NotNull
    private BigDecimal foodCoupon;

    public WxUserBell() {
        super();
    }

    public WxUserBell(String phone2) {
        this.phone = phone2;
    }

    private static final long serialVersionUID = 1L;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
        WxUserBell other = (WxUserBell) that;
        return (this.getPhone() == null ? other.getPhone() == null : this.getPhone().equals(other.getPhone()))
                && (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getWxUserId() == null ? other.getWxUserId() == null : this.getWxUserId().equals(other.getWxUserId()))
                && (this.getSource() == null ? other.getSource() == null : this.getSource().equals(other.getSource()))
                && (this.getMoney() == null ? other.getMoney() == null : this.getMoney().equals(other.getMoney()))
                && (this.getIsVip() == null ? other.getIsVip() == null : this.getIsVip().equals(other.getIsVip()))
                && (this.getVipOutTime() == null ? other.getVipOutTime() == null : this.getVipOutTime().equals(other.getVipOutTime()))
                && (this.getFoodCoupon() == null ? other.getFoodCoupon() == null : this.getFoodCoupon().equals(other.getFoodCoupon()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getPhone() == null) ? 0 : getPhone().hashCode());
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getWxUserId() == null) ? 0 : getWxUserId().hashCode());
        result = prime * result + ((getSource() == null) ? 0 : getSource().hashCode());
        result = prime * result + ((getMoney() == null) ? 0 : getMoney().hashCode());
        result = prime * result + ((getIsVip() == null) ? 0 : getIsVip().hashCode());
        result = prime * result + ((getVipOutTime() == null) ? 0 : getVipOutTime().hashCode());
        result = prime * result + ((getFoodCoupon() == null) ? 0 : getFoodCoupon().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", phone=").append(phone);
        sb.append(", id=").append(id);
        sb.append(", wxUserId=").append(wxUserId);
        sb.append(", source=").append(source);
        sb.append(", money=").append(money);
        sb.append(", isVip=").append(isVip);
        sb.append(", vipOutTime=").append(vipOutTime);
        sb.append(", foodCoupon=").append(foodCoupon);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}