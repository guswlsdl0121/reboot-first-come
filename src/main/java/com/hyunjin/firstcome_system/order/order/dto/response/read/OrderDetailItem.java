package com.hyunjin.firstcome_system.order.order.dto.response.read;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class OrderDetailItem {
    private Integer orderProductId;
    private Integer productId;
    private String productName;
    private Integer quantity;
    private BigDecimal price;
}