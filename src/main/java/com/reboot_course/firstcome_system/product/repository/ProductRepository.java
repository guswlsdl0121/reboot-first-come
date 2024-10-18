package com.reboot_course.firstcome_system.product.repository;

import com.reboot_course.firstcome_system.product.dto.response.ProductDTO;
import com.reboot_course.firstcome_system.product.entity.Product;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query("""
            SELECT new com.reboot_course.firstcome_system.product.dto.response.ProductDTO(p.id, p.name, p.price)
            FROM Product p
            WHERE p.id < :cursor
            ORDER BY p.id DESC
            LIMIT :size
            """)
    List<ProductDTO> getProducts(@Param("size") Integer size, @Param("cursor") Integer cursor);
}
