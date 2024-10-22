package com.reboot_course.firstcome_system.common.config;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

@Profile("dev")
@Configuration
@RequiredArgsConstructor
public class DatabaseCleanupConfig {
    private final EntityManager entityManager;

    @Bean
    @Transactional
    CommandLineRunner cleanupUserData() {
        return args -> {
            entityManager.createNativeQuery("DELETE FROM wishlist").executeUpdate();
            entityManager.createNativeQuery("DELETE FROM order_product").executeUpdate();
            entityManager.createNativeQuery("DELETE FROM `order`").executeUpdate();
            entityManager.createNativeQuery("DELETE FROM member").executeUpdate();

            entityManager.createNativeQuery("ALTER TABLE wishlist AUTO_INCREMENT = 1").executeUpdate();
            entityManager.createNativeQuery("ALTER TABLE order_product AUTO_INCREMENT = 1").executeUpdate();
            entityManager.createNativeQuery("ALTER TABLE `order` AUTO_INCREMENT = 1").executeUpdate();
            entityManager.createNativeQuery("ALTER TABLE member AUTO_INCREMENT = 1").executeUpdate();
        };
    }
}