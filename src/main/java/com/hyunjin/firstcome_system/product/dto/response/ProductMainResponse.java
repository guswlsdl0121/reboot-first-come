package com.hyunjin.firstcome_system.product.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record ProductMainResponse(List<ProductItemDTO> items, String nextCursor) {
}