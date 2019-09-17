package ops.school.api.dto.print;

import ops.school.api.entity.Orders;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * CreatebyFang
 * fangfor@outlook.com
 * 2019/8/12
 * 18:52
 * #
 */
public class PrintDataDTO {

    private String ourOrderId;

    private Integer ourShopId;

    private Integer ourSchoolId;

    private String platePrintOrderId;

    private String platePrintSn;

    private String platePrintKey;

    private Integer yesPrintTrue;

    private Integer printBrand;

    private Integer waterNumber;

    private Date creatTime;

    private Orders realOrder;

    private Integer cycleRedisCount = 0;

    private String[] sendMsg3Params = new String[3];

    private String schoolLeaderPhone;


    public String getOurOrderId() {
        return ourOrderId;
    }

    public void setOurOrderId(String ourOrderId) {
        this.ourOrderId = ourOrderId;
    }

    public Integer getOurShopId() {
        return ourShopId;
    }

    public void setOurShopId(Integer ourShopId) {
        this.ourShopId = ourShopId;
    }

    public String getPlatePrintOrderId() {
        return platePrintOrderId;
    }

    public void setPlatePrintOrderId(String platePrintOrderId) {
        this.platePrintOrderId = platePrintOrderId;
    }

    public String getPlatePrintSn() {
        return platePrintSn;
    }

    public void setPlatePrintSn(String platePrintSn) {
        this.platePrintSn = platePrintSn;
    }

    public String getPlatePrintKey() {
        return platePrintKey;
    }

    public void setPlatePrintKey(String platePrintKey) {
        this.platePrintKey = platePrintKey;
    }

    public Integer getYesPrintTrue() {
        return yesPrintTrue;
    }

    public void setYesPrintTrue(Integer yesPrintTrue) {
        this.yesPrintTrue = yesPrintTrue;
    }

    public Integer getPrintBrand() {
        return printBrand;
    }

    public void setPrintBrand(Integer printBrand) {
        this.printBrand = printBrand;
    }

    public Orders getRealOrder() {
        return realOrder;
    }

    public void setRealOrder(Orders realOrder) {
        this.realOrder = realOrder;
    }

    public Integer getWaterNumber() {
        return waterNumber;
    }

    public void setWaterNumber(Integer waterNumber) {
        this.waterNumber = waterNumber;
    }

    public Integer getCycleRedisCount() {
        return cycleRedisCount;
    }

    public void setCycleRedisCount(Integer cycleRedisCount) {
        this.cycleRedisCount = cycleRedisCount;
    }

    public String[] getSendMsg3Params() {
        return sendMsg3Params;
    }

    public void setSendMsg3Params(String[] sendMsg3Params) {
        this.sendMsg3Params = sendMsg3Params;
    }

    public String getSchoolLeaderPhone() {
        return schoolLeaderPhone;
    }

    public void setSchoolLeaderPhone(String schoolLeaderPhone) {
        this.schoolLeaderPhone = schoolLeaderPhone;
    }

    public Date getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(Date creatTime) {
        this.creatTime = creatTime;
    }

    public Integer getOurSchoolId() {
        return ourSchoolId;
    }

    public void setOurSchoolId(Integer ourSchoolId) {
        this.ourSchoolId = ourSchoolId;
    }

    @Override
    public String toString() {
        return "PrintDataDTO{" +
                "ourOrderId='" + ourOrderId + '\'' +
                ", ourShopId=" + ourShopId +
                ", ourSchoolId=" + ourSchoolId +
                ", platePrintOrderId='" + platePrintOrderId + '\'' +
                ", platePrintSn='" + platePrintSn + '\'' +
                ", platePrintKey='" + platePrintKey + '\'' +
                ", yesPrintTrue=" + yesPrintTrue +
                ", printBrand=" + printBrand +
                ", waterNumber=" + waterNumber +
                ", creatTime=" + creatTime +
                ", realOrder=" + realOrder +
                ", cycleRedisCount=" + cycleRedisCount +
                ", sendMsg3Params=" + Arrays.toString(sendMsg3Params) +
                ", schoolLeaderPhone='" + schoolLeaderPhone + '\'' +
                '}';
    }
}
