package com.reboot_course.firstcome_system.product.dto.response;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ProductDetailResponse(int productId, String name, String description, BigDecimal price, Integer quantity) {
}
