package com.reboot_course.firstcome_system.product.repository;

import com.reboot_course.firstcome_system.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
