package com.reboot_course.firstcome_system.product.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record ProductMainResponse(List<ProductMain> products, String nextCursor) {
}