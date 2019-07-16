package ops.school.api.dto;

import java.math.BigDecimal;

public class Test {

    private String openId;

    private BigDecimal money;


    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }
}
