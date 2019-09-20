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
    SUCCESS_NUM("1210002","操作成功,操作的条数"),
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
    /**创建人不能为空*/
    CREATE_ID_CANT_NULL("1210007","创建人不能为空"),
    UPDATE_ID_CAN_NOT_NULL("1210008","更新对象不能为空"),


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
    COUPON_CANT_USE_THIS_SCHOOL("1230009","这优惠卷不能用在当前学校"),
    COUPON_CANT_USE_OR_PAST("1230010","优惠卷不可用，过期或者失效"),
    COUPON_CANT_USE("1230011","优惠卷失效无法使用"),

    /**----------4-微信用户错误-----------*/
    WX_USER_NO_EXIST("1240001","微信用户不存在，更新后操作"),
    WX_USER_BELL_NEED_ID("1240002","微信用户，余额，粮票的操作，必须有微信用户id"),
    WX_USER_NEED_USER_ID("1240003","用户操作需要用户id参数"),
    WX_USER_FAILED_TO_WX("1240004","微信接口繁忙，请重试！"),
    WX_TUI_KUAN_ERROR("1240005","微信退款失败，请稍后重试或者请联系后台管理！"),
    WX_USER_NO_EXIST2("1240006","微信用户不存在，更新后操作或者退出小程序并删除后重进"),

    /**----------5-订单错误-----------*/
    ORDER_DONT_HAVE_PRODUCT("1250001","订单中不存在商品，请点单后下单"),
    ORDER_SAVE_ERROR("1250002","订单下单失败，请稍后重新下单"),
    ORDER_PARAM_ERROR("1250003","订单信息错误，请稍后重新下单或者请联系后台管理"),
    ORDER_COMPLETE_SOURCE_ERROR("1250004","订单完结增加积分错误，请稍后重试或者请联系后台管理"),
    ORDER_PRINT_NO_PRINTER("1250005","打印订单时店铺没有关联打印机"),
    ORDER_PRINT_ACCEPT_ERROR("1250006","打印订单接手失败"),
    ORDER_MESSAGE_NULL_ORDER_ID("1250007","发送微信模板消息订单id为空"),
    ORDER_MESSAGE_NULL_SCHOOL("1250008","发送微信模板消息学校信息空需要appid"),
    ORDER_MESSAGE_NULL_SCHOOL_ID("1250009","发送微信模板消息学校信息空需要学校id"),
    ORDER_PARAM_NULL("1250010","订单信息错误，请稍后重新下单"),
    ORDER_PARAM_TIME_BEFORE("1250011","预定订单时间太早，请稍后重新下单"),
    ORDER_USER_BELL_NULL("1250012","用户数据有误，请前往“我的”界面重新授权绑定手机号"),
    ORDER_ADD_ORDER_NO_SHOP("1250013","亲！支付成功，下单失败，店铺有误，请联系学校负责人或者椰子后台客服服务"),
    ORDER_NEED_USER_CARD_ID("1250014","订单缺失信息，需要配送卡信息"),

    /**----------6-商品信息错误-----------*/
    PRODUCT_HAD_CHANGE("1260001","商品信息发生变化，请联系后台管理"),

    /**----------7-学校信息错误-----------*/
    SCHOOL_HAD_CHANGE("1270001","学校信息发生变化，请刷新或者联系后台管理"),
    SCHOOL_CANT_BE_NULL("1270002","需要选择学校"),

    /**----------8-店铺信息错误-----------*/
    SHOP_HAD_CHANGE("1280001","店铺信息发生变化，请联系后台管理"),
    SHOP_ONE_HAVE_ONE_TYPE_PRINT("1280002","一个店铺每一种品牌的打印机只能拥有一个"),
    SHOP_ADD_FEI_FAILED("1280003","飞鹅平台添加打印机失败，请稍后重试或者联系管理员"),
    SHOP_PRINT_ERROR_OPTIONS("1280004","飞鹅平台添加打印机失败，新增操作不能传入id参数，请校验"),
    SHOP_PRINT_ERROR_HTTP("1280005","飞鹅平台打印机请求失败，请校验"),
    SHOP_PRINT_ERROR("1280006","飞鹅平台打印失败，请校验"),
    SHOP_PRINT_ERROR_NOT_ORDER_ID("1280007","飞鹅平台打印失败,没有返回打印id，请校验"),
    SHOP_NOT_EXISTS("1280008","店铺不存在，请校验"),
    SHOP_ADD_TIME_ERROR("1280009","时间添加失败，请校验输入必须是21:21格式，英文冒号:"),
    SHOP_DISCOUNT_ALL_PARAMS_NULL("1280010","添加店铺所有商品折扣，必选店铺"),
    SHOP_DISCOUNT_ALL_PARAMS_ID_NULL("1280011","添加店铺所有商品折扣，必填折扣"),
    SHOP_NEED_TO_ID("1280012","店铺操作必选择一个店铺"),

    /**----------9-配送员信息错误-----------*/
    SENDER_HAD_CHANGE("1290001","配送员信息发生变化，请联系后台管理"),
    SENDER_NEED_PHONE("1290002","配送员查询需要电话"),

    /**----------10-订单完成表信息错误-----------*/
    ORDERS_COMPLETE_HAD_ERROR("1210001","订单完成信息错误，请联系后台管理"),

    /**----------11-跑腿订单完成表信息错误-----------*/
    RUN_ORDERS_COMPLETE_HAD_ERROR("1211001","跑腿订单完成失败，请联系后台管理"),
    RUN_ORDERS_WX_USER_ERROR("1211002","跑腿订单下单用户信息错误，请重试或者删除微信小程序重新进入"),

    /**----------12-提现信息错误-----------*/
    TX_ERROR_USER_BELL_FAILED("1212001","用户账户提现失败，请重试或者联系后台管理"),
    TX_ERROR_SCHOOL_BELL_FAILED("1212002","学校负责人账户失败，请重试或者联系后台管理"),
    TX_ERROR_BACK_FAILED("1212003","提现后台失败，请重试或者联系后台管理"),
    TX_ERROR_WX_CHARGE_FAILED("1212004","提现微信交易失败，请重试或者联系后台管理"),

    /**----------13-支付信息错误-----------*/
    PAY_ERROR_SCHOOL_FAILED("1213001","支付时学校数据操作有误，请重试或者联系后台管理"),

    /**----------14-发送微信信息错误-----------*/
    SEND_WX_MESSAGE_ERROR("1214001","发送微信消息错误"),
    SEND_WX_MESSAGE_ERROR_NO_PARAMS("1214002","发送微信消息错误,缺少参数"),

    /**----------15-首页信息错误-----------*/
    INDEX_FIND_NO_SCHOOL("1215001","首页查询没有区分学校"),
    INDEX_ADD_NO_SCHOOL("1215002","首页展示设置没有区分学校"),
    INDEX_ADD_ERROR_NULL("1215003","首页展示设置未选择任何店铺和商品，请检查"),
    INDEX_ADD_ERROR_PARAMS("1215004","首页展示设置格式错误，请检查，必须是1,2,3格式"),

    /**----------16-统计和提现错误错误-----------*/
    TX_DATA_HAD_NOT_COUNT("1216001","后台每日统计数据存疑，禁止当日提现，请联系后台"),
    TX_MONEY_LESS("1216002","可提现金额不足，请校验"),

    /**----------17-配送会员卡-----------*/
    CARD_CAN_NOT_USED("1217001","数据变更，配送卡无法使用，请刷新或联系客服"),
    CARD_BUY_ERROR("1217002","配送卡购买失败，请重试或联系客服"),
    CARD_SEND_CANT_NULL("1217003","配送卡信息错误，请联系客服或者刷新小程序重试"),
    CARD_SEND_CANT_USE_TODAY("1217004","配送卡今天不能使用，请校验"),
    CARD_ORDERS_NEED_HAVE_CARD("1217004","外卖订单缺少配送卡信息，请校验"),


    /**----------18-微信支付错误-----------*/
    WX_PAY_ERROR("1218001","微信支付失败，请重试"),



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
