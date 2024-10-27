package com.reboot_course.firstcome_system.order.domain.usecase.orderproduct;

import com.reboot_course.firstcome_system.order.domain.entity.OrderProduct;
import com.reboot_course.firstcome_system.order.domain.repository.OrderProductRepository;
import com.reboot_course.firstcome_system.order.order.dto.internal.OrderProductInfo;
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
    public OrderProductInfo getByIdsWithProductCount(List<Integer> orderIds) {
        if (orderIds.isEmpty()) {
            return new OrderProductInfo(
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

        return new OrderProductInfo(orderProducts, orderProductCountMap);
    }

    @Transactional(readOnly = true)
    public List<OrderProduct> getById(Integer id) {
        return orderProductRepository.findByOrderId(id);
    }
}