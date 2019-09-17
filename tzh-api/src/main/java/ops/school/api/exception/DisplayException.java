package ops.school.api.exception;

import ops.school.api.enums.PublicErrorEnums;
import ops.school.api.enums.RootEnums;

import java.util.List;

/**
 * @author: FANG
 * @date:   2019/7/15 16:43
 * @desc:   自定义业务类异常
 */
public class DisplayException extends BaseException {

    public DisplayException(){
        super();
    }

    public DisplayException(Integer code) {
        super(code);
    }

    public DisplayException(String message){
        super(message);
    }

    public DisplayException(Integer code, String message) {
        super(code, message);
    }

    public DisplayException(Integer code, String message, String... params) {
        throw new DisplayException(code,message,params);
    }

    public static void throwMessage(String msg){
        throw new DisplayException(msg);
    }

    public static void throwMessageWithEnum(RootEnums errorEnums){
        throw new DisplayException(errorEnums.getErrorMessage());
    }

    public static void throwMessageParamNull(){
        throw new DisplayException(PublicErrorEnums.PULBIC_EMPTY_PARAM.getErrorMessage());
    }
}
