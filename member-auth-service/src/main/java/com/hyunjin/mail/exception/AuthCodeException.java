package com.hyunjin.mail.exception;

public class AuthCodeException extends RuntimeException {
    public AuthCodeException(String message) {
        super(message);
    }

    public AuthCodeException(String message, Throwable cause) {
        super(message, cause);
    }
}