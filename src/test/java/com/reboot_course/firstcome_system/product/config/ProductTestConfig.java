package com.reboot_course.firstcome_system.product.config;

import com.reboot_course.firstcome_system.common.CommonTestConfig;
import com.reboot_course.firstcome_system.product.repository.ProductRepository;
import com.reboot_course.firstcome_system.product.usecase.ProductFinder;
import com.reboot_course.firstcome_system.product.usecase.ProductReader;
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