package com.reboot_course.firstcome_system.product.controller;

import com.reboot_course.firstcome_system.product.dto.ProductDetailResponse;
import com.reboot_course.firstcome_system.product.dto.ProductListResponse;
import com.reboot_course.firstcome_system.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ProductListResponse> getProductList(@RequestParam Long cursor, @RequestParam int size) {
        ProductListResponse response = productService.getProductList(cursor, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDetailResponse> getProductDetail(@PathVariable Long productId) {
        ProductDetailResponse response = productService.getProductDetail(productId);
        return ResponseEntity.ok(response);
    }
}