package com.reboot_course.firstcome_system.order.orderstock.exception;

public class OutOfStockException extends RuntimeException {
    public OutOfStockException(String message) {
        super(message);
    }
}
