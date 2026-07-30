package com.forum.demo.exception;

import com.forum.demo.common.AppResult;

/*
* 自定义异常
* */
public class ApplicationException extends RuntimeException{

    protected AppResult errorResult;

    public ApplicationException(AppResult errorResult) {
        super(errorResult.getMessage());
    }

    public ApplicationException() {
    }

    public ApplicationException(String message) {
        super(message);
    }

    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicationException(Throwable cause) {
        super(cause);
    }

    public AppResult getErrorResult() {
        return errorResult;
    }
}
