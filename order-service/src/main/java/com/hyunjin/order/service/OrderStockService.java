package com.hyunjin.order.service;

import com.hyunjin.order.dto.request.create.OrderCreateItem;
import com.hyunjin.order.entity.OrderProduct;
import com.hyunjin.order.exception.OutOfStockException;
import com.hyunjin.wishlist.client.product.ProductServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderStockService {
    private final ProductServiceClient productClient;

    /**
     * 주문 생성 시 재고 감소
     */
    @Transactional
    public void decreaseStock(List<OrderCreateItem> orderItems) {
        for (OrderCreateItem item : orderItems) {
            try {
                productClient.updateStock(item.getProductId(), -item.getQuantity());
            } catch (Exception e) {
                log.error("Failed to decrease stock for product: {}. Error: {}",
                        item.getProductId(), e.getMessage());
                throw new OutOfStockException(String.format(
                        "상품(id: %d)의 재고 처리에 실패했습니다.", item.getProductId()
                ));
            }
        }
    }

    /**
     * 주문 취소 시 재고 증가
     */
    @Transactional
    public void increaseStockForCancel(List<OrderProduct> orderProducts) {
        for (OrderProduct orderProduct : orderProducts) {
            try {
                productClient.updateStock(
                        orderProduct.getProductId(),
                        orderProduct.getQuantity()
                );
                log.info("Stock increased for cancelled order - productId: {}, quantity: {}",
                        orderProduct.getProductId(), orderProduct.getQuantity());
            } catch (Exception e) {
                log.error("Failed to increase stock for cancelled order - productId: {}, quantity: {}",
                        orderProduct.getProductId(), orderProduct.getQuantity(), e);
                throw new RuntimeException("재고 복구 처리에 실패했습니다.", e);
            }
        }
    }

    /**
     * 반품 완료 시 재고 증가
     */
    @Transactional
    public void increaseStockForReturn(Integer orderId, List<OrderProduct> orderProducts) {
        for (OrderProduct orderProduct : orderProducts) {
            try {
                productClient.updateStock(
                        orderProduct.getProductId(),
                        orderProduct.getQuantity()
                );
                log.info("Stock increased for returned order - orderId: {}, productId: {}, quantity: {}",
                        orderId, orderProduct.getProductId(), orderProduct.getQuantity());
            } catch (Exception e) {
                log.error("Failed to increase stock for returned order - orderId: {}, productId: {}",
                        orderId, orderProduct.getProductId(), e);
                throw new RuntimeException("반품 상품의 재고 처리에 실패했습니다.", e);
            }
        }
    }
}