package ops.school.api.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import ops.school.api.util.BaiduUtil;
import ops.school.api.util.Util;

/**
 * orders
 * @author 
 */
@Table(name="orders")
public class Orders implements Serializable {
    /**
     * 订单
     */
    @TableId(type = IdType.INPUT)
    private String id;

    /**
     * 主体id
     */
    @NotNull
    private Integer appId;

    /**
     * 学校id
     */
    @NotNull
    private Integer schoolId;

    /**
     * 楼上楼下差价
     */
    @NotNull
    private BigDecimal schoolTopDownPrice;

    /**
     * 店铺id
     */
    @NotNull
    private Integer shopId;

    /**
     * 店铺名字
     */
    @NotBlank
    private String shopName;

    /**
     * 店铺图片
     */
    @NotBlank
    private String shopImage;

    /**
     * 店铺地址
     */
    @NotBlank
    private String shopAddress;

    /**
     * 店铺电话
     */
    @NotBlank
    private String shopPhone;

    /**
     * 用户id
     */
    @NotBlank
    private String openId;

    /**
     * 收货人姓名
     */
    @NotBlank
    private String addressName;

    /**
     * 收货人手机
     */
    @NotBlank
    private String addressPhone;

    /**
     * 收货人详细地址
     */
    @NotBlank
    private String addressDetail;

    /**
     * 楼栋id
     */
    @NotNull
    private Integer floorId;

    /**
     * 订单类型
     */
    @NotBlank
    @Pattern(regexp = "外卖订单|堂食订单|跑腿订单|自取订单")
    private String typ;

    /**
     * 订单状态
     */
    @NotBlank
    private String status;

    /**
     * 餐盒费
     */
    @NotNull
    private BigDecimal boxPrice;

    /**
     * 配送费
     */
    @NotNull
    private BigDecimal sendPrice;

    /**
     * 基础配送费
     */
    @NotNull
    private BigDecimal sendBasePrice;

    /**
     * 额外距离配送费
     */
    @NotNull
    private BigDecimal sendAddDistancePrice;

    /**
     * 额外件数配送费
     */
    @NotNull
    private BigDecimal sendAddCountPrice;

    /**
     * 商品费用
     */
    @NotNull
    private BigDecimal productPrice;

    /**
     * 优惠类型
     */
    private String discountType;

    /**
     * 优惠价格
     */
    private BigDecimal discountPrice;

    /**
     * 优惠卷主键id
     */
    private Long couponId;

    /**
     * 优惠券满足条件金额
     */
    private BigDecimal couponFullAmount;

    /**
     * 优惠券减去金额
     */
    private BigDecimal couponUsedAmount;

    /**
     * 店铺满减表id
     */
    private Long fullCutId;

    /**
     * 店铺满减满足条件金额
     */
    private BigDecimal fullAmount;

    /**
     * 店铺满减减去金额
     */
    private BigDecimal fullUsedAmount;

    /**
     * 订单原价,菜价+配送费+餐盒费
     */
    private BigDecimal originalPrice;

    /**
     * 实际付款
     */
    @NotNull
    private BigDecimal payPrice;

    /**
     * 配送员名字
     */
    private String senderName;

    /**
     * 配送员手机
     */
    private String senderPhone;

    /**
     * 配送员id
     */
    private Integer senderId;

    /**
     * 是否送达
     */
    private Integer destination;

    /**
     * 备注
     */
    @NotBlank
    private String remark;

    /**
     * 订单相对于店铺的流水号
     */
    private Integer waterNumber;

    /**
     * 创建时间
     */
    @NotNull
    private Date createTime;

    /**
     * 支付方式
     */
    private String payment;

    /**
     * 支付时间
     */
    private String payTime;

    /**
     * 支付时间戳
     */
    private Long payTimeLong;

    /**
     * 配送取得物品标志
     */
    @NotNull
    private Integer sendGetFlag;

    /**
     * 送达时间
     */
    private String endTime;

    /**
     * 是否评论
     */
    @NotNull
    private Integer evaluateFlag;

    /**
     * 预定送达时间
     */
    private String reseverTime;

    /**
     * 商家接手时间
     */
    @NotBlank
    private String shopAcceptTime;

