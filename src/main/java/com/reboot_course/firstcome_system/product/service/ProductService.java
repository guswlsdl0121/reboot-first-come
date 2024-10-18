package com.reboot_course.firstcome_system.product.service;

import com.reboot_course.firstcome_system.product.dto.ProductDetailResponse;
import com.reboot_course.firstcome_system.product.dto.ProductListResponse;
import com.reboot_course.firstcome_system.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;


    public ProductListResponse getProductList(Long cursor, int size) {
        return null;
    }

    public ProductDetailResponse getProductDetail(Long productId) {
        return null;
    }
}
