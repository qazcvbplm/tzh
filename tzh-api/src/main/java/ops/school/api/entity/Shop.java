package ops.school.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

public class Shop {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer schoolId;
    @NotBlank
    private String shopName;
    @NotBlank
    private String shopPhone;
    @NotBlank
    private String shopImage;
    @NotNull
    private Integer shopCategoryId;
    @NotNull
    private Integer openFlag;
    @NotNull
    private Integer sendModelFlag;
    @NotNull
    private Integer getModelFlag;
    @NotNull
    private Integer tsModelFlag;
    @NotNull
    private Integer score;
    @NotNull
    private BigDecimal startPrice;
    @NotNull
    private BigDecimal boxPrice;
    @NotNull
    private BigDecimal sendPrice;
    @NotBlank
    private String sendTime;
    @NotBlank
    private String topTitle;
    @NotBlank
    private String shopLoginName;
    @NotBlank
    private String shopLoginPassWord;
    @NotBlank
    private String shopAddress;
    @NotNull
    private BigDecimal rate;
    @NotBlank
    private String lat;
    @NotBlank
    private String lng;
    @NotNull
    private Integer vipDiscountFlag;
    @NotNull
    private Integer sendPriceAddByCountFlag;
    @NotNull
    private BigDecimal sendPriceAdd;
    @NotNull
    private BigDecimal fullMinusRate;
    @NotNull
    private BigDecimal couponRate;
    @NotNull
    private BigDecimal discountRate;

    private Integer shopTxFlag;

    private BigDecimal txAmount;

    private BigDecimal allTxAmount;

    /**
     * 店铺二维码图片
     */
    private String shopCodeImage;

    private Integer isDelete;

    /**排序字段*/
    private BigDecimal sort;

    private Integer printType;

    private Integer yesOnly;



    @TableField(exist = false)
    private BigDecimal minDiscount;
    @TableField(exist = false)
    private List<FullCut> fullCut;

    @TableField(exist = false)
    private Integer page;

    @TableField(exist = false)
    private Integer size;

    @TableField(exist = false)
    private String orderBy;

    @TableField(exist = false)
    private Integer total;

    @TableField(exist = false)
    private String query;

    @TableField(exist = false)
    private String queryType;

    @TableField(exist = false)
    private Integer shopWeight;

    private String openDescription;


    public Integer getYesOnly() {
        return yesOnly;
    }

    public void setYesOnly(Integer yesOnly) {
        this.yesOnly = yesOnly;
    }

    @Transient
    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    @Transient
    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    @Transient
    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    @Transient
    public Integer getPage() {
        if (page == null)
            return null;
        else
            return (page - 1) * this.size;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    @Transient
    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    @Transient
    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
    public BigDecimal getTxAmount() {
        return txAmount;
    }

    public void setTxAmount(BigDecimal txAmount) {
        this.txAmount = txAmount;
    }

    public BigDecimal getAllTxAmount() {
        return allTxAmount;
    }

    public void setAllTxAmount(BigDecimal allTxAmount) {
        this.allTxAmount = allTxAmount;
    }

    public String getShopCodeImage() {
        return shopCodeImage;
    }

    public void setShopCodeImage(String shopCodeImage) {
        this.shopCodeImage = shopCodeImage;
    }

    public Integer getshopTxFlag() {
        return shopTxFlag;
    }

    public void setshopTxFlag(Integer shopTxFlag) {
        this.shopTxFlag = shopTxFlag;
    }

    public Integer getTsModelFlag() {
        return tsModelFlag;
    }

    public void setTsModelFlag(Integer tsModelFlag) {
        this.tsModelFlag = tsModelFlag;
    }

    public Shop(Integer schoolId) {
        super();
        this.schoolId = schoolId;
    }

    public Shop() {
        super();
    }


    public BigDecimal getFullMinusRate() {
        return fullMinusRate;
    }

    public void setFullMinusRate(BigDecimal fullMinusRate) {
        this.fullMinusRate = fullMinusRate;
    }

    public BigDecimal getCouponRate() {
        return couponRate;
    }

    public void setCouponRate(BigDecimal couponRate) {
        this.couponRate = couponRate;
    }

    public BigDecimal getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
    }

