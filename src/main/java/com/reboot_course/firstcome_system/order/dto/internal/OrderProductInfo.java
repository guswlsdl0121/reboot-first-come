package com.reboot_course.firstcome_system.order.dto.internal;

import com.reboot_course.firstcome_system.order.entity.OrderProduct;

import java.util.List;
import java.util.Map;

public record OrderProductInfo(
        List<OrderProduct> orderProducts,
        Map<Integer, Integer> productCountMap
) {
    public int getProductCount(Integer orderId) {
        return productCountMap.getOrDefault(orderId, 0);
    }
}
