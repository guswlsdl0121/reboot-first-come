package com.reboot_course.firstcome_system.order.repository;

import com.reboot_course.firstcome_system.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
}
