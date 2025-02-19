package com.keven1z.core.error;

public class HttpException extends Exception {
    public HttpException(String message) {
        super(message);
    }

    // 自定义异常构造器，传递异常信息和原始异常
    public HttpException(String message, Throwable cause) {
        super(message, cause);
    }
}
