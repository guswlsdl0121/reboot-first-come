package com.hyunjin.product.repository;


import com.hyunjin.product.dto.response.ProductItemDTO;
import com.hyunjin.product.entity.Product;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query("""
            SELECT new com.hyunjin.product.dto.response.ProductItemDTO(p.id, p.name, p.price)
            FROM Product p
            WHERE p.id < :cursor
            ORDER BY p.id DESC
            LIMIT :size
            """)
    List<ProductItemDTO> getProducts(@Param("size") Integer size, @Param("cursor") Integer cursor);

    @Modifying
    @Query("""
            UPDATE Product p
            SET p.stock = :quantity
            WHERE p.id = :productId
            """)
    void updateStock(@Param("productId") Integer productId, @Param("quantity") int quantity);

    List<Product> findAllByIdIn(List<Integer> productIds);
}