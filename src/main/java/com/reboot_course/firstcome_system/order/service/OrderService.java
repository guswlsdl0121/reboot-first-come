package com.reboot_course.firstcome_system.order.service;

import com.reboot_course.firstcome_system.order.dto.request.create.OrderCreateRequest;
import com.reboot_course.firstcome_system.order.dto.response.read.OrderDetailResponse;
import com.reboot_course.firstcome_system.order.dto.response.read.OrderMainResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
    public OrderMainResponse getOrders(String email, String cursor, int size) {
        return null;
    }

    public OrderDetailResponse getOrderDetail(String email, Integer orderId) {
        return null;
    }

    public Integer createOrder(String email, @Valid OrderCreateRequest request) {
        return null;
    }

    public void cancelOrder(String email, Integer orderId) {
        return;
    }

    public void returnOrder(String email, Integer orderId) {
        return;
    }
}
