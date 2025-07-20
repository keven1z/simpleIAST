package com.keven1z.core.error.http;

public class PolicyException extends HttpException {
    public PolicyException(String message, Throwable cause) {
        super(message, cause);
    }
}
