package com.reboot_course.firstcome_system.order.usecase.order;

import com.reboot_course.firstcome_system.order.entity.Order;
import com.reboot_course.firstcome_system.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class OrderModifier {
    private final OrderRepository orderRepository;

    @Transactional
    public void cancel(Order order) {
        order.cancel();
        orderRepository.save(order);
    }

    @Transactional
    public void returnOrder(Order order) {
        order.returned();
        orderRepository.save(order);
    }
}
