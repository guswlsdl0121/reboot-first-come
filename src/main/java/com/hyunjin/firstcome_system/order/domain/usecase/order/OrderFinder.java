package com.hyunjin.firstcome_system.order.domain.usecase.order;

import com.hyunjin.firstcome_system.order.domain.entity.Order;
import com.hyunjin.firstcome_system.order.domain.repository.OrderRepository;
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
