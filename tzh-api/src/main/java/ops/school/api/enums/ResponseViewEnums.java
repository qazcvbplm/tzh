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
    /**操作失败*/
    DELETE_SUCCESS("1210004","删除成功"),
    /**操作成功*/
    FIND_SUCCESS("1210005","查询成功"),

    /**操作失败*/
    FIND_FAILED("1210006","查询失败"),


    /**----------2-楼栋错误-----------*/
    FLOOR_SELECT_NULL("1220001","楼栋数据变更，更新后操作"),

    /**----------3-优惠券错误-----------*/
    COUPON_HOME_NUM_ERROR("1230001","优惠券信息变更，更新后操作"),
    COUPON_USER_GET_ERROR("1230002","领取优惠券失败，稍后再来！"),
    COUPON_USER_GET_SUCCESS("1230003","领取优惠券成功，尽情使用吧！"),
    COUPON_USER_GET_NEED_SHOP("1230004","店铺优惠券和首页优惠券需要先绑定店铺"),
    COUPON_TYPE_CANT_UPDATE("1230005","优惠券类型无法修改"),
    COUPON_FIND_NEED_ID("1230006","优惠券查询需要优惠券id"),
    COUPON_TYPE_SHOP_NEED_SHOP_ID("1230007","优惠券查询需要优惠券id"),
    COUPON_CANT_USE_THIS_SHOP("1230008","这优惠卷不能用在当前店铺"),

    /**----------4-微信用户错误-----------*/
    WX_USER_NO_EXIST("1240001","微信用户不存在，更新后操作"),
    WX_USER_BELL_NEED_ID("1240001","微信用户，余额，粮票的操作，必须有微信用户id"),
    WX_USER_NEED_USER_ID("1240002","微信用户需要用户id参数"),
    WX_USER_FAILED_TO_WX("1240002","微信接口繁忙，请重试！"),

    /**----------5-订单错误-----------*/
    ORDER_DONT_HAVE_PRODUCT("1250001","订单中不存在商品，请点单后下单"),
    ORDER_SAVE_ERROR("1250002","订单下单失败，请稍后重新下单"),
    ORDER_PARAM_ERROR("1250003","订单信息错误，请稍后重新下单或者请联系后台管理"),
    ORDER_COMPLETE_SOURCE_ERROR("1250004","订单完结增加积分错误，请稍后重试或者请联系后台管理"),

    /**----------6-商品信息错误-----------*/
    PRODUCT_HAD_CHANGE("1260001","商品信息发生变化，请联系后台管理"),

    /**----------7-学校信息错误-----------*/
    SCHOOL_HAD_CHANGE("1270001","学校信息发生变化，请联系后台管理"),

    /**----------8-店铺信息错误-----------*/
    SHOP_HAD_CHANGE("1280001","店铺信息发生变化，请联系后台管理"),

    /**----------9-配送员信息错误-----------*/
    SENDER_HAD_CHANGE("1290001","配送员信息发生变化，请联系后台管理"),

    /**----------10-订单完成表信息错误-----------*/
    ORDERSCOMPLETE_HAD_ERROR("1210001","订单完成表信息错误，请联系后台管理"),

    /**----------11-跑腿订单完成表信息错误-----------*/
    RUN_ORDERS_COMPLETE_HAD_ERROR("1211001","跑腿订单完成失败，请联系后台管理"),
    RUN_ORDERS_WX_USER_ERROR("1211002","跑腿订单下单用户信息错误，请重试或者删除微信小程序重新进入"),

    /**----------12-提现信息错误-----------*/
    TX_ERROR_USER_BELL_FAILED("1212001","用户账户提现失败，请重试或者联系后台管理"),
    TX_ERROR_SCHOOL_BELL_FAILED("1212002","学校负责人账户失败，请重试或者联系后台管理"),
    TX_ERROR_BACK_FAILED("1212003","提现后台失败，请重试或者联系后台管理"),
    TX_ERROR_WX_CHARGE_FAILED("1212004","提现微信交易失败，请重试或者联系后台管理"),
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
