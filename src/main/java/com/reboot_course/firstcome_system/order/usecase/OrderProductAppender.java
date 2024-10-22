package com.reboot_course.firstcome_system.order.usecase;

import com.reboot_course.firstcome_system.order.dto.request.create.OrderCreateItem;
import com.reboot_course.firstcome_system.order.entity.Order;
import com.reboot_course.firstcome_system.order.entity.OrderProduct;
import com.reboot_course.firstcome_system.order.repository.OrderProductRepository;
import com.reboot_course.firstcome_system.order.vo.OrderProductMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderProductAppender {
    private final OrderProductRepository orderProductRepository;

    @Transactional
    public List<OrderProduct> create(Order order, List<OrderCreateItem> items, OrderProductMap orderProduct) {
        List<OrderProduct> orderProducts = items.stream()
                .map(item ->
                        OrderProduct.builder()
                                .order(order)
                                .product(orderProduct.getProduct(item.getProductId()))
                                .quantity(item.getQuantity())
                                .price(orderProduct.getPrice(item.getProductId()))
                                .build())
                .collect(Collectors.toList());

        return orderProductRepository.saveAll(orderProducts);
    }
}
