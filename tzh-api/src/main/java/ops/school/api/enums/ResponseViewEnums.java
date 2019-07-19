package ops.school.api.enums;

/**
 * @author CreateByFang on Time:2019/7/15 16:45
 * description:
 */
public enum ResponseViewEnums implements RootEnums{
    /**操作成功*/
    SUCCESS("A11001","操作成功"),
    /**操作失败*/
    FAILED("A11002","操作失败"),
    /**楼栋错误*/
    FLOOR_SELECT_NULL("F11001","楼栋为空，更新后操作"),

    /**优惠券错误*/
    COUPON_HOME_NUM_ERROR("C11001","优惠券信息变更，更新后操作"),
    COUPON_USER_GET_ERROR("C11002","领取优惠券失败，稍后再来！"),
    COUPON_USER_GET_SUCCESS("C11003","领取优惠券成功，尽情使用吧！"),
    COUPON_USER_GET_NEED_SHOP("C11004","店铺优惠券和首页优惠券需要先绑定店铺"),

    /**微信用户错误*/
    WX_USER_NO_EXIST("WX11001","微信用户不存在，更新后操作"),
    ;

    private String errorCode;
    private String errorMessage;

    ResponseViewEnums(String errorCode, String errorMessage) {
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
