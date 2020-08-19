package cn.stackflow.aums.common.exception;

import cn.stackflow.aums.common.Result;
import cn.stackflow.aums.common.ResultBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;


/**
 * 全局异常处理
 *
 * @author: zhangc/jaguar_zc@sina.com
 * @create: 2020-07-02 09:57
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private static final String DEFAULT_ERROR_MSG = "系统异常";

    private static final String DEFAULT_WEB_ERROR_MSG = "系统繁忙，请稍后再试";

    private static final String DEFAULT_VALIDATE_ERROR_MSG = "参数非法";

    private static final String BUSINESS_ERROR_MSG = "处理业务时发生异常";

    private static final String DEFAULT_ACCESS_DENIED_MSG = "权限不足";

    public static final String USER_PASSWORD_FAIL = "用户名密码错误";

    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Result defaultHandler(ServiceException e) {
        log.error("ServiceException", e);
        return ResultBuilder.fail(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Result defaultHandler(Exception e) {
        log.error("Exception:", e);
        return ResultBuilder.fail(DEFAULT_ERROR_MSG);
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Result unauthorizedExceptionHandler(UnauthorizedException e) {
        log.error("UnauthorizedException", e);
        return ResultBuilder.fail(e.getMessage());
    }


    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Result illegalArgumentExceptionHandler(IllegalArgumentException e) {
        log.error("IllegalArgumentException", e);
        return ResultBuilder.fail(e.getMessage());
    }


    @ExceptionHandler(BindException.class)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Result defaultHandler(BindException e) {
        this.printStackTrace(e);
        ObjectError error = e.getFieldError();
        if (error == null) {
            List<ObjectError> errors = e.getAllErrors();
            error = errors.get(0);
        }
        if (error.contains(ConversionNotSupportedException.class)) {
            return ResultBuilder.fail(DEFAULT_VALIDATE_ERROR_MSG);
        }
        return ResultBuilder.fail(error.getDefaultMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Result defaultMethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException", e);
        FieldError fieldError = e.getBindingResult().getFieldError();
        return ResultBuilder.fail("[" + fieldError.getField() + "]" + fieldError.getDefaultMessage());
    }


    private void printStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        pw.close();
        log.error("\n{}", sw.toString());
    }

}
