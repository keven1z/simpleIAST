package com.keven1z.core.error.http;

/**
 * @author keven1z
 * @date 2025/8/23
 */
public class HeartbeatSendException extends HttpException{
    public HeartbeatSendException(String message, Throwable cause) {
        super(message, cause);
    }
}
