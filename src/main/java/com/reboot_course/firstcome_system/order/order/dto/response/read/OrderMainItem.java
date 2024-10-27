package com.reboot_course.firstcome_system.order.order.dto.response.read;

import com.reboot_course.firstcome_system.order.domain.entity.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class OrderMainItem {
    private Integer orderId;
    private OrderStatus status;
    private BigDecimal totalAmount;
    private LocalDateTime orderDate;
    private int totalQuantity;
}
