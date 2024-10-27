package com.reboot_course.firstcome_system.order.order.dto.response.read;

import com.reboot_course.firstcome_system.order.domain.entity.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class OrderDetailResponse {
    private Integer orderId;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private LocalDateTime orderDate;
    private List<OrderDetailItem> items;
}