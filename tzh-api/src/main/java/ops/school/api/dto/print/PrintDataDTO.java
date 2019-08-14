package ops.school.api.dto.print;

import ops.school.api.entity.Orders;

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

    private String platePrintOrderId;

    private String platePrintSn;

    private String platePrintKey;

    private Integer yesPrintTrue;

    private Integer printBrand;

    private Integer waterNumber;

    private Orders realOrder;

    private Integer cycleRedisCount = 0;


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

    @Override
    public String toString() {
        return "PrintDataDTO{" +
                "ourOrderId='" + ourOrderId + '\'' +
                ", ourShopId=" + ourShopId +
                ", platePrintOrderId='" + platePrintOrderId + '\'' +
                ", platePrintSn='" + platePrintSn + '\'' +
                ", platePrintKey='" + platePrintKey + '\'' +
                ", yesPrintTrue=" + yesPrintTrue +
                ", printBrand=" + printBrand +
                ", waterNumber=" + waterNumber +
                ", realOrder=" + realOrder +
                ", cycleRedisCount=" + cycleRedisCount +
                '}';
    }
}
