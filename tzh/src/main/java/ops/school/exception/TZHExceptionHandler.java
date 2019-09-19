package ops.school.exception;


import ops.school.api.exception.DisplayException;
import ops.school.api.exception.YWException;
import ops.school.api.util.LoggerUtil;
import ops.school.api.util.ResponseObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NestedIOException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

@RestControllerAdvice
public class TZHExceptionHandler {

    private static Logger logger = LoggerFactory.getLogger(TZHExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseObject handleException(Exception ex) {
        StackTraceElement[] elements =  ex.getStackTrace();
        StringBuilder message = new StringBuilder();
        message.append(ex.getMessage());
        message.append("\n");
        for (StackTraceElement element : elements) {
            message.append(element.toString());
            message.append("\n");
        }
        logger.error(message.toString());
        return new ResponseObject(false, "服务器被外星人攻击了！");
    }

    /**
     * 全局异常捕捉处理
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(DisplayException.class)
    public ResponseObject handlerDisplayException(DisplayException ex) {
        StackTraceElement[] elements =  ex.getStackTrace();
        StringBuilder message = new StringBuilder();
        message.append(ex.getMessage());
        message.append("\n");
        for (StackTraceElement element : elements) {
            message.append(element.toString());
            message.append("\n");
        }
        logger.error(message.toString());
        return new ResponseObject(false, ex.getMessage());
    }

    @ExceptionHandler(YWException.class)
    public ResponseObject handlerYWException(YWException ex) {
        StackTraceElement[] elements =  ex.getStackTrace();
        StringBuilder message = new StringBuilder();
        message.append(ex.getMessage());
        message.append("\n");
        for (StackTraceElement element : elements) {
            message.append(element.toString());
            message.append("\n");
        }
        logger.error(message.toString());
        return new ResponseObject(false, ex.getMessage());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseObject handlerNoFoundException(Exception ex) {
        StackTraceElement[] elements =  ex.getStackTrace();
        StringBuilder message = new StringBuilder();
        message.append(ex.getMessage());
        message.append("\n");
        for (StackTraceElement element : elements) {
            message.append(element.toString());
            message.append("\n");
        }
        logger.error(message.toString());
        return new ResponseObject(false, "页面被外星人带走了！");
    }

    @ExceptionHandler(NestedIOException.class)
    public ResponseObject handleNestedIOException(NestedIOException ex) {
        StackTraceElement[] elements =  ex.getStackTrace();
        StringBuilder message = new StringBuilder();
        message.append(ex.getMessage());
        message.append("\n");
        for (StackTraceElement element : elements) {
            message.append(element.toString());
            message.append("\n");
        }
        logger.error(message.toString());
        return new ResponseObject(false, "服务器被外星人攻击了！");
    }



}
