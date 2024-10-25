package com.reboot_course.firstcome_system.authmail.mail.exception.exception;

public class CodeNotFoundException extends AuthCodeException {
    public CodeNotFoundException() {
        super("인증 코드가 잘못되었거나 찾을 수 없습니다.");
    }
}
