package ops.school.api.dto.card;

import java.math.BigDecimal;
import java.util.Date;

import ops.school.api.dto.BaseDTOCompute;
import ops.school.api.entity.card.CardBuyLog;
import ops.school.api.vo.card.CardBuyLogVO;
import java.io.Serializable;


public class CardBuyLogDTO extends BaseDTOCompute implements Serializable {

    private static final long serialVersionUID = 1L;
    /**主键id*/
    private Long id;
    /**学校id*/
    private Long schoolId;
    /**用户id*/
    private Long userId;
    /**卡id*/
    private Long cardId;
    /**购买金额*/
    private BigDecimal money;
    /**微信充值流水号*/
    private String wxTradeNo;
    /**创建人id*/
    private Long createId;
    /**修改人id*/
    private Long updateId;
    /**创建时间*/
    private Date createTime;
    /**修改时间*/
    private Date updateTime;

    private String openId;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSchoolId() {
        return this.schoolId;
    }

    public void setSchoolId(Long schoolId) {
        this.schoolId = schoolId;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCardId() {
        return this.cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    public BigDecimal getMoney() {
        return this.money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getWxTradeNo() {
        return this.wxTradeNo;
    }

    public void setWxTradeNo(String wxTradeNo) {
        this.wxTradeNo = wxTradeNo;
    }

    public Long getCreateId() {
        return this.createId;
    }

    public void setCreateId(Long createId) {
        this.createId = createId;
    }

    public Long getUpdateId() {
        return this.updateId;
    }

    public void setUpdateId(Long updateId) {
        this.updateId = updateId;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public CardBuyLogVO toVO() {
        CardBuyLogVO vo = new CardBuyLogVO();
        vo.setId(this.id);
        vo.setSchoolId(this.schoolId);
        vo.setUserId(this.userId);
        vo.setCardId(this.cardId);
        vo.setMoney(this.money);
        vo.setWxTradeNo(this.wxTradeNo);
        vo.setCreateId(this.createId);
        vo.setUpdateId(this.updateId);
        vo.setCreateTime(this.createTime);
        vo.setUpdateTime(this.updateTime);
        return vo;
    }
    public CardBuyLog toModel() {
        CardBuyLog model = new CardBuyLog();
        model.setId(this.id);
        model.setSchoolId(this.schoolId);
        model.setUserId(this.userId);
        model.setCardId(this.cardId);
        model.setMoney(this.money);
        model.setWxTradeNo(this.wxTradeNo);
        model.setCreateId(this.createId);
        model.setUpdateId(this.updateId);
        model.setCreateTime(this.createTime);
        model.setUpdateTime(this.updateTime);
        return model;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("CardBuyLogDTO{");
        sb.append("id=").append(id);
        sb.append(", schoolId=").append(schoolId);
        sb.append(", userId=").append(userId);
        sb.append(", cardId=").append(cardId);
        sb.append(", money=").append(money);
        sb.append(", wxTradeNo='").append(wxTradeNo).append('\'');
        sb.append(", createId=").append(createId);
        sb.append(", updateId=").append(updateId);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", openId='").append(openId).append('\'');
        sb.append('}');
        return sb.toString();
    }
}