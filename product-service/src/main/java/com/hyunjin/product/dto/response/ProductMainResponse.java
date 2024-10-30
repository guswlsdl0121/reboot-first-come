package com.hyunjin.product.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record ProductMainResponse(List<ProductItemDTO> items, String nextCursor) {
}