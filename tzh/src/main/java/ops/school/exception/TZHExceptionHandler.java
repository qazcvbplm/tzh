package ops.school.exception;


import ops.school.api.exception.DisplayException;
import ops.school.api.exception.YWException;
import ops.school.api.util.LoggerUtil;
import ops.school.api.util.ResponseObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

@RestControllerAdvice
public class TZHExceptionHandler {

    private static Logger logger = LoggerFactory.getLogger(TZHExceptionHandler.class);

    final Writer result = new StringWriter();
    final PrintWriter printWriter = new PrintWriter(result);


    private StringWriter stringWriter;

    /**
     * 全局异常捕捉处理
     *
     * @param ex
     * @return
     */

    @ExceptionHandler(DisplayException.class)
    public ResponseObject handlerDisplayException(DisplayException ex) {
        logger.error(ex.getMessage(), ex);
        StackTraceElement stackTraceElement= ex.getStackTrace()[0];
        System.out.println(stackTraceElement.getMethodName());
        System.out.println(stackTraceElement.getLineNumber());
        return new ResponseObject(false, ex.getMessage());
    }

    @ExceptionHandler(YWException.class)
    public ResponseObject handlerYWException(YWException ex) {
       /* stringWriter = new StringWriter();
        ex.printStackTrace(new PrintWriter(stringWriter));*/
        StackTraceElement[] trace = ex.getStackTrace();
        StringBuilder out = new StringBuilder();
        for (StackTraceElement s : trace) {
            out.append("\tat " + s + "\r\n");
        }
        LoggerUtil.log(out.toString());
        if (ex instanceof YWException) {
            return new ResponseObject(false, ex.getMessage());
        } else {
            return new ResponseObject(false, "服务器被外星人攻击了！");
        }
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseObject handlerNoFoundException(Exception ex) {
        logger.error(ex.getMessage(), ex);
        StackTraceElement stackTraceElement= ex.getStackTrace()[0];
        System.out.println(stackTraceElement.getMethodName());
        System.out.println(stackTraceElement.getLineNumber());
        return new ResponseObject(false, "页面被外星人带走了！");
    }

    @ExceptionHandler(Exception.class)
    public ResponseObject handleException(Exception ex) {
        ex.fillInStackTrace().printStackTrace(printWriter);
        logger.error(ex.toString());
        return new ResponseObject(false, "服务器被外星人攻击了！");
    }

}
