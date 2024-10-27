package com.reboot_course.firstcome_system.order.domain.entity;

import lombok.Getter;

@Getter
public enum OrderStatus {
    PENDING("결제 대기 중 (결제 시스템 연동 후 사용)"),
    PROCESSING("주문 처리 중 (결제 완료)"),
    SHIPPED("배송 중 (주문 후 D+1)"),
    DELIVERED("배송 완료 (주문 후 D+2)"),
    CANCELLED("주문 취소 (배송 전 취소 시)"),
    RETURNED("반품 완료 (배송 완료 후 D+1일 내 신청 가능)");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }
}