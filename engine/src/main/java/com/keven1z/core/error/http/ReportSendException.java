package com.keven1z.core.error.http;

/**
 * @author keven1z
 * @date 2025/8/23
 */
public class ReportSendException extends HttpException {
    public ReportSendException(String message, Throwable cause) {
        super(message, cause);
    }
}
