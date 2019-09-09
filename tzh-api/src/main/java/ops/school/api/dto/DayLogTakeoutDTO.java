package ops.school.api.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import ops.school.api.entity.Orders;
import ops.school.api.entity.School;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class DayLogTakeoutDTO {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String selfName;

    private Integer selfId;

    private Integer parentId;

    private String day;

    private Integer totalCount;

    private BigDecimal totalPrice;

    private BigDecimal selfGet;

    private BigDecimal parentGet;

    private Integer takeoutTotalCount;

    private Integer selfgetTotalCount;

    private String type;

    private BigDecimal boxPrice;

    private BigDecimal sendPrice;

    private BigDecimal productPrice;

    private BigDecimal wxPayGet;

    private BigDecimal bellPayGet;

    private BigDecimal usedCoupon;

    private BigDecimal usedFoodCoupon;

    private BigDecimal usedFullCut;

    private BigDecimal usedDiscount;

    private BigDecimal schoolGetTotal;

    private BigDecimal schoolDayTx;

    private BigDecimal shopDayTx;

    private BigDecimal schoolAllMoney;

    private Integer downSendCount;

    private BigDecimal downSendMoney;



    /**
     * 创建时间
     */
    private Date createTime;

    @TableField(exist = false)
    private Integer isDelete;


    // 当日跑腿负责人所得
    private BigDecimal runSchoolTotal;

    // 跑腿总单数
    private Integer runAllCounts;
    // 跑腿总营业额
    private BigDecimal runMoneyAmount;

    // 外卖总单数
    private Integer orderAllCounts;

    // 外卖总营业额
    private BigDecimal orderAllMoneyAmount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSelfName() {
        return selfName;
    }

    public void setSelfName(String selfName) {
        this.selfName = selfName;
    }

    public Integer getSelfId() {
        return selfId;
    }

    public void setSelfId(Integer selfId) {
        this.selfId = selfId;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getSelfGet() {
        return selfGet;
    }

    public void setSelfGet(BigDecimal selfGet) {
        this.selfGet = selfGet;
    }

    public BigDecimal getParentGet() {
        return parentGet;
    }

    public void setParentGet(BigDecimal parentGet) {
        this.parentGet = parentGet;
    }

    public Integer getTakeoutTotalCount() {
        return takeoutTotalCount;
    }

    public void setTakeoutTotalCount(Integer takeoutTotalCount) {
        this.takeoutTotalCount = takeoutTotalCount;
    }

    public Integer getSelfgetTotalCount() {
        return selfgetTotalCount;
    }

    public void setSelfgetTotalCount(Integer selfgetTotalCount) {
        this.selfgetTotalCount = selfgetTotalCount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public BigDecimal getWxPayGet() {
        return wxPayGet;
    }

    public void setWxPayGet(BigDecimal wxPayGet) {
        this.wxPayGet = wxPayGet;
    }

    public BigDecimal getBellPayGet() {
        return bellPayGet;
    }

    public void setBellPayGet(BigDecimal bellPayGet) {
        this.bellPayGet = bellPayGet;
    }

    public BigDecimal getUsedCoupon() {
        return usedCoupon;
    }

    public void setUsedCoupon(BigDecimal usedCoupon) {
        this.usedCoupon = usedCoupon;
    }

    public BigDecimal getUsedFoodCoupon() {
        return usedFoodCoupon;
    }

    public void setUsedFoodCoupon(BigDecimal usedFoodCoupon) {
        this.usedFoodCoupon = usedFoodCoupon;
    }

    public BigDecimal getUsedFullCut() {
        return usedFullCut;
    }

    public void setUsedFullCut(BigDecimal usedFullCut) {
        this.usedFullCut = usedFullCut;
    }

    public BigDecimal getUsedDiscount() {
        return usedDiscount;
    }

    public void setUsedDiscount(BigDecimal usedDiscount) {
        this.usedDiscount = usedDiscount;
    }

    public BigDecimal getSchoolGetTotal() {
        return schoolGetTotal;
    }

    public void setSchoolGetTotal(BigDecimal schoolGetTotal) {
        this.schoolGetTotal = schoolGetTotal;
    }

    public BigDecimal getSchoolDayTx() {
        return schoolDayTx;
    }

    public void setSchoolDayTx(BigDecimal schoolDayTx) {
        this.schoolDayTx = schoolDayTx;
    }

    public BigDecimal getSchoolAllMoney() {
        return schoolAllMoney;
    }

    public void setSchoolAllMoney(BigDecimal schoolAllMoney) {
        this.schoolAllMoney = schoolAllMoney;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public BigDecimal getRunSchoolTotal() {
        return runSchoolTotal;
    }

    public void setRunSchoolTotal(BigDecimal runSchoolTotal) {
        this.runSchoolTotal = runSchoolTotal;
    }

    public Integer getRunAllCounts() {
        return runAllCounts;
    }

    public void setRunAllCounts(Integer runAllCounts) {
        this.runAllCounts = runAllCounts;
    }

    public BigDecimal getRunMoneyAmount() {
        return runMoneyAmount;
    }

    public void setRunMoneyAmount(BigDecimal runMoneyAmount) {
        this.runMoneyAmount = runMoneyAmount;
    }

    public Integer getOrderAllCounts() {
        return orderAllCounts;
    }

    public void setOrderAllCounts(Integer orderAllCounts) {
        this.orderAllCounts = orderAllCounts;
    }

    public BigDecimal getOrderAllMoneyAmount() {
        return orderAllMoneyAmount;
    }

    public void setOrderAllMoneyAmount(BigDecimal orderAllMoneyAmount) {
        this.orderAllMoneyAmount = orderAllMoneyAmount;
    }

    public BigDecimal getShopDayTx() {
        return shopDayTx;
    }

    public void setShopDayTx(BigDecimal shopDayTx) {
        this.shopDayTx = shopDayTx;
    }


    public Integer getDownSendCount() {
        return downSendCount;
    }

    public void setDownSendCount(Integer downSendCount) {
        this.downSendCount = downSendCount;
    }

    public BigDecimal getDownSendMoney() {
        return downSendMoney;
    }

    public void setDownSendMoney(BigDecimal downSendMoney) {
        this.downSendMoney = downSendMoney;
    }
}