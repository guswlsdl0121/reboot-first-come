package com.reboot_course.firstcome_system.product.controller;

import com.reboot_course.firstcome_system.product.dto.response.ProductDetailResponse;
import com.reboot_course.firstcome_system.product.dto.response.ProductMainResponse;
import com.reboot_course.firstcome_system.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
public class ProductController implements ProductWebAPI {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ProductMainResponse> getProductList(
            @RequestParam(required = false) String cursor,
            @RequestParam int size
    ) {
        ProductMainResponse response = productService.getProductList(cursor, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDetailResponse> getProductDetail(@PathVariable int productId) {
        ProductDetailResponse response = productService.getProductDetail(productId);
        return ResponseEntity.ok(response);
    }
}
