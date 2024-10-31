package com.hyunjin.firstcome_system.order.domain.repository;

import com.hyunjin.firstcome_system.order.domain.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct, Integer> {
    @Query("""
            SELECT op
            FROM OrderProduct op
            JOIN FETCH op.product
            WHERE op.order.id IN :orderIds
            """)
    List<OrderProduct> findByOrderIdsWithProduct(@Param("orderIds") List<Integer> orderIds);

    @Query("""
            SELECT op
            FROM OrderProduct op
            JOIN FETCH op.product
            WHERE op.order.id = :orderId
            """)
    List<OrderProduct> findByOrderId(@Param("orderId") Integer orderId);
}