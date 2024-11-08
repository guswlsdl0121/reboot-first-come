package com.hyunjin.mail.exception;

public class CodeExpiredException extends AuthCodeException {
    public CodeExpiredException() {
        super("인증 코드가 만료되었습니다.");
    }
}