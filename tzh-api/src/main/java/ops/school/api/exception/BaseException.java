package ops.school.api.exception;

import java.util.ArrayList;
import java.util.List;

/**
 * CreatebyFang
 * fangfor@outlook.com
 * 2019/7/24
 * 12:52
 * #
 */
public class BaseException extends RuntimeException {

    private Integer code;
    private String message;
    private List<String> params = new ArrayList<String>(0);

    public BaseException(){}

    public BaseException(Integer code) {
        this.code = code;
    }

    public BaseException(String message){
        this.message = message;
    }

    public BaseException(Integer code,String message){
        this.code = code;
        this.message = message;
    }

    public BaseException(Integer code, String message, List<String> params) {
        this.code = code;
        this.message = message;
        this.params = params;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setmessage(String message) {
        this.message = message;
    }


    public List<String> getParams() {
        return params;
    }

    public void setParams(List<String> params) {
        this.params = params;
    }
}
