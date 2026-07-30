package com.forum.demo.exception;


import com.forum.demo.common.AppResult;
import com.forum.demo.common.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(ApplicationException.class)
    public AppResult applicationExceptionHandler(ApplicationException e) {
        //打印异常信息
        e.printStackTrace();
        //打印日志
        log.error(e.getMessage());
        //非空校验
        if(e.getMessage() == null || e.getMessage().equals("")) {
            return AppResult.failed(ResultCode.ERROR_SERVICES);
        }
        //返回具体异常信息
        return AppResult.failed(e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public AppResult exceptionHandler(Exception e) {
        e.printStackTrace();
        log.error(e.getMessage());
        if(e.getMessage() == null || e.getMessage().equals("")) {
            return AppResult.failed(ResultCode.ERROR_SERVICES);
        }
        return AppResult.failed(e.getMessage());
    }
}
