package com.hyunjin.firstcome_system.product.config;

import com.hyunjin.firstcome_system.common.CommonTestConfig;
import com.hyunjin.firstcome_system.product.repository.ProductRepository;
import com.hyunjin.firstcome_system.product.usecase.ProductFinder;
import com.hyunjin.firstcome_system.product.usecase.ProductReader;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class ProductTestConfig extends CommonTestConfig {
    @Bean
    public ProductFinder productFinder(ProductRepository productRepository) {
        return new ProductFinder(productRepository);
    }

    @Bean
    public ProductReader productReader(ProductRepository productRepository) {
        return new ProductReader(productRepository);
    }
}