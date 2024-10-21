package com.reboot_course.firstcome_system.common.exception.exception;

public class DuplicatedException extends RuntimeException {
    public DuplicatedException(String message) {
        super(message);
    }
}