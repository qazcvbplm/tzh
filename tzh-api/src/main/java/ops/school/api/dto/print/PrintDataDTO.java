package ops.school.api.dto.print;

import io.swagger.models.auth.In;
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

    private String plateOrderId;

    private String platePrintSn;

    private String platePrintKey;

    private Integer yesPrintTrue;

    private Integer printBrand;

    private Orders realOrder;


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

    public String getPlateOrderId() {
        return plateOrderId;
    }

    public void setPlateOrderId(String plateOrderId) {
        this.plateOrderId = plateOrderId;
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
}