    public BigDecimal getMinDiscount() {
        return minDiscount;
    }

    public void setMinDiscount(BigDecimal minDiscount) {
        this.minDiscount = minDiscount;
    }

    public List<FullCut> getFullCut() {
        return fullCut;
    }

    public void setFullCut(List<FullCut> fullCut) {
        this.fullCut = fullCut;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Integer schoolId) {
        this.schoolId = schoolId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName == null ? null : shopName.trim();
    }

    public String getShopPhone() {
        return shopPhone;
    }

    public void setShopPhone(String shopPhone) {
        this.shopPhone = shopPhone == null ? null : shopPhone.trim();
    }

    public String getShopImage() {
        return shopImage;
    }

    public void setShopImage(String shopImage) {
        this.shopImage = shopImage == null ? null : shopImage.trim();
    }

    public Integer getShopCategoryId() {
        return shopCategoryId;
    }

    public void setShopCategoryId(Integer shopCategoryId) {
        this.shopCategoryId = shopCategoryId;
    }

    public Integer getOpenFlag() {
        return openFlag;
    }

    public void setOpenFlag(Integer openFlag) {
        this.openFlag = openFlag;
    }

    public Integer getSendModelFlag() {
        return sendModelFlag;
    }

    public void setSendModelFlag(Integer sendModelFlag) {
        this.sendModelFlag = sendModelFlag;
    }

    public Integer getGetModelFlag() {
        return getModelFlag;
    }

    public void setGetModelFlag(Integer getModelFlag) {
        this.getModelFlag = getModelFlag;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public BigDecimal getStartPrice() {
        return startPrice;
    }

    public void setStartPrice(BigDecimal startPrice) {
        this.startPrice = startPrice;
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

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime == null ? null : sendTime.trim();
    }

    public String getTopTitle() {
        return topTitle;
    }

    public void setTopTitle(String topTitle) {
        this.topTitle = topTitle == null ? null : topTitle.trim();
    }

    public String getShopLoginName() {
        return shopLoginName;
    }

    public void setShopLoginName(String shopLoginName) {
        this.shopLoginName = shopLoginName == null ? null : shopLoginName.trim();
    }

    public String getShopLoginPassWord() {
        return shopLoginPassWord;
    }

    public void setShopLoginPassWord(String shopLoginPassWord) {
        this.shopLoginPassWord = shopLoginPassWord == null ? null : shopLoginPassWord.trim();
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress == null ? null : shopAddress.trim();
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat == null ? null : lat.trim();
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng == null ? null : lng.trim();
    }

    public Integer getVipDiscountFlag() {
        return vipDiscountFlag;
    }

    public void setVipDiscountFlag(Integer vipDiscountFlag) {
        this.vipDiscountFlag = vipDiscountFlag;
    }

    public Integer getSendPriceAddByCountFlag() {
        return sendPriceAddByCountFlag;
    }

    public void setSendPriceAddByCountFlag(Integer sendPriceAddByCountFlag) {
        this.sendPriceAddByCountFlag = sendPriceAddByCountFlag;
    }

    public BigDecimal getSendPriceAdd() {
        return sendPriceAdd;
    }

    public void setSendPriceAdd(BigDecimal sendPriceAdd) {
        this.sendPriceAdd = sendPriceAdd;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public BigDecimal getSort() {
        return sort;
    }

    public void setSort(BigDecimal sort) {
        this.sort = sort;
    }

    public Integer getPrintType() {
        return printType;
    }

    public void setPrintType(Integer printType) {
        this.printType = printType;
    }

    public Integer getShopWeight() {
        return shopWeight;
    }

    public void setShopWeight(Integer shopWeight) {
        this.shopWeight = shopWeight;
    }


    public String getOpenDescription() {
        return openDescription;
    }

    public void setOpenDescription(String openDescription) {
        this.openDescription = openDescription;
    }

    @Override
    public String toString() {
        return "Shop [id=" + id + ", schoolId=" + schoolId + ", shopName=" + shopName + ","
                + " shopPhone=" + shopPhone + " shopCategoryId=" + this.shopCategoryId + "]";
    }



}