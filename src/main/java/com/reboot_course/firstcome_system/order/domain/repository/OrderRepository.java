package com.reboot_course.firstcome_system.order.domain.repository;

import com.reboot_course.firstcome_system.order.domain.entity.Order;
import com.reboot_course.firstcome_system.order.domain.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query("""
            SELECT o.id
            FROM Order o
            WHERE o.member.id = :memberId AND o.id < :cursor
            ORDER BY o.id DESC
            LIMIT :size
            """)
    List<Integer> getOrderIds(@Param("memberId") Integer memberId,
                              @Param("cursor") Integer cursor,
                              @Param("size") Integer size);

    @Query("""
            SELECT o
            FROM Order o
            WHERE o.id IN :ids
            """)
    List<Order> findByIds(@Param("ids") List<Integer> ids);


    @Modifying
    @Query("""
        UPDATE Order o
        SET o.status = :newStatus,
            o.updatedAt = CURRENT_TIMESTAMP
        WHERE o.status = :currentStatus
        AND o.createdAt <= :targetTime
        """)
    int updateOrderStatus(
            @Param("currentStatus") OrderStatus currentStatus,
            @Param("newStatus") OrderStatus newStatus,
            @Param("targetTime") LocalDateTime targetTime
    );
}