    /**
     * 粮票金额
     */
    @NotNull
    private BigDecimal payFoodCoupon;

    private List<OrderProduct> op;

    private OrdersComplete complete;

    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public Integer getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Integer schoolId) {
        this.schoolId = schoolId;
    }

    public BigDecimal getSchoolTopDownPrice() {
        return schoolTopDownPrice;
    }

    public void setSchoolTopDownPrice(BigDecimal schoolTopDownPrice) {
        this.schoolTopDownPrice = schoolTopDownPrice;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopImage() {
        return shopImage;
    }

    public void setShopImage(String shopImage) {
        this.shopImage = shopImage;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }

    public String getShopPhone() {
        return shopPhone;
    }

    public void setShopPhone(String shopPhone) {
        this.shopPhone = shopPhone;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public String getAddressPhone() {
        return addressPhone;
    }

    public void setAddressPhone(String addressPhone) {
        this.addressPhone = addressPhone;
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    public Integer getFloorId() {
        return floorId;
    }

    public void setFloorId(Integer floorId) {
        this.floorId = floorId;
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getBoxPrice() {
        return boxPrice;
    }

    public void setBoxPrice(BigDecimal boxPrice) {
        this.boxPrice = boxPrice;
    }

    public BigDecimal getSendPrice() {
        return sendPrice;
    }

    public void setSendPrice(BigDecimal sendPrice) {
        this.sendPrice = sendPrice;
    }

    public BigDecimal getSendBasePrice() {
        return sendBasePrice;
    }

    public void setSendBasePrice(BigDecimal sendBasePrice) {
        this.sendBasePrice = sendBasePrice;
    }

    public BigDecimal getSendAddDistancePrice() {
        return sendAddDistancePrice;
    }

    public void setSendAddDistancePrice(BigDecimal sendAddDistancePrice) {
        this.sendAddDistancePrice = sendAddDistancePrice;
    }

    public BigDecimal getSendAddCountPrice() {
        return sendAddCountPrice;
    }

    public void setSendAddCountPrice(BigDecimal sendAddCountPrice) {
        this.sendAddCountPrice = sendAddCountPrice;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(BigDecimal discountPrice) {
        this.discountPrice = discountPrice;
    }

    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }

    public BigDecimal getCouponFullAmount() {
        return couponFullAmount;
    }

    public void setCouponFullAmount(BigDecimal couponFullAmount) {
        this.couponFullAmount = couponFullAmount;
    }

    public BigDecimal getCouponUsedAmount() {
        return couponUsedAmount;
    }

    public void setCouponUsedAmount(BigDecimal couponUsedAmount) {
        this.couponUsedAmount = couponUsedAmount;
    }

    public Long getFullCutId() {
        return fullCutId;
    }

    public void setFullCutId(Long fullCutId) {
        this.fullCutId = fullCutId;
    }

    public BigDecimal getFullAmount() {
        return fullAmount;
    }

    public void setFullAmount(BigDecimal fullAmount) {
        this.fullAmount = fullAmount;
    }

    public BigDecimal getFullUsedAmount() {
        return fullUsedAmount;
    }

    public void setFullUsedAmount(BigDecimal fullUsedAmount) {
        this.fullUsedAmount = fullUsedAmount;
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public BigDecimal getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(BigDecimal payPrice) {
        this.payPrice = payPrice;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderPhone() {
        return senderPhone;
    }

    public void setSenderPhone(String senderPhone) {
        this.senderPhone = senderPhone;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public Integer getDestination() {
        return destination;
    }

    public void setDestination(Integer destination) {
        this.destination = destination;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getWaterNumber() {
        return waterNumber;
    }

    public void setWaterNumber(Integer waterNumber) {
        this.waterNumber = waterNumber;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public Long getPayTimeLong() {
        return payTimeLong;
    }

    public void setPayTimeLong(Long payTimeLong) {
        this.payTimeLong = payTimeLong;
    }

    public Integer getSendGetFlag() {
        return sendGetFlag;
    }

    public void setSendGetFlag(Integer sendGetFlag) {
        this.sendGetFlag = sendGetFlag;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getEvaluateFlag() {
        return evaluateFlag;
    }

    public void setEvaluateFlag(Integer evaluateFlag) {
        this.evaluateFlag = evaluateFlag;
    }

    public String getReseverTime() {
        return reseverTime;
    }

    public void setReseverTime(String reseverTime) {
        this.reseverTime = reseverTime;
    }

    public String getShopAcceptTime() {
        return shopAcceptTime;
    }

    public void setShopAcceptTime(String shopAcceptTime) {
        this.shopAcceptTime = shopAcceptTime;
    }

    public BigDecimal getPayFoodCoupon() {
        return payFoodCoupon;
    }

    public void setPayFoodCoupon(BigDecimal payFoodCoupon) {
        this.payFoodCoupon = payFoodCoupon;
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
        Orders other = (Orders) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getAppId() == null ? other.getAppId() == null : this.getAppId().equals(other.getAppId()))
            && (this.getSchoolId() == null ? other.getSchoolId() == null : this.getSchoolId().equals(other.getSchoolId()))
            && (this.getSchoolTopDownPrice() == null ? other.getSchoolTopDownPrice() == null : this.getSchoolTopDownPrice().equals(other.getSchoolTopDownPrice()))
            && (this.getShopId() == null ? other.getShopId() == null : this.getShopId().equals(other.getShopId()))
            && (this.getShopName() == null ? other.getShopName() == null : this.getShopName().equals(other.getShopName()))
            && (this.getShopImage() == null ? other.getShopImage() == null : this.getShopImage().equals(other.getShopImage()))
            && (this.getShopAddress() == null ? other.getShopAddress() == null : this.getShopAddress().equals(other.getShopAddress()))
            && (this.getShopPhone() == null ? other.getShopPhone() == null : this.getShopPhone().equals(other.getShopPhone()))
            && (this.getOpenId() == null ? other.getOpenId() == null : this.getOpenId().equals(other.getOpenId()))
            && (this.getAddressName() == null ? other.getAddressName() == null : this.getAddressName().equals(other.getAddressName()))
            && (this.getAddressPhone() == null ? other.getAddressPhone() == null : this.getAddressPhone().equals(other.getAddressPhone()))
            && (this.getAddressDetail() == null ? other.getAddressDetail() == null : this.getAddressDetail().equals(other.getAddressDetail()))
            && (this.getFloorId() == null ? other.getFloorId() == null : this.getFloorId().equals(other.getFloorId()))
            && (this.getTyp() == null ? other.getTyp() == null : this.getTyp().equals(other.getTyp()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getBoxPrice() == null ? other.getBoxPrice() == null : this.getBoxPrice().equals(other.getBoxPrice()))
            && (this.getSendPrice() == null ? other.getSendPrice() == null : this.getSendPrice().equals(other.getSendPrice()))
            && (this.getSendBasePrice() == null ? other.getSendBasePrice() == null : this.getSendBasePrice().equals(other.getSendBasePrice()))
            && (this.getSendAddDistancePrice() == null ? other.getSendAddDistancePrice() == null : this.getSendAddDistancePrice().equals(other.getSendAddDistancePrice()))
            && (this.getSendAddCountPrice() == null ? other.getSendAddCountPrice() == null : this.getSendAddCountPrice().equals(other.getSendAddCountPrice()))
            && (this.getProductPrice() == null ? other.getProductPrice() == null : this.getProductPrice().equals(other.getProductPrice()))
            && (this.getDiscountType() == null ? other.getDiscountType() == null : this.getDiscountType().equals(other.getDiscountType()))
            && (this.getDiscountPrice() == null ? other.getDiscountPrice() == null : this.getDiscountPrice().equals(other.getDiscountPrice()))
            && (this.getCouponId() == null ? other.getCouponId() == null : this.getCouponId().equals(other.getCouponId()))
            && (this.getCouponFullAmount() == null ? other.getCouponFullAmount() == null : this.getCouponFullAmount().equals(other.getCouponFullAmount()))
            && (this.getCouponUsedAmount() == null ? other.getCouponUsedAmount() == null : this.getCouponUsedAmount().equals(other.getCouponUsedAmount()))
            && (this.getFullCutId() == null ? other.getFullCutId() == null : this.getFullCutId().equals(other.getFullCutId()))
            && (this.getFullAmount() == null ? other.getFullAmount() == null : this.getFullAmount().equals(other.getFullAmount()))
            && (this.getFullUsedAmount() == null ? other.getFullUsedAmount() == null : this.getFullUsedAmount().equals(other.getFullUsedAmount()))
            && (this.getOriginalPrice() == null ? other.getOriginalPrice() == null : this.getOriginalPrice().equals(other.getOriginalPrice()))
            && (this.getPayPrice() == null ? other.getPayPrice() == null : this.getPayPrice().equals(other.getPayPrice()))
            && (this.getSenderName() == null ? other.getSenderName() == null : this.getSenderName().equals(other.getSenderName()))
            && (this.getSenderPhone() == null ? other.getSenderPhone() == null : this.getSenderPhone().equals(other.getSenderPhone()))
            && (this.getSenderId() == null ? other.getSenderId() == null : this.getSenderId().equals(other.getSenderId()))
            && (this.getDestination() == null ? other.getDestination() == null : this.getDestination().equals(other.getDestination()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()))
            && (this.getWaterNumber() == null ? other.getWaterNumber() == null : this.getWaterNumber().equals(other.getWaterNumber()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getPayment() == null ? other.getPayment() == null : this.getPayment().equals(other.getPayment()))
            && (this.getPayTime() == null ? other.getPayTime() == null : this.getPayTime().equals(other.getPayTime()))
            && (this.getPayTimeLong() == null ? other.getPayTimeLong() == null : this.getPayTimeLong().equals(other.getPayTimeLong()))
            && (this.getSendGetFlag() == null ? other.getSendGetFlag() == null : this.getSendGetFlag().equals(other.getSendGetFlag()))
            && (this.getEndTime() == null ? other.getEndTime() == null : this.getEndTime().equals(other.getEndTime()))
            && (this.getEvaluateFlag() == null ? other.getEvaluateFlag() == null : this.getEvaluateFlag().equals(other.getEvaluateFlag()))
            && (this.getReseverTime() == null ? other.getReseverTime() == null : this.getReseverTime().equals(other.getReseverTime()))
            && (this.getShopAcceptTime() == null ? other.getShopAcceptTime() == null : this.getShopAcceptTime().equals(other.getShopAcceptTime()))
            && (this.getPayFoodCoupon() == null ? other.getPayFoodCoupon() == null : this.getPayFoodCoupon().equals(other.getPayFoodCoupon()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getAppId() == null) ? 0 : getAppId().hashCode());
        result = prime * result + ((getSchoolId() == null) ? 0 : getSchoolId().hashCode());
        result = prime * result + ((getSchoolTopDownPrice() == null) ? 0 : getSchoolTopDownPrice().hashCode());
        result = prime * result + ((getShopId() == null) ? 0 : getShopId().hashCode());
        result = prime * result + ((getShopName() == null) ? 0 : getShopName().hashCode());
        result = prime * result + ((getShopImage() == null) ? 0 : getShopImage().hashCode());
        result = prime * result + ((getShopAddress() == null) ? 0 : getShopAddress().hashCode());
        result = prime * result + ((getShopPhone() == null) ? 0 : getShopPhone().hashCode());
        result = prime * result + ((getOpenId() == null) ? 0 : getOpenId().hashCode());
        result = prime * result + ((getAddressName() == null) ? 0 : getAddressName().hashCode());
        result = prime * result + ((getAddressPhone() == null) ? 0 : getAddressPhone().hashCode());
        result = prime * result + ((getAddressDetail() == null) ? 0 : getAddressDetail().hashCode());
        result = prime * result + ((getFloorId() == null) ? 0 : getFloorId().hashCode());
        result = prime * result + ((getTyp() == null) ? 0 : getTyp().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getBoxPrice() == null) ? 0 : getBoxPrice().hashCode());
        result = prime * result + ((getSendPrice() == null) ? 0 : getSendPrice().hashCode());
        result = prime * result + ((getSendBasePrice() == null) ? 0 : getSendBasePrice().hashCode());
        result = prime * result + ((getSendAddDistancePrice() == null) ? 0 : getSendAddDistancePrice().hashCode());
        result = prime * result + ((getSendAddCountPrice() == null) ? 0 : getSendAddCountPrice().hashCode());
        result = prime * result + ((getProductPrice() == null) ? 0 : getProductPrice().hashCode());
        result = prime * result + ((getDiscountType() == null) ? 0 : getDiscountType().hashCode());
        result = prime * result + ((getDiscountPrice() == null) ? 0 : getDiscountPrice().hashCode());
        result = prime * result + ((getCouponId() == null) ? 0 : getCouponId().hashCode());
        result = prime * result + ((getCouponFullAmount() == null) ? 0 : getCouponFullAmount().hashCode());
        result = prime * result + ((getCouponUsedAmount() == null) ? 0 : getCouponUsedAmount().hashCode());
        result = prime * result + ((getFullCutId() == null) ? 0 : getFullCutId().hashCode());
        result = prime * result + ((getFullAmount() == null) ? 0 : getFullAmount().hashCode());
        result = prime * result + ((getFullUsedAmount() == null) ? 0 : getFullUsedAmount().hashCode());
        result = prime * result + ((getOriginalPrice() == null) ? 0 : getOriginalPrice().hashCode());
        result = prime * result + ((getPayPrice() == null) ? 0 : getPayPrice().hashCode());
        result = prime * result + ((getSenderName() == null) ? 0 : getSenderName().hashCode());
        result = prime * result + ((getSenderPhone() == null) ? 0 : getSenderPhone().hashCode());
        result = prime * result + ((getSenderId() == null) ? 0 : getSenderId().hashCode());
        result = prime * result + ((getDestination() == null) ? 0 : getDestination().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        result = prime * result + ((getWaterNumber() == null) ? 0 : getWaterNumber().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getPayment() == null) ? 0 : getPayment().hashCode());
        result = prime * result + ((getPayTime() == null) ? 0 : getPayTime().hashCode());
        result = prime * result + ((getPayTimeLong() == null) ? 0 : getPayTimeLong().hashCode());
        result = prime * result + ((getSendGetFlag() == null) ? 0 : getSendGetFlag().hashCode());
        result = prime * result + ((getEndTime() == null) ? 0 : getEndTime().hashCode());
        result = prime * result + ((getEvaluateFlag() == null) ? 0 : getEvaluateFlag().hashCode());
        result = prime * result + ((getReseverTime() == null) ? 0 : getReseverTime().hashCode());
        result = prime * result + ((getShopAcceptTime() == null) ? 0 : getShopAcceptTime().hashCode());
        result = prime * result + ((getPayFoodCoupon() == null) ? 0 : getPayFoodCoupon().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", appId=").append(appId);
        sb.append(", schoolId=").append(schoolId);
        sb.append(", schoolTopDownPrice=").append(schoolTopDownPrice);
        sb.append(", shopId=").append(shopId);
        sb.append(", shopName=").append(shopName);
        sb.append(", shopImage=").append(shopImage);
        sb.append(", shopAddress=").append(shopAddress);
        sb.append(", shopPhone=").append(shopPhone);
        sb.append(", openId=").append(openId);
        sb.append(", addressName=").append(addressName);
        sb.append(", addressPhone=").append(addressPhone);
        sb.append(", addressDetail=").append(addressDetail);
        sb.append(", floorId=").append(floorId);
        sb.append(", typ=").append(typ);
        sb.append(", status=").append(status);
        sb.append(", boxPrice=").append(boxPrice);
        sb.append(", sendPrice=").append(sendPrice);
        sb.append(", sendBasePrice=").append(sendBasePrice);
        sb.append(", sendAddDistancePrice=").append(sendAddDistancePrice);
        sb.append(", sendAddCountPrice=").append(sendAddCountPrice);
        sb.append(", productPrice=").append(productPrice);
        sb.append(", discountType=").append(discountType);
        sb.append(", discountPrice=").append(discountPrice);
        sb.append(", couponId=").append(couponId);
        sb.append(", couponFullAmount=").append(couponFullAmount);
        sb.append(", couponUsedAmount=").append(couponUsedAmount);
        sb.append(", fullCutId=").append(fullCutId);
        sb.append(", fullAmount=").append(fullAmount);
        sb.append(", fullUsedAmount=").append(fullUsedAmount);
        sb.append(", originalPrice=").append(originalPrice);
        sb.append(", payPrice=").append(payPrice);
        sb.append(", senderName=").append(senderName);
        sb.append(", senderPhone=").append(senderPhone);
        sb.append(", senderId=").append(senderId);
        sb.append(", destination=").append(destination);
        sb.append(", remark=").append(remark);
        sb.append(", waterNumber=").append(waterNumber);
        sb.append(", createTime=").append(createTime);
        sb.append(", payment=").append(payment);
        sb.append(", payTime=").append(payTime);
        sb.append(", payTimeLong=").append(payTimeLong);
        sb.append(", sendGetFlag=").append(sendGetFlag);
        sb.append(", endTime=").append(endTime);
        sb.append(", evaluateFlag=").append(evaluateFlag);
        sb.append(", reseverTime=").append(reseverTime);
        sb.append(", shopAcceptTime=").append(shopAcceptTime);
        sb.append(", payFoodCoupon=").append(payFoodCoupon);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
    public void takeoutinit1(WxUser wxUser, School school, Shop shop, Floor floor, int totalcount, boolean isDiscount, List<FullCut> fullcut, int boxcount) {
        this.schoolId = school.getId();
        this.appId = school.getAppId();
        this.schoolTopDownPrice = school.getTopDown();
        this.shopId = shop.getId();
        this.shopAddress = shop.getShopAddress();
        this.shopImage = shop.getShopImage();
        this.shopName = shop.getShopName();
        this.shopPhone = shop.getShopPhone();
        if (!isDiscount) {
            for (FullCut temp : fullcut) {
                if (this.productPrice.compareTo(new BigDecimal(temp.getFull())) != -1) {
                    this.setDiscountType("满减优惠");
                    this.discountPrice = new BigDecimal(temp.getCut());
                    this.productPrice = this.productPrice.subtract(this.discountPrice);
                    break;
                }
            }
        }
        //计算餐盒费
        if (this.typ.equals("外卖订单") || this.typ.equals("自取订单")) {
            this.boxPrice = shop.getBoxPrice().multiply(new BigDecimal(boxcount));
        } else {
            this.boxPrice = new BigDecimal(0);
        }
        //开始计算外卖的配送费
        if (this.typ.equals("外卖订单")) {
            this.sendBasePrice = shop.getSendPrice();
            //按物品件数增加配送费
            if (shop.getSendPriceAddByCountFlag() == 1) {
                this.sendAddCountPrice = new BigDecimal((totalcount - 1)).multiply(shop.getSendPriceAdd());
            } else {
                this.sendAddCountPrice = new BigDecimal(0);
            }
            //判断配送距离
            int distance = BaiduUtil.DistanceAll(floor.getLat() + "," + floor.getLng(), shop.getLat() + "," + shop.getLng());
            if (distance > school.getSendMaxDistance()) {
                int per = (distance / school.getSendPerOut()) + 1;
                this.sendAddDistancePrice = new BigDecimal(per).multiply(school.getSendPerMoney());
            } else {
                this.sendAddDistancePrice = new BigDecimal(0);
            }
        } else {
            this.sendBasePrice = new BigDecimal(0);
            this.sendAddDistancePrice = new BigDecimal(0);
            this.sendAddCountPrice = new BigDecimal(0);
        }
        //计算总的配送费
        this.sendPrice = this.sendBasePrice.add(this.sendAddCountPrice).add(this.sendAddDistancePrice);
        //计算总价
        this.payPrice = this.productPrice.add(this.boxPrice).add(this.sendPrice);
    }

    public void init() {
        synchronized (this) {
            this.id = Util.GenerateOrderId();
        }
    }

    public List<OrderProduct> getOp() {
        return op;
    }

    public void setOp(List<OrderProduct> op) {
        this.op = op;
    }

    public OrdersComplete getComplete() {
        return complete;
    }

    public void setComplete(OrdersComplete complete) {
        this.complete = complete;
    }
}