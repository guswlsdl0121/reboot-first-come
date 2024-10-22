package com.reboot_course.firstcome_system.order.usecase.order;

import com.reboot_course.firstcome_system.order.entity.Order;
import com.reboot_course.firstcome_system.order.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderFinder {
    private final OrderRepository orderRepository;

    public Order fetchById(int id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("해당 주문을 찾을 수 없습니다. (id : %d)", id)));
    }
}
