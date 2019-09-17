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


    /**
     * @date:   2019/8/2 18:19
     * @author: QinDaoFang
     * @version:version
     * @return: boolean
     * @param   o
     * @Desc:   desc 这里不不能用equals比较，要用数值比较
     */
    public boolean thisCompareToTempTrue(Object o) {
        if (this == o) {return true;}
        if (o == null ) {return false;}
        OrderTempDTO that = (OrderTempDTO) o;
        if (this.getSendPrice() == null){
            this.setSendPrice(new BigDecimal(0));
        }
        if (that.getSendPrice() == null){
            that.setSendPrice(new BigDecimal(0));
        }
        if (this.getSendPrice().compareTo(that.getSendPrice()) != 0){
            return false;
        }
        //1
        if (this.getBoxPrice() == null){
            this.setBoxPrice(new BigDecimal(0));
        }
        if (that.getBoxPrice() == null){
            that.setBoxPrice(new BigDecimal(0));
        }
        if (this.getBoxPrice().compareTo(that.getBoxPrice()) != 0){
            return false;
        }
        //1
        if (this.getPayPrice() == null){
            this.setPayPrice(new BigDecimal(0));
        }
        if (that.getPayPrice() == null){
            that.setPayPrice(new BigDecimal(0));
        }
        if (this.getPayPrice().compareTo(that.getPayPrice()) != 0){
            return false;
        }
        //1
        if (this.getProductPrice() == null){
            this.setProductPrice(new BigDecimal(0));
        }
        if (that.getProductPrice() == null){
            that.setProductPrice(new BigDecimal(0));
        }
        if (this.getProductPrice().compareTo(that.getProductPrice()) != 0){
            return false;
        }
        //1
        if (this.getDiscountPrice() == null){
            this.setDiscountPrice(new BigDecimal(0));
        }
        if (that.getDiscountPrice() == null){
            that.setDiscountPrice(new BigDecimal(0));
        }
        if (this.getDiscountPrice().compareTo(that.getDiscountPrice()) != 0){
            return false;
        }
        //1
        if (this.getPayFoodCoupon() == null){
            this.setPayFoodCoupon(new BigDecimal(0));
        }
        if (that.getPayFoodCoupon() == null){
            that.setPayFoodCoupon(new BigDecimal(0));
        }
        if (this.getPayFoodCoupon().compareTo(that.getPayFoodCoupon()) != 0){
            return false;
        }
        //1
        return true;
    }



    @Override
    public int hashCode() {
        return Objects.hash(sendPrice, boxPrice, payPrice, productPrice, discountPrice, payFoodCoupon);
    }


    @Override
    public String toString() {
        return "OrderTempDTO{" +
                "sendPrice=" + sendPrice +
                ", boxPrice=" + boxPrice +
                ", payPrice=" + payPrice +
                ", productPrice=" + productPrice +
                ", discountPrice=" + discountPrice +
                ", payFoodCoupon=" + payFoodCoupon +
                '}';
    }
}
