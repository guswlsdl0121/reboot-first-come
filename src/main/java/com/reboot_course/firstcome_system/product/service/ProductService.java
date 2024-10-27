package com.reboot_course.firstcome_system.product.service;

import com.reboot_course.firstcome_system.product.dto.response.ProductDetailResponse;
import com.reboot_course.firstcome_system.product.dto.response.ProductMainResponse;
import com.reboot_course.firstcome_system.product.entity.Product;
import com.reboot_course.firstcome_system.product.usecase.ProductFinder;
import com.reboot_course.firstcome_system.product.usecase.ProductReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductFinder productFinder;
    private final ProductReader productReader;

    public ProductMainResponse getProductList(String cursor, int size) {
        return productReader.getMainList(cursor, size);
    }

    public ProductDetailResponse getProductDetail(int productId) {
        Product product = productFinder.fetchById(productId);

        return ProductDetailResponse.builder()
                .productId(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getStock())
                .build();
    }
}
