package com.reboot_course.firstcome_system.order.usecase;

import com.reboot_course.firstcome_system.order.dto.internal.OrderProductResult;
import com.reboot_course.firstcome_system.order.entity.OrderProduct;
import com.reboot_course.firstcome_system.order.repository.OrderProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderProductReader {
    private final OrderProductRepository orderProductRepository;

    @Transactional(readOnly = true)
    public OrderProductResult getOrderProductAndCount(List<Integer> orderIds) {
        if (orderIds.isEmpty()) {
            return new OrderProductResult(
                    Collections.emptyList(),
                    Collections.emptyMap()
            );
        }

        List<OrderProduct> orderProducts = orderProductRepository.findByOrderIdsWithProduct(orderIds);
        Map<Integer, Integer> orderProductCountMap = orderProducts.stream()
                .collect(Collectors.groupingBy(
                        op -> op.getOrder().getId(),
                        Collectors.collectingAndThen(
                                Collectors.counting(),
                                Long::intValue
                        )
                ));

        return new OrderProductResult(orderProducts, orderProductCountMap);
    }
}