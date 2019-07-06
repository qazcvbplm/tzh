package ops.school.exception;


import ops.school.api.exception.YWException;
import ops.school.api.util.LoggerUtil;
import ops.school.api.util.ResponseObject;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.StringWriter;

@ControllerAdvice
public class ExceptionHandler {

	private StringWriter stringWriter;
	  /**
     * 全局异常捕捉处理
     * @param ex
     * @return
     */
    @ResponseBody
    @org.springframework.web.bind.annotation.ExceptionHandler(RuntimeException.class)
    public ResponseObject errorHandler(Exception ex) {
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
    	}
    	else{
            return new ResponseObject(false, "服务器被外星人攻击了！");
    	}
    }
    
}
