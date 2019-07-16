package ops.school.api.dto.redis;

import java.math.BigDecimal;

public class SenderAddMoneyDTO extends RedisMessage {

    private String openId;

    private BigDecimal amount;

    public SenderAddMoneyDTO(String openId, BigDecimal amount) {
        super("addmoney");
        this.openId = openId;
        this.amount = amount;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
