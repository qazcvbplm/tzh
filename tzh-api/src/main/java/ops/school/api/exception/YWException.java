package ops.school.api.exception;

public class YWException extends RuntimeException {

    private String msg;

    @Override
    public String getMessage() {
        return this.msg;
    }

    public YWException(String msg) {
        this.msg = msg;
    }
}
