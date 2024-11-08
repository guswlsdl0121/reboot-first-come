package com.hyunjin.wishlist.client.product;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service")
public interface ProductServiceClient {
    @GetMapping("/api/v1/product/internal/{productId}")
    ProductResponse getProduct(@PathVariable Integer productId);
}
