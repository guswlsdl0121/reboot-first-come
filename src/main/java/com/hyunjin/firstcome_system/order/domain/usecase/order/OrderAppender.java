package com.hyunjin.firstcome_system.order.domain.usecase.order;

import com.hyunjin.firstcome_system.member.entity.Member;
import com.hyunjin.firstcome_system.order.domain.entity.Order;
import com.hyunjin.firstcome_system.order.domain.entity.OrderStatus;
import com.hyunjin.firstcome_system.order.domain.repository.OrderRepository;
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
                .status(OrderStatus.PROCESSING)
                .build();

        return orderRepository.save(order);
    }
}
