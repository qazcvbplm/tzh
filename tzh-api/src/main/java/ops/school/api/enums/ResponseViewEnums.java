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
    FLOOR_SELECT_NULL("F11001","楼栋数据变更，更新后操作"),

    /**优惠券错误*/
    COUPON_HOME_NUM_ERROR("C11001","优惠券信息变更，更新后操作"),
    COUPON_USER_GET_ERROR("C11002","领取优惠券失败，稍后再来！"),
    COUPON_USER_GET_SUCCESS("C11003","领取优惠券成功，尽情使用吧！"),
    COUPON_USER_GET_NEED_SHOP("C11004","店铺优惠券和首页优惠券需要先绑定店铺"),

    /**微信用户错误*/
    WX_USER_NO_EXIST("WX11001","微信用户不存在，更新后操作"),

    /**订单错误*/
    ORDER_DONT_HAVE_PRODUCT("O11001","订单中不存在商品，请点单后下单"),
    ORDER_SAVE_ERROR("O11001","订单下单失败，请稍后重新下单"),

    /**学校信息错误*/
    SCHOOL_HAD_CHANGE("SC11001","学校信息发生变化，请联系后台管理"),
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
