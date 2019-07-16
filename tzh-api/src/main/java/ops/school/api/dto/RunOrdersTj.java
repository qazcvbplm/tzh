package ops.school.api.dto;

import java.math.BigDecimal;

public class RunOrdersTj {

    private Integer schoolId;

    private BigDecimal total;

    private String payType;

    private BigDecimal senderGet;

    private Integer counts;

    public Integer getCounts() {
        return counts;
    }

    public void setCounts(Integer counts) {
        this.counts = counts;
    }

    public BigDecimal getSenderGet() {
        return senderGet;
    }

    public void setSenderGet(BigDecimal senderGet) {
        this.senderGet = senderGet;
    }

    public Integer getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Integer schoolId) {
        this.schoolId = schoolId;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }


    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }
}
