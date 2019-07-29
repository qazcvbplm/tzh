package ops.school.api.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import ops.school.api.dto.project.ProductOrderDTO;
import ops.school.api.util.BaiduUtil;
import ops.school.api.util.Util;

import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * orders
 * @author 
 */
@Table(name="orders")
public class Orders extends Base implements Serializable {
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
     * 用户电话
     */
    @NotBlank
    @TableField(exist = false)
    private String wxUserPhone;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
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

    private BigDecimal afterDiscountPrice;

    @TableField(exist = false)
    private List<OrderProduct> op;

    @TableField(exist = false)
    private OrdersComplete complete;

    @TableField(exist = false)
    List<ProductOrderDTO> productOrderDTOS;

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

    public BigDecimal getAfterDiscountPrice() {
        return afterDiscountPrice;
    }

    public void setAfterDiscountPrice(BigDecimal afterDiscountPrice) {
        this.afterDiscountPrice = afterDiscountPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        Orders orders = (Orders) o;
        return Objects.equals(id, orders.id) &&
                Objects.equals(appId, orders.appId) &&
                Objects.equals(schoolId, orders.schoolId) &&
                Objects.equals(schoolTopDownPrice, orders.schoolTopDownPrice) &&
                Objects.equals(shopId, orders.shopId) &&
                Objects.equals(shopName, orders.shopName) &&
                Objects.equals(shopImage, orders.shopImage) &&
                Objects.equals(shopAddress, orders.shopAddress) &&
                Objects.equals(shopPhone, orders.shopPhone) &&
                Objects.equals(openId, orders.openId) &&
                Objects.equals(addressName, orders.addressName) &&
                Objects.equals(addressPhone, orders.addressPhone) &&
                Objects.equals(addressDetail, orders.addressDetail) &&
                Objects.equals(floorId, orders.floorId) &&
                Objects.equals(typ, orders.typ) &&
                Objects.equals(status, orders.status) &&
                Objects.equals(boxPrice, orders.boxPrice) &&
                Objects.equals(sendPrice, orders.sendPrice) &&
                Objects.equals(sendBasePrice, orders.sendBasePrice) &&
                Objects.equals(sendAddDistancePrice, orders.sendAddDistancePrice) &&
                Objects.equals(sendAddCountPrice, orders.sendAddCountPrice) &&
                Objects.equals(productPrice, orders.productPrice) &&
                Objects.equals(discountType, orders.discountType) &&
                Objects.equals(discountPrice, orders.discountPrice) &&
                Objects.equals(couponId, orders.couponId) &&
                Objects.equals(couponFullAmount, orders.couponFullAmount) &&
                Objects.equals(couponUsedAmount, orders.couponUsedAmount) &&
                Objects.equals(fullCutId, orders.fullCutId) &&
                Objects.equals(fullAmount, orders.fullAmount) &&
                Objects.equals(fullUsedAmount, orders.fullUsedAmount) &&
                Objects.equals(originalPrice, orders.originalPrice) &&
                Objects.equals(payPrice, orders.payPrice) &&
                Objects.equals(senderName, orders.senderName) &&
                Objects.equals(senderPhone, orders.senderPhone) &&
                Objects.equals(senderId, orders.senderId) &&
                Objects.equals(destination, orders.destination) &&
                Objects.equals(remark, orders.remark) &&
                Objects.equals(waterNumber, orders.waterNumber) &&
                Objects.equals(createTime, orders.createTime) &&
                Objects.equals(payment, orders.payment) &&
                Objects.equals(payTime, orders.payTime) &&
                Objects.equals(payTimeLong, orders.payTimeLong) &&
                Objects.equals(sendGetFlag, orders.sendGetFlag) &&
                Objects.equals(endTime, orders.endTime) &&
                Objects.equals(evaluateFlag, orders.evaluateFlag) &&
                Objects.equals(reseverTime, orders.reseverTime) &&
                Objects.equals(shopAcceptTime, orders.shopAcceptTime) &&
                Objects.equals(payFoodCoupon, orders.payFoodCoupon) &&
                Objects.equals(op, orders.op) &&
                Objects.equals(complete, orders.complete);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, appId, schoolId, schoolTopDownPrice, shopId, shopName, shopImage, shopAddress, shopPhone, openId, addressName, addressPhone, addressDetail, floorId, typ, status, boxPrice, sendPrice, sendBasePrice, sendAddDistancePrice, sendAddCountPrice, productPrice, discountType, discountPrice, couponId, couponFullAmount, couponUsedAmount, fullCutId, fullAmount, fullUsedAmount, originalPrice, payPrice, senderName, senderPhone, senderId, destination, remark, waterNumber, createTime, payment, payTime, payTimeLong, sendGetFlag, endTime, evaluateFlag, reseverTime, shopAcceptTime, payFoodCoupon, op, complete);
    }

    @Override
    public String toString() {
        return "Orders{" +
                "id='" + id + '\'' +
                ", appId=" + appId +
                ", schoolId=" + schoolId +
                ", schoolTopDownPrice=" + schoolTopDownPrice +
                ", shopId=" + shopId +
                ", shopName='" + shopName + '\'' +
                ", shopImage='" + shopImage + '\'' +
                ", shopAddress='" + shopAddress + '\'' +
                ", shopPhone='" + shopPhone + '\'' +
                ", openId='" + openId + '\'' +
                ", wxUserPhone='" + wxUserPhone + '\'' +
                ", addressName='" + addressName + '\'' +
                ", addressPhone='" + addressPhone + '\'' +
                ", addressDetail='" + addressDetail + '\'' +
                ", floorId=" + floorId +
                ", typ='" + typ + '\'' +
                ", status='" + status + '\'' +
                ", boxPrice=" + boxPrice +
                ", sendPrice=" + sendPrice +
                ", sendBasePrice=" + sendBasePrice +
                ", sendAddDistancePrice=" + sendAddDistancePrice +
                ", sendAddCountPrice=" + sendAddCountPrice +
                ", productPrice=" + productPrice +
                ", discountType='" + discountType + '\'' +
                ", discountPrice=" + discountPrice +
                ", couponId=" + couponId +
                ", couponFullAmount=" + couponFullAmount +
                ", couponUsedAmount=" + couponUsedAmount +
                ", fullCutId=" + fullCutId +
                ", fullAmount=" + fullAmount +
                ", fullUsedAmount=" + fullUsedAmount +
                ", originalPrice=" + originalPrice +
                ", payPrice=" + payPrice +
                ", senderName='" + senderName + '\'' +
                ", senderPhone='" + senderPhone + '\'' +
                ", senderId=" + senderId +
                ", destination=" + destination +
                ", remark='" + remark + '\'' +
                ", waterNumber=" + waterNumber +
                ", createTime=" + createTime +
                ", payment='" + payment + '\'' +
                ", payTime='" + payTime + '\'' +
                ", payTimeLong=" + payTimeLong +
                ", sendGetFlag=" + sendGetFlag +
                ", endTime='" + endTime + '\'' +
                ", evaluateFlag=" + evaluateFlag +
                ", reseverTime='" + reseverTime + '\'' +
                ", shopAcceptTime='" + shopAcceptTime + '\'' +
                ", payFoodCoupon=" + payFoodCoupon +
                ", afterDiscountPrice=" + afterDiscountPrice +
                ", op=" + op +
                ", complete=" + complete +
                ", productOrderDTOS=" + productOrderDTOS +
                '}';
    }

    // totalCount 为订单中商品总件数，boxcount 餐盒数，
    public void takeoutinit1(WxUser wxUser, School school, Shop shop, Floor floor, int totalcount, boolean isDiscount, List<FullCut> fullcut, int boxcount) {
        this.schoolId = school.getId();
        this.appId = school.getAppId();
        this.schoolTopDownPrice = school.getTopDown();
        this.shopId = shop.getId();
        this.shopAddress = shop.getShopAddress();
        this.shopImage = shop.getShopImage();
        this.shopName = shop.getShopName();
        this.shopPhone = shop.getShopPhone();
        /*计算满减优惠时，满减使用错误*/
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
            //判断配送距离增加配送费
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
        //计算总价（实付款金额）
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
    public Orders() {
        super();
    }


    public List<ProductOrderDTO> getProductOrderDTOS() {
        return productOrderDTOS;
    }

    public void setProductOrderDTOS(List<ProductOrderDTO> productOrderDTOS) {
        this.productOrderDTOS = productOrderDTOS;
    }

    public String getWxUserPhone() {
        return wxUserPhone;
    }

    public void setWxUserPhone(String wxUserPhone) {
        this.wxUserPhone = wxUserPhone;
    }
}