package com.keven1z.core.error;

public class RegistrationException extends HttpException{
    public RegistrationException(String message) {
        super(message);
    }
    public RegistrationException(String message, Throwable cause) {
        super(message, cause);
    }
}
