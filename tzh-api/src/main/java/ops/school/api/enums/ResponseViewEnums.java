package ops.school.api.enums;

/**
 * @author CreateByFang on Time:2019/7/15 16:45
 * description: 错误码errorCode定义规则，7位，
 * 第一位默认值1
 * 第二位区别错误码类型，0-公共publicError 2-返回响应ResponseError
 * 第三位区别错误码类型下面的业务类型，
 * 2-ResponseError中example：1-常用公共错误 2-楼栋错误 3-优惠券错误 4-微信用户 5-订单 6-商品 7-学校 8-店铺
 */
public enum ResponseViewEnums implements RootEnums{
    /**操作成功*/
    SUCCESS("1210001","操作成功"),
    /**操作失败*/
    FAILED("1210002","操作失败"),
    /**操作失败*/
    DELETE_FAILED("1210003","删除失败，请检查后操作"),


    /**----------2-楼栋错误-----------*/
    FLOOR_SELECT_NULL("1220001","楼栋数据变更，更新后操作"),

    /**----------3-优惠券错误-----------*/
    COUPON_HOME_NUM_ERROR("1230001","优惠券信息变更，更新后操作"),
    COUPON_USER_GET_ERROR("1230002","领取优惠券失败，稍后再来！"),
    COUPON_USER_GET_SUCCESS("1230003","领取优惠券成功，尽情使用吧！"),
    COUPON_USER_GET_NEED_SHOP("1230004","店铺优惠券和首页优惠券需要先绑定店铺"),

    /**----------4-微信用户错误-----------*/
    WX_USER_NO_EXIST("1240001","微信用户不存在，更新后操作"),
    WX_USER_BELL_NEED_ID("1240001","微信用户，余额，粮票的操作，必须有微信用户id"),
    WX_USER_NEED_USER_ID("1240002","微信用户需要用户id参数"),

    /**----------5-订单错误-----------*/
    ORDER_DONT_HAVE_PRODUCT("1250001","订单中不存在商品，请点单后下单"),
    ORDER_SAVE_ERROR("1250002","订单下单失败，请稍后重新下单"),
    ORDER_PARAM_ERROR("1250002","订单信息错误，请稍后重新下单或者请联系后台管理"),

    /**----------6-商品信息错误-----------*/
    PRODUCT_HAD_CHANGE("1260001","商品信息发生变化，请联系后台管理"),

    /**----------7-学校信息错误-----------*/
    SCHOOL_HAD_CHANGE("1270001","学校信息发生变化，请联系后台管理"),

    /**----------8-店铺信息错误-----------*/
    SHOP_HAD_CHANGE("1280001","店铺信息发生变化，请联系后台管理"),

    /**----------9-配送员信息错误-----------*/
    SENDER_HAD_CHANGE("1290001","配送员信息发生变化，请联系后台管理"),

    /**----------10-订单完成表信息错误-----------*/
    ORDERSCOMPLETE_HAD_ERROR("1300001","订单完成表信息错误，请联系后台管理"),
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
