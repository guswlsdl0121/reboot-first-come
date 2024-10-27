package com.reboot_course.firstcome_system.order.domain.usecase.order;

import com.reboot_course.firstcome_system.order.domain.entity.Order;
import com.reboot_course.firstcome_system.order.domain.repository.OrderRepository;
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
    public void returnRequest(Order order) {
        order.returnRequest();
        orderRepository.save(order);
    }
}
