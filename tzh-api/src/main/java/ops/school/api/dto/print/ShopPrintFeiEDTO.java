package ops.school.api.dto.print;

import java.util.List;

/**
 * CreatebyFang
 * fangfor@outlook.com
 * 2019/8/12
 * 16:51
 * #
 */
public class ShopPrintFeiEDTO {

    private String msg;

    private Integer ret;

    private FeiERsultData data;

    private Long serverExecutedTime;

    /**
     * 自定义
     */
    private String errorMessage;

    /**
     * 自定义
     */
    private boolean success;

    public ShopPrintFeiEDTO() {
    }

    public ShopPrintFeiEDTO(boolean success , String errorMessage) {
        this.errorMessage = errorMessage;
        this.success = success;
    }

    public ShopPrintFeiEDTO(String msg, Integer ret, FeiERsultData data, Long serverExecutedTime, String errorMessage, boolean success) {
        this.msg = msg;
        this.ret = ret;
        this.data = data;
        this.serverExecutedTime = serverExecutedTime;
        this.errorMessage = errorMessage;
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getRet() {
        return ret;
    }

    public void setRet(Integer ret) {
        this.ret = ret;
    }

    public FeiERsultData getData() {
        return data;
    }

    public void setData(FeiERsultData data) {
        this.data = data;
    }

    public Long getServerExecutedTime() {
        return serverExecutedTime;
    }

    public void setServerExecutedTime(Long serverExecutedTime) {
        this.serverExecutedTime = serverExecutedTime;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}

