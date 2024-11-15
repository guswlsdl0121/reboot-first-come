package com.hyunjin.wishlist.client.product;

import com.hyunjin.wishlist.client.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "product-service", configuration = FeignConfig.class)
public interface ProductServiceClient {
    @GetMapping("/api/v1/product/internal/{productId}")
    ProductResponse getProduct(@PathVariable Integer productId);

    @GetMapping("/api/v1/product/internal/bulk")
    List<ProductResponse> getProducts(@RequestParam List<Integer> productIds);

    @PostMapping("/api/v1/product/internal/{productId}/stock")
    void updateStock(@PathVariable Integer productId, @RequestParam Integer quantity);
}
