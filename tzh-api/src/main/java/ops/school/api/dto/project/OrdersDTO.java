package ops.school.api.dto.project;

    import ops.school.api.entity.Orders;

    import java.math.BigDecimal;
    import java.util.Date;
    import java.io.Serializable;


public class OrdersDTO extends BaseDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    /**订单*/
    private String id;
    /**主体id*/
    private Integer appId;
    /**学校id*/
    private Integer schoolId;
    /**楼上楼下差价*/
    private BigDecimal schoolTopDownPrice;
    /**店铺id*/
    private Integer shopId;
    /**店铺名字*/
    private String shopName;
    /**店铺图片*/
    private String shopImage;
    /**店铺地址*/
    private String shopAddress;
    /**店铺电话*/
    private String shopPhone;
    /**用户id*/
    private String openId;
    /**收货人姓名*/
    private String addressName;
    /**收货人手机*/
    private String addressPhone;
    /**收货人详细地址*/
    private String addressDetail;
    /**楼栋id*/
    private Integer floorId;
    /**订单类型*/
    private String typ;
    /**订单状态*/
    private String status;
    /**餐盒费*/
    private BigDecimal boxPrice;
    /**配送费*/
    private BigDecimal sendPrice;
    /**基础配送费*/
    private BigDecimal sendBasePrice;
    /**额外距离配送费*/
    private BigDecimal sendAddDistancePrice;
    /**额外件数配送费*/
    private BigDecimal sendAddCountPrice;
    /**商品费用*/
    private BigDecimal productPrice;
    /**优惠类型*/
    private String discountType;
    /**优惠价格*/
    private BigDecimal discountPrice;
    /**优惠卷主键id*/
    private Long couponId;
    /**优惠券满足条件金额*/
    private BigDecimal couponFullAmount;
    /**优惠券减去金额*/
    private BigDecimal couponUsedAmount;
    /**店铺满减表id*/
    private Long fullCutId;
    /**店铺满减满足条件金额*/
    private BigDecimal fullAmount;
    /**店铺满减减去金额*/
    private BigDecimal fullUsedAmount;
    /**订单原价,菜价+配送费+餐盒费*/
    private BigDecimal originalPrice;
    /**实际付款*/
    private BigDecimal payPrice;
    /**配送员名字*/
    private String senderName;
    /**配送员手机*/
    private String senderPhone;
    /**配送员id*/
    private Integer senderId;
    /**是否送达*/
    private Integer destination;
    /**备注*/
    private String remark;
    /**订单相对于店铺的流水号*/
    private Integer waterNumber;
    /**创建时间*/
    private Date createTime;
    /**支付方式*/
    private String payment;
    /**支付时间*/
    private String payTime;
    /**支付时间戳*/
    private Long payTimeLong;
    /**配送取得物品标志*/
    private Integer sendGetFlag;
    /**送达时间*/
    private String endTime;
    /**是否评论*/
    private Integer evaluateFlag;
    /**预定送达时间*/
    private String reseverTime;
    /**商家接手时间*/
    private String shopAcceptTime;
    /**粮票金额*/
    private BigDecimal payFoodCoupon;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

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
}