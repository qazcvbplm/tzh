package ops.school.enums;

/**
 * @author CreateByFang on Time:2019/7/15 16:45
 * description:
 */
public enum PublicErrorEnums implements RootEnums {
    /**操作成功*/
    SUCCESS("A11001","操作成功"),
    /**操作失败*/
    FAILED("A11002","操作失败"),
    /**参数为空*/
    PULBIC_EMPTY_PARAM("A11003","参数为空"),
    /**参数错误*/
    PULBIC_ERROR_PARAM("A11004","参数错误"),
    PUBLIC_DATA_ERROR("A11005","数据为空"),
    PUBLIC_DATA_CHANGE("A11006","数据发生变更，请刷新"),
    /**参数错误*/
    PUBLIC_DO_FAILED("A11007","操作失败"),
    /**楼栋错误*/
    FLOOR_SELECT_NULL("F11007","楼栋为空，更新后操作"),
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
