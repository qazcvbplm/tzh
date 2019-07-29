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
    private String msg;
    private List<String> params = new ArrayList<String>(0);

    public BaseException(){}

    public BaseException(Integer code) {
        this.code = code;
    }

    public BaseException(String msg){
        this.msg = msg;
    }

    public BaseException(Integer code,String msg){
        this.code = code;
        this.msg = msg;
    }

    public BaseException(Integer code, String msg, List<String> params) {
        this.code = code;
        this.msg = msg;
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
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public List<String> getParams() {
        return params;
    }

    public void setParams(List<String> params) {
        this.params = params;
    }
}
