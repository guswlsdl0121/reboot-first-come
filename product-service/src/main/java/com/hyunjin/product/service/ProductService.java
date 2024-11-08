package com.hyunjin.product.service;


import com.hyunjin.product.dto.response.ProductDetailResponse;
import com.hyunjin.product.dto.response.ProductMainResponse;
import com.hyunjin.product.entity.Product;
import com.hyunjin.product.usecase.ProductFinder;
import com.hyunjin.product.usecase.ProductReader;
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

    public Product fetchById(Integer productId) {
        return productFinder.fetchById(productId);
    }
}
