package ops.school.api.dto.print;

import java.util.List;

/**
 * CreatebyFang
 * fangfor@outlook.com
 * 2019/8/12
 * 16:51
 * #
 */
public class ShopPrintResultDTO<T> {

    private String msg;

    private Integer ret;

    private T data;

    private Long serverExecutedTime;

    /**
     * 自定义
     */
    private String errorMessage;

    /**
     * 自定义
     */
    private boolean success;


    public ShopPrintResultDTO() {
    }

    public ShopPrintResultDTO(boolean success , String errorMessage) {
        this.errorMessage = errorMessage;
        this.success = success;
    }

    public ShopPrintResultDTO(String msg, Integer ret, T data, Long serverExecutedTime, String errorMessage, boolean success) {
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
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

