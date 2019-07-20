package ops.school.api.enums;

/**
 * @author CreateByFang on Time:2019/7/15 16:45
 * description: 错误码errorCode定义规则，7位，
 * 第一位默认值1
 * 第二位区别错误码类型，0-公共publicError 2-返回响应ResponseError
 * 第三位区别错误码类型下面的业务类型
 * 2-ResponseError中example：1-常用公共错误 2-楼栋错误 3-优惠券错误 4-微信用户 5-订单 6-商品 7-学校 8-店铺
 */
public enum PublicErrorEnums implements RootEnums {
    /**操作成功*/
    SUCCESS("1010001","操作成功"),
    /**操作失败*/
    FAILED("1010002","操作失败"),
    /**参数为空*/
    PULBIC_EMPTY_PARAM("1010003","参数为空"),
    /**参数错误*/
    PULBIC_ERROR_PARAM("1010004","参数错误"),
    PUBLIC_DATA_ERROR("1010005","数据为空"),
    PUBLIC_DATA_CHANGE("1010006","数据发生变更，请刷新"),
    /**参数错误*/
    PUBLIC_DO_FAILED("1010007","操作失败"),
    /**登陆超时*/
    LOGIN_TIME_OUT("1010008","登陆超时，请重新登陆"),
    /**登陆超时*/
    CALL_THE_BACK_WORKER("1010009","服务器被外星人攻击了，请联系管理员"),



    ;

    private String errorCode;
    private String errorMessage;

    private PublicErrorEnums(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @Override
    public String getErrorCode() {

        return errorCode;
    }

    @Override
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
