package ops.school.api.exception;

import ops.school.api.enums.PublicErrorEnums;
import ops.school.api.enums.RootEnums;

/**
 * @author: FANG
 * @date:   2019/7/15 16:43
 * @desc:   自定义业务类异常
 */
public class DisplayException extends RuntimeException {

    private Integer code;
    private String  message;

    public DisplayException(){
        super();
    }
    public DisplayException(String message){
        super(message);
    }

    public DisplayException(Integer code,String message){
        super(message);
        this.code = code;
    }

    public static void throwMessage(String msg){
        throw new RuntimeException(msg);
    }

    public static void throwMessageWithEnum(RootEnums errorEnums){
        throw new RuntimeException(errorEnums.getErrorMessage());
    }

    public static void throwMessageParamNull(){
        throw new RuntimeException(PublicErrorEnums.PULBIC_EMPTY_PARAM.getErrorMessage());
    }
}
