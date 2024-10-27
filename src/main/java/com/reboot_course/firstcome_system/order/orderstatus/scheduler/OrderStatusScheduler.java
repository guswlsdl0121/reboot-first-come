package com.reboot_course.firstcome_system.order.orderstatus.scheduler;

import com.reboot_course.firstcome_system.order.domain.entity.OrderStatus;
import com.reboot_course.firstcome_system.order.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderStatusScheduler {
    private final OrderRepository orderRepository;

    /**
     * 주문 상태 자동 업데이트
     * - PROCESSING -> SHIPPED (D+1)
     * - SHIPPED -> DELIVERED (D+2)
     */
    @Scheduled(cron = "0 0 */1 * * *") // 매시 정각마다 실행
    @Transactional
    public void updateOrderStatus() {
        try {
            // PROCESSING -> SHIPPED (D+1)
            int shippedCount = orderRepository.updateOrderStatus(
                    OrderStatus.PROCESSING,
                    OrderStatus.SHIPPED,
                    LocalDateTime.now().minusDays(1)
            );

            // SHIPPED -> DELIVERED (D+2)
            int deliveredCount = orderRepository.updateOrderStatus(
                    OrderStatus.SHIPPED,
                    OrderStatus.DELIVERED,
                    LocalDateTime.now().minusDays(2)
            );

            if (shippedCount > 0 || deliveredCount > 0) {
                log.info("[주문상태 업데이트 완료] SHIPPED: {}건, DELIVERED: {}건",
                        shippedCount, deliveredCount);
            }
        } catch (Exception e) {
            log.error("[주문상태 업데이트 실패] 에러 메시지: {}", e.getMessage(), e);
        }
    }
}