package com.reboot_course.firstcome_system.factory;

import com.reboot_course.firstcome_system.product.entity.Product;
import com.reboot_course.firstcome_system.product.repository.ProductRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

@Component
public class TestProductFactory {

    private final ProductRepository productRepository;

    public TestProductFactory(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(Long seq) {
        Product product = Product.builder()
                .name(String.format("Product name %d", seq))
                .price(new BigDecimal(10000))
                .stock(1000)
                .createdAt(LocalDateTime.now())
                .description(String.format("Product description %d", seq))
                .build();
        return productRepository.save(product);
    }

    public List<Product> createMultipleProducts(int count) {
        return IntStream.rangeClosed(1, count)
                .mapToObj(i -> createProduct((long) i))
                .toList();
    }

    public void clearProducts() {
        productRepository.deleteAll();
    }
}