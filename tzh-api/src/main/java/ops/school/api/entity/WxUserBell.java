package ops.school.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.math.BigDecimal;

@JsonInclude(Include.NON_NULL)
public class WxUserBell {
    @TableId(type = IdType.INPUT)
    private String phone;

    private Integer source;

    private BigDecimal money;

    private Integer isVip;

    private Long vipOutTime;

    private BigDecimal foodCoupon;

    public BigDecimal getFoodCoupon() {
        return foodCoupon;
    }

    public void setFoodCoupon(BigDecimal foodCoupon) {
        this.foodCoupon = foodCoupon;
    }

    public WxUserBell() {
        super();
    }

    public WxUserBell(String phone2) {
        this.phone = phone2;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
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
}