package com.reboot_course.firstcome_system.order.orderstock.service;

import com.reboot_course.firstcome_system.order.domain.entity.OrderProduct;
import com.reboot_course.firstcome_system.order.domain.usecase.orderproduct.OrderProductReader;
import com.reboot_course.firstcome_system.order.order.dto.request.create.OrderCreateItem;
import com.reboot_course.firstcome_system.order.orderstock.exception.OutOfStockException;
import com.reboot_course.firstcome_system.order.orderstock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderStockService {
    private final StockRepository stockRepository;
    private final OrderProductReader orderProductReader;

    @Transactional
    public void decreaseStock(List<OrderCreateItem> orderItems) {
        for (OrderCreateItem item : orderItems) {
            boolean success = stockRepository.decrease(item.getProductId(), item.getQuantity());
            if (!success) {
                throw new OutOfStockException(String.format(
                        "상품(id: %d)의 재고가 부족합니다.", item.getProductId()
                ));
            }
        }
    }

    @Transactional
    public void increaseStockForCancel(List<OrderProduct> orderProducts) {
        for (OrderProduct orderProduct : orderProducts) {
            stockRepository.increase(
                    orderProduct.getProduct().getId(),
                    orderProduct.getQuantity()
            );
        }
    }

    @Transactional
    public void increaseStockForReturn(Integer orderId) {
        var orderProducts = orderProductReader.getById(orderId);
        for (OrderProduct orderProduct : orderProducts) {
            stockRepository.increase(
                    orderProduct.getProduct().getId(),
                    orderProduct.getQuantity()
            );
        }
        log.info("반품 상품 재고 증가 완료 - orderId: {}", orderId);
    }
}