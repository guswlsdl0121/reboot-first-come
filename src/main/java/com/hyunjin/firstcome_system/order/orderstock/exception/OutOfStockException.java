package com.hyunjin.firstcome_system.order.orderstock.exception;

public class OutOfStockException extends RuntimeException {
    public OutOfStockException(String message) {
        super(message);
    }
}
