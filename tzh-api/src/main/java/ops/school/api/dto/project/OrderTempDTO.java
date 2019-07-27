package ops.school.api.dto.project;

import java.math.BigDecimal;
import java.util.Objects;

public class OrderTempDTO {

    /**
     * 配送费
     */
    private BigDecimal sendPrice;

    /**
     * 餐盒费
     */
    private BigDecimal boxPrice;

    /**
     * 实付款
     */
    private BigDecimal payPrice;

    /**
     * 原菜价
     */
    private BigDecimal productPrice;

    /**
     * 优惠的价格
     */
    private BigDecimal discountPrice;

    /**
     * 粮票金额
     */
    private BigDecimal payFoodCoupon;

    public OrderTempDTO() {
    }

    public OrderTempDTO(BigDecimal sendPrice, BigDecimal boxPrice, BigDecimal payPrice, BigDecimal productPrice, BigDecimal discountPrice, BigDecimal payFoodCoupon) {
        this.sendPrice = sendPrice;
        this.boxPrice = boxPrice;
        this.payPrice = payPrice;
        this.productPrice = productPrice;
        this.discountPrice = discountPrice;
        this.payFoodCoupon = payFoodCoupon;
    }

    public BigDecimal getSendPrice() {
        return sendPrice;
    }

    public void setSendPrice(BigDecimal sendPrice) {
        this.sendPrice = sendPrice;
    }

    public BigDecimal getBoxPrice() {
        return boxPrice;
    }

    public void setBoxPrice(BigDecimal boxPrice) {
        this.boxPrice = boxPrice;
    }

    public BigDecimal getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(BigDecimal payPrice) {
        this.payPrice = payPrice;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public BigDecimal getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(BigDecimal discountPrice) {
        this.discountPrice = discountPrice;
    }

    public BigDecimal getPayFoodCoupon() {
        return payFoodCoupon;
    }

    public void setPayFoodCoupon(BigDecimal payFoodCoupon) {
        this.payFoodCoupon = payFoodCoupon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderTempDTO that = (OrderTempDTO) o;
        return sendPrice.equals(that.sendPrice) &&
                boxPrice.equals(that.boxPrice) &&
                payPrice.equals(that.payPrice) &&
                productPrice.equals(that.productPrice) &&
                discountPrice.equals(that.discountPrice) &&
                payFoodCoupon.equals(that.payFoodCoupon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sendPrice, boxPrice, payPrice, productPrice, discountPrice, payFoodCoupon);
    }
}
