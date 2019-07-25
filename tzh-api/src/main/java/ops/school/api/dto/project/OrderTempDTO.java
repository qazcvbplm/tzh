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
     * 优惠后价格
     */
    private BigDecimal afterDiscountPrice;

    public OrderTempDTO() {
    }

    public OrderTempDTO(BigDecimal sendPrice, BigDecimal boxPrice, BigDecimal payPrice, BigDecimal productPrice, BigDecimal afterDiscountPrice) {
        this.sendPrice = sendPrice;
        this.boxPrice = boxPrice;
        this.payPrice = payPrice;
        this.productPrice = productPrice;
        this.afterDiscountPrice = afterDiscountPrice;
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

    public BigDecimal getAfterDiscountPrice() {
        return afterDiscountPrice;
    }

    public void setAfterDiscountPrice(BigDecimal afterDiscountPrice) {
        this.afterDiscountPrice = afterDiscountPrice;
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
                afterDiscountPrice.equals(that.afterDiscountPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sendPrice, boxPrice, payPrice, productPrice, afterDiscountPrice);
    }
}
