package com.reboot_course.firstcome_system.order.mapper;

import com.reboot_course.firstcome_system.order.dto.internal.OrderProductResult;
import com.reboot_course.firstcome_system.order.dto.response.read.OrderDetailItem;
import com.reboot_course.firstcome_system.order.dto.response.read.OrderDetailResponse;
import com.reboot_course.firstcome_system.order.dto.response.read.OrderMainItem;
import com.reboot_course.firstcome_system.order.entity.Order;
import com.reboot_course.firstcome_system.order.entity.OrderProduct;

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

    public static OrderDetailResponse toOrderDetailResponse(Order order, List<OrderProduct> orderProducts) {
        return OrderDetailResponse.builder()
                .orderId(order.getId())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .orderDate(order.getCreatedAt())
                .items(toOrderDetailItems(orderProducts))
                .build();
    }

    private static List<OrderDetailItem> toOrderDetailItems(List<OrderProduct> orderProducts) {
        return orderProducts.stream()
                .map(op -> OrderDetailItem.builder()
                        .orderProductId(op.getId())
                        .productId(op.getProduct().getId())
                        .productName(op.getProduct().getName())
                        .quantity(op.getQuantity())
                        .price(op.getPrice())
                        .build())
                .toList();
    }
}