package ops.school.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class School extends BaseDTOMP implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @NotBlank
    private String name;
    @NotBlank
    private String phone;

    private String messagePhone;
    @NotBlank
    private String loginName;
    @NotBlank
    private String loginPassWord;
    @DecimalMin(value = "0.006")
    private BigDecimal rate;
    @NotNull
    private Integer appId;

    private BigDecimal money;
    @NotNull
    private BigDecimal topDown;

    private Long sort;

    private BigDecimal senderMoney;
    @NotNull
    private Integer sendMaxDistance;
    @NotNull
    private Integer sendPerOut;
    @NotNull
    private BigDecimal sendPerMoney;
    @NotBlank
    private String wxAppId;
    @NotBlank
    private String wxMessageTemplateId;
    @NotBlank
    private String wxSecret;
    @NotBlank
    private String mchId;
    @NotBlank
    private String wxPayId;
    @NotBlank
    private String certPath;

    private Integer isDelete;

    private Date createTime;
    @NotNull
    private Integer enableTakeout;

    private Integer enableSender;

    private Integer enableCoupon;

    private BigDecimal senderAllTx;

    private BigDecimal userCharge;

    private BigDecimal userBellAll;

    private BigDecimal userChargeSend;

    private Integer pageLayout;

    private BigDecimal smallMinAmount;

    private BigDecimal middleMinAmount;

    private BigDecimal largeMinAmount;

    private BigDecimal extraLargeMinAmount;

    private Integer yesWaterOne;

    private Integer yesOpenCard;

    private String senderTencentGroup;

    public String getMessagePhone() {
        return messagePhone;
    }

    public void setMessagePhone(String messagePhone) {
        this.messagePhone = messagePhone;
    }

    public String getSenderTencentGroup() {
        return senderTencentGroup;
    }

    public void setSenderTencentGroup(String senderTencentGroup) {
        this.senderTencentGroup = senderTencentGroup;
    }

    public BigDecimal getSmallMinAmount() {
        return smallMinAmount;
    }

    public void setSmallMinAmount(BigDecimal smallMinAmount) {
        this.smallMinAmount = smallMinAmount;
    }

    public BigDecimal getMiddleMinAmount() {
        return middleMinAmount;
    }

    public void setMiddleMinAmount(BigDecimal middleMinAmount) {
        this.middleMinAmount = middleMinAmount;
    }

    public BigDecimal getLargeMinAmount() {
        return largeMinAmount;
    }

    public void setLargeMinAmount(BigDecimal largeMinAmount) {
        this.largeMinAmount = largeMinAmount;
    }

    public BigDecimal getExtraLargeMinAmount() {
        return extraLargeMinAmount;
    }

    public void setExtraLargeMinAmount(BigDecimal extraLargeMinAmount) {
        this.extraLargeMinAmount = extraLargeMinAmount;
    }

    public Integer getPageLayout() {
        return pageLayout;
    }

    public void setPageLayout(Integer pageLayout) {
        this.pageLayout = pageLayout;
    }

    public BigDecimal getUserChargeSend() {
        return userChargeSend;
    }

    public void setUserChargeSend(BigDecimal userChargeSend) {
        this.userChargeSend = userChargeSend;
    }

    public BigDecimal getSenderAllTx() {
        return senderAllTx;
    }

    public void setSenderAllTx(BigDecimal senderAllTx) {
        this.senderAllTx = senderAllTx;
    }

    public BigDecimal getUserCharge() {
        return userCharge;
    }

    public void setUserCharge(BigDecimal userCharge) {
        this.userCharge = userCharge;
    }

    public BigDecimal getUserBellAll() {
        return userBellAll;
    }

    public void setUserBellAll(BigDecimal userBellAll) {
        this.userBellAll = userBellAll;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getEnableTakeout() {
        return enableTakeout;
    }

    public void setEnableTakeout(Integer enableTakeout) {
        this.enableTakeout = enableTakeout;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public BigDecimal getTopDown() {
        return topDown;
    }

    public void setTopDown(BigDecimal topDown) {
        this.topDown = topDown;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getCertPath() {
        return certPath;
    }

    public void setCertPath(String certPath) {
        this.certPath = certPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName == null ? null : loginName.trim();
    }

    public String getLoginPassWord() {
        return loginPassWord;
    }

    public void setLoginPassWord(String loginPassWord) {
        this.loginPassWord = loginPassWord == null ? null : loginPassWord.trim();
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public Long getSort() {
        return sort;
    }

    public void setSort(Long sort) {
        this.sort = sort;
    }

    public BigDecimal getSenderMoney() {
        return senderMoney;
    }

    public void setSenderMoney(BigDecimal senderMoney) {
        this.senderMoney = senderMoney;
    }

    public Integer getSendMaxDistance() {
        return sendMaxDistance;
    }

    public void setSendMaxDistance(Integer sendMaxDistance) {
        this.sendMaxDistance = sendMaxDistance;
    }

    public Integer getSendPerOut() {
        return sendPerOut;
    }

    public void setSendPerOut(Integer sendPerOut) {
        this.sendPerOut = sendPerOut;
    }

    public BigDecimal getSendPerMoney() {
        return sendPerMoney;
    }

    public void setSendPerMoney(BigDecimal sendPerMoney) {
        this.sendPerMoney = sendPerMoney;
    }

    public String getWxAppId() {
        return wxAppId;
    }

    public void setWxAppId(String wxAppId) {
        this.wxAppId = wxAppId == null ? null : wxAppId.trim();
    }

    public String getWxSecret() {
        return wxSecret;
    }

    public void setWxSecret(String wxSecret) {
        this.wxSecret = wxSecret == null ? null : wxSecret.trim();
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId == null ? null : mchId.trim();
    }

    public String getWxPayId() {
        return wxPayId;
    }

    public void setWxPayId(String wxPayId) {
        this.wxPayId = wxPayId == null ? null : wxPayId.trim();
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getEnableCoupon() {
        return enableCoupon;
    }

    public void setEnableCoupon(Integer enableCoupon) {
        this.enableCoupon = enableCoupon;
    }

    public String getWxMessageTemplateId() {
        return wxMessageTemplateId;
    }

    public void setWxMessageTemplateId(String wxMessageTemplateId) {

        this.wxMessageTemplateId = wxMessageTemplateId;
    }

    public Integer getYesWaterOne() {
        return yesWaterOne;
    }

    public void setYesWaterOne(Integer yesWaterOne) {
        this.yesWaterOne = yesWaterOne;
    }

    public Integer getEnableSender() {
        return enableSender;
    }

    public void setEnableSender(Integer enableSender) {
        this.enableSender = enableSender;
    }
    public Integer getYesOpenCard() {
        return yesOpenCard;
    }

    public void setYesOpenCard(Integer yesOpenCard) {
        this.yesOpenCard = yesOpenCard;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("School{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", phone='").append(phone).append('\'');
        sb.append(", messagePhone='").append(messagePhone).append('\'');
        sb.append(", loginName='").append(loginName).append('\'');
        sb.append(", loginPassWord='").append(loginPassWord).append('\'');
        sb.append(", rate=").append(rate);
        sb.append(", appId=").append(appId);
        sb.append(", money=").append(money);
        sb.append(", topDown=").append(topDown);
        sb.append(", sort=").append(sort);
        sb.append(", senderMoney=").append(senderMoney);
        sb.append(", sendMaxDistance=").append(sendMaxDistance);
        sb.append(", sendPerOut=").append(sendPerOut);
        sb.append(", sendPerMoney=").append(sendPerMoney);
        sb.append(", wxAppId='").append(wxAppId).append('\'');
        sb.append(", wxMessageTemplateId='").append(wxMessageTemplateId).append('\'');
        sb.append(", wxSecret='").append(wxSecret).append('\'');
        sb.append(", mchId='").append(mchId).append('\'');
        sb.append(", wxPayId='").append(wxPayId).append('\'');
        sb.append(", certPath='").append(certPath).append('\'');
        sb.append(", isDelete=").append(isDelete);
        sb.append(", createTime=").append(createTime);
        sb.append(", enableTakeout=").append(enableTakeout);
        sb.append(", enableCoupon=").append(enableCoupon);
        sb.append(", senderAllTx=").append(senderAllTx);
        sb.append(", userCharge=").append(userCharge);
        sb.append(", userBellAll=").append(userBellAll);
        sb.append(", userChargeSend=").append(userChargeSend);
        sb.append(", pageLayout=").append(pageLayout);
        sb.append(", smallMinAmount=").append(smallMinAmount);
        sb.append(", middleMinAmount=").append(middleMinAmount);
        sb.append(", largeMinAmount=").append(largeMinAmount);
        sb.append(", extraLargeMinAmount=").append(extraLargeMinAmount);
        sb.append(", yesWaterOne=").append(yesWaterOne);
        sb.append(", yesOpenCard=").append(yesOpenCard);
        sb.append(", senderTencentGroup='").append(senderTencentGroup).append('\'');
        sb.append('}');
        return sb.toString();
    }
}