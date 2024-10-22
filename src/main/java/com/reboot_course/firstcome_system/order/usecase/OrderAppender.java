package com.reboot_course.firstcome_system.order.usecase;

import com.reboot_course.firstcome_system.member.entity.Member;
import com.reboot_course.firstcome_system.order.entity.Order;
import com.reboot_course.firstcome_system.order.entity.OrderStatus;
import com.reboot_course.firstcome_system.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class OrderAppender {
    private final OrderRepository orderRepository;

    @Transactional
    public Order create(Member member, BigDecimal totalAmount) {
        Order order = Order.builder()
                .member(member)
                .totalAmount(totalAmount)
                .status(OrderStatus.PENDING)
                .build();

        return orderRepository.save(order);
    }
}
