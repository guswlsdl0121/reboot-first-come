package com.reboot_course.firstcome_system.order.usecase.order;

import com.reboot_course.firstcome_system.order.entity.Order;
import com.reboot_course.firstcome_system.order.entity.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class OrderValidator {

    public void validateForCancel(Order order, Integer memberId) {
        validateOwnership(order, memberId);
        validateCancellableStatus(order);
    }

    public void validateForReturn(Order order, Integer memberId) {
        validateOwnership(order, memberId);
        validateReturnableStatus(order);
        validateReturnPeriod(order);
    }

    private void validateOwnership(Order order, Integer memberId) {
        if (!order.getMember().getId().equals(memberId)) {
            throw new IllegalArgumentException("주문자만 주문을 변경할 수 있습니다.");
        }
    }

    private void validateCancellableStatus(Order order) {
        OrderStatus status = order.getStatus();
        if (status == OrderStatus.SHIPPED ||
                status == OrderStatus.DELIVERED ||
                status == OrderStatus.CANCELLED ||
                status == OrderStatus.RETURNED) {
            throw new IllegalStateException(
                    "취소할 수 없는 주문 상태입니다. (현재 상태: " + status.getDescription() + ")"
            );
        }
    }

    private void validateReturnableStatus(Order order) {
        if (order.getStatus() != OrderStatus.DELIVERED) {
            throw new IllegalStateException(
                    "반품은 배송 완료 상태에서만 가능합니다. (현재 상태: " + order.getStatus().getDescription() + ")"
            );
        }
    }

    private void validateReturnPeriod(Order order) {
        LocalDateTime deliveredDate = order.getUpdatedAt();
        LocalDateTime returnDeadline = deliveredDate.plusDays(1);

        if (LocalDateTime.now().isAfter(returnDeadline)) {
            throw new IllegalStateException("반품 신청 기한이 지났습니다. (기한: 배송완료 후 1일 이내)");
        }
    }
}