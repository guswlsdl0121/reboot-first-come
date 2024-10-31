package com.hyunjin.product.dto.response;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ProductDetailResponse(int productId, String name, String description, BigDecimal price,
                                    Integer quantity) {
}
