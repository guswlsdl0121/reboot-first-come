package com.hyunjin.product.usecase;


import com.hyunjin.product.entity.Product;
import com.hyunjin.product.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ProductFinder {
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public Product fetchById(int productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("해당 상품을 찾을 수 없습니다. (productId : %d)", productId)));
    }
}
