package ops.school.api.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * CreatebyFang
 * fangfor@outlook.com
 * 2019/4/22
 * 22:32
 * #
 */
public class LimitTableData<T> implements Serializable {

    public LimitTableData(){
        this.success = true;
        this.recordsTotal = 0;
        this.errorMsg = "";
        this.data = new ArrayList<>();
    }

    private Boolean success;

    /**
     * 拖拽的地方
     */
    private Integer draw;

    /**
     * 查询总条数
     */
    private Integer recordsTotal;

    /**
     * 过滤的条数
     */
    private Integer recordsFiltered;

    private String errorMsg;

    private List<T> data;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getDraw() {
        return draw;
    }

    public void setDraw(Integer draw) {
        this.draw = draw;
    }

    public Integer getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(Integer recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public Integer getRecordsFiltered() {
        return recordsFiltered;
    }

    public void setRecordsFiltered(Integer recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
