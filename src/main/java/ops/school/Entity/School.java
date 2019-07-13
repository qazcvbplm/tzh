//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package ops.school.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class School extends Base implements Serializable {
    @TableId(
            type = IdType.AUTO
    )
    private Integer id;
    @NotBlank
    private String name;
    @NotBlank
    private String phone;
    @NotBlank
    private String loginName;
    @NotBlank
    private String loginPassWord;
    @DecimalMin("0.006")
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
    private BigDecimal senderAllTx;
    private BigDecimal userCharge;
    private BigDecimal userBellAll;
    private BigDecimal userChargeSend;
    private Integer pageLayout;
    private BigDecimal smallMinAmount;
    private BigDecimal middleMinAmount;
    private BigDecimal largeMinAmount;
    private BigDecimal extraLargeMinAmount;

    public School() {
    }

    public BigDecimal getSmallMinAmount() {
        return this.smallMinAmount;
    }

    public void setSmallMinAmount(BigDecimal smallMinAmount) {
        this.smallMinAmount = smallMinAmount;
    }

    public BigDecimal getMiddleMinAmount() {
        return this.middleMinAmount;
    }

    public void setMiddleMinAmount(BigDecimal middleMinAmount) {
        this.middleMinAmount = middleMinAmount;
    }

    public BigDecimal getLargeMinAmount() {
        return this.largeMinAmount;
    }

    public void setLargeMinAmount(BigDecimal largeMinAmount) {
        this.largeMinAmount = largeMinAmount;
    }

    public BigDecimal getExtraLargeMinAmount() {
        return this.extraLargeMinAmount;
    }

    public void setExtraLargeMinAmount(BigDecimal extraLargeMinAmount) {
        this.extraLargeMinAmount = extraLargeMinAmount;
    }

    public Integer getPageLayout() {
        return this.pageLayout;
    }

    public void setPageLayout(Integer pageLayout) {
        this.pageLayout = pageLayout;
    }

    public BigDecimal getUserChargeSend() {
        return this.userChargeSend;
    }

    public void setUserChargeSend(BigDecimal userChargeSend) {
        this.userChargeSend = userChargeSend;
    }

    public BigDecimal getSenderAllTx() {
        return this.senderAllTx;
    }

    public void setSenderAllTx(BigDecimal senderAllTx) {
        this.senderAllTx = senderAllTx;
    }

    public BigDecimal getUserCharge() {
        return this.userCharge;
    }

    public void setUserCharge(BigDecimal userCharge) {
        this.userCharge = userCharge;
    }

    public BigDecimal getUserBellAll() {
        return this.userBellAll;
    }

    public void setUserBellAll(BigDecimal userBellAll) {
        this.userBellAll = userBellAll;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getEnableTakeout() {
        return this.enableTakeout;
    }

    public void setEnableTakeout(Integer enableTakeout) {
        this.enableTakeout = enableTakeout;
    }

    public Integer getAppId() {
        return this.appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public BigDecimal getTopDown() {
        return this.topDown;
    }

    public void setTopDown(BigDecimal topDown) {
        this.topDown = topDown;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCertPath() {
        return this.certPath;
    }

    public void setCertPath(String certPath) {
        this.certPath = certPath;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getLoginName() {
        return this.loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName == null ? null : loginName.trim();
    }

    public String getLoginPassWord() {
        return this.loginPassWord;
    }

    public void setLoginPassWord(String loginPassWord) {
        this.loginPassWord = loginPassWord == null ? null : loginPassWord.trim();
    }

    public BigDecimal getRate() {
        return this.rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getMoney() {
        return this.money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public Long getSort() {
        return this.sort;
    }

    public void setSort(Long sort) {
        this.sort = sort;
    }

    public BigDecimal getSenderMoney() {
        return this.senderMoney;
    }

    public void setSenderMoney(BigDecimal senderMoney) {
        this.senderMoney = senderMoney;
    }

    public Integer getSendMaxDistance() {
        return this.sendMaxDistance;
    }

    public void setSendMaxDistance(Integer sendMaxDistance) {
        this.sendMaxDistance = sendMaxDistance;
    }

    public Integer getSendPerOut() {
        return this.sendPerOut;
    }

    public void setSendPerOut(Integer sendPerOut) {
        this.sendPerOut = sendPerOut;
    }

    public BigDecimal getSendPerMoney() {
        return this.sendPerMoney;
    }

    public void setSendPerMoney(BigDecimal sendPerMoney) {
        this.sendPerMoney = sendPerMoney;
    }

    public String getWxAppId() {
        return this.wxAppId;
    }

    public void setWxAppId(String wxAppId) {
        this.wxAppId = wxAppId == null ? null : wxAppId.trim();
    }

    public String getWxSecret() {
        return this.wxSecret;
    }

    public void setWxSecret(String wxSecret) {
        this.wxSecret = wxSecret == null ? null : wxSecret.trim();
    }

    public String getMchId() {
        return this.mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId == null ? null : mchId.trim();
    }

    public String getWxPayId() {
        return this.wxPayId;
    }

    public void setWxPayId(String wxPayId) {
        this.wxPayId = wxPayId == null ? null : wxPayId.trim();
    }

    public Integer getIsDelete() {
        return this.isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
