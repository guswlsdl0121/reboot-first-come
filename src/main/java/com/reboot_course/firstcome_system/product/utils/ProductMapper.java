package com.reboot_course.firstcome_system.product.utils;

import com.reboot_course.firstcome_system.product.dto.response.ProductDetailResponse;
import com.reboot_course.firstcome_system.product.entity.Product;

public class ProductMapper {
    public static ProductDetailResponse toProductDetailResponse(Product product) {
        return ProductDetailResponse.builder()
                .id(product.getId().longValue())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .build();
    }
    private ProductMapper() {}
}