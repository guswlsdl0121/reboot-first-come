package com.reboot_course.firstcome_system.order.vo;

import com.reboot_course.firstcome_system.order.dto.request.create.OrderCreateItem;
import com.reboot_course.firstcome_system.product.entity.Product;
import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public class OrderProductMap {
    private final Map<Integer, Product> products;

    private OrderProductMap(List<Product> products) {
        this.products = products.stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));
    }

    public static OrderProductMap from(List<Product> products) {
        return new OrderProductMap(products);
    }

    public void validateProductsExist(List<OrderCreateItem> orderItems) {
        if (orderItems.size() != products.size()) {
            throw new EntityNotFoundException("요청한 상품 중 존재하지 않는 상품이 있습니다.");
        }
    }

    public Product getProduct(Integer productId) {
        return products.get(productId);
    }

    public BigDecimal getPrice(Integer productId) {
        return products.get(productId).getPrice();
    }

    public BigDecimal calculateTotalAmount(List<OrderCreateItem> orderItems) {
        return orderItems.stream()
                .map(this::calculateItemAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateItemAmount(OrderCreateItem item) {
        return getPrice(item.getProductId()).multiply(BigDecimal.valueOf(item.getQuantity()));
    }
}