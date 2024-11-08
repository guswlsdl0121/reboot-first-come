package com.hyunjin.product.controller;

import com.hyunjin.product.dto.response.ProductDetailResponse;
import com.hyunjin.product.dto.response.ProductMainResponse;
import com.hyunjin.product.dto.response.ProductResponse;
import com.hyunjin.product.entity.Product;
import com.hyunjin.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product")
public class ProductController {
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

    @GetMapping("/internal/{productId}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Integer productId) {
        Product product = productService.fetchById(productId);
        return ResponseEntity.ok(ProductResponse.from(product));
    }
}
