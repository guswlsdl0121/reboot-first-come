package com.hyunjin.product.service;


import com.hyunjin.product.dto.response.ProductDetailResponse;
import com.hyunjin.product.dto.response.ProductMainResponse;
import com.hyunjin.product.entity.Product;
import com.hyunjin.product.exception.exception.OutOfStockException;
import com.hyunjin.product.repository.ProductRepository;
import com.hyunjin.product.usecase.ProductFinder;
import com.hyunjin.product.usecase.ProductReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductFinder productFinder;
    private final ProductReader productReader;
    private final ProductRepository productRepository;

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

    @Transactional
    public void updateStock(Integer productId, Integer quantity) {
        Product product = productFinder.fetchById(productId);

        int newStock = product.getStock() + quantity;
        if (newStock < 0) {
            throw new OutOfStockException("재고가 부족합니다. 현재 재고: " + product.getStock() + ", 요청 수량: " + Math.abs(quantity));
        }

        productRepository.updateStock(productId, newStock);
    }

    public List<Product> fetchByIds(List<Integer> productIds) {
        return productRepository.findAllByIdIn(productIds);
    }
}
