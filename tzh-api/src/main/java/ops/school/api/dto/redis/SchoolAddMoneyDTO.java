package ops.school.api.dto.redis;

import java.math.BigDecimal;

public class SchoolAddMoneyDTO extends RedisMessage {

    private Integer schoolId;

    private BigDecimal selfGet;

    private BigDecimal senderGet;

    public SchoolAddMoneyDTO(Integer schoolId, BigDecimal selfGet, BigDecimal senderGet) {
        super("addmoney");
        this.schoolId = schoolId;
        this.selfGet = selfGet;
        this.senderGet = senderGet;
    }

    public Integer getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Integer schoolId) {
        this.schoolId = schoolId;
    }

    public BigDecimal getSelfGet() {
        return selfGet;
    }

    public void setSelfGet(BigDecimal selfGet) {
        this.selfGet = selfGet;
    }

    public BigDecimal getSenderGet() {
        return senderGet;
    }

    public void setSenderGet(BigDecimal senderGet) {
        this.senderGet = senderGet;
    }
}
