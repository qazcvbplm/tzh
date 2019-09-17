package ops.school.api.constants;

/**
 * CreatebyFang
 * fangfor@outlook.com
 * 2019/8/12
 * 11:24
 * #
 */
public class ShopPrintConfigConstants {

    /**
     * 打印机平台飞鹅
     */
    public final static int PRINT_BRAND_DB_FEI_E = 1;

    /**
     * 打印机平台飞印
     */
    public final static int PRINT_BRAND_DB_FE_YIN = 2;

    public final static String FEI_E_URL = "http://api.feieyun.cn/Api/Open/";
    //*必填*：账号名
    public final static String FEI_E_USER = "hzchuyinkeji@163.com";

    //*必填*: 注册账号后生成的UKEY
    public final static String FEI_E_UKEY = "QyN8ICuprmDaJmpe";

    //*必填*: 注册账号后生成的UKEY
    public final static String FEI_E_Open_printerAddlist = "Open_printerAddlist";


    public final static String FE_YIN_URL_2_0 = "https://api.open.feyin.net/";

    public final static String FE_YIN_URL_2_0_TOKEN = "https://api.open.feyin.net/token?";

    // *必填*：账号名 飞印用户的商户编码membercode
    public final static String FE_YIN_2_0_USER_MEMBER_CODE = "53294540bcc411e990a6525400ee10bb";

    // *必填*: 注册账号后生成的UKEY secret 飞印用户的API密钥（在飞印公众号-API接口描述页面可以获取）appkey
    public final static String FE_YIN_2_0_API_KEY = "2cf94351";

    public final static String FE_YIN_2_0_APP_ID = "154609";

    public final static String FEI_E_Open_printMsg = "Open_printMsg";

    public final static int DB_SHOP_PRINT_CLOUD = 1;

    public final static int DB_SHOP_PRINT_BLUE = 2;

}
