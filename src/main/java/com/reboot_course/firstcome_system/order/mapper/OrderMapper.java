package com.reboot_course.firstcome_system.order.mapper;

import com.reboot_course.firstcome_system.order.dto.internal.OrderProductResult;
import com.reboot_course.firstcome_system.order.dto.response.read.OrderMainItem;
import com.reboot_course.firstcome_system.order.entity.Order;

import java.util.List;

public class OrderMapper {
    private OrderMapper() {} // 인스턴스화 방지

    public static List<OrderMainItem> toOrderMainItems(List<Order> orders, OrderProductResult orderProductResult) {
        return orders.stream()
                .map(order -> toOrderMainItem(order, orderProductResult))
                .toList();
    }

    private static OrderMainItem toOrderMainItem(Order order, OrderProductResult orderProductResult) {
        return OrderMainItem.builder()
                .orderId(order.getId())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .orderDate(order.getCreatedAt())
                .totalQuantity(orderProductResult.getProductCount(order.getId()))
                .build();
    }
}