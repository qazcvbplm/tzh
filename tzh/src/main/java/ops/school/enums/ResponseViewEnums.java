package ops.school.enums;

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
