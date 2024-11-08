package com.hyunjin.product.controller;

import com.hyunjin.product.dto.response.ProductDetailResponse;
import com.hyunjin.product.dto.response.ProductMainResponse;
import com.hyunjin.product.dto.response.ProductResponse;
import com.hyunjin.product.entity.Product;
import com.hyunjin.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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

    @PostMapping("/internal/{productId}/stock")
    public ResponseEntity<Void> updateStock(
            @PathVariable Integer productId,
            @RequestParam Integer quantity) {
        productService.updateStock(productId, quantity);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/internal/bulk")
    public ResponseEntity<List<ProductResponse>> getProducts(@RequestParam List<Integer> productIds) {
        List<Product> products = productService.fetchByIds(productIds);
        List<ProductResponse> responses = products.stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
}