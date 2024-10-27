package com.reboot_course.firstcome_system.order.orderstock.scheduler;

import com.reboot_course.firstcome_system.order.domain.entity.Order;
import com.reboot_course.firstcome_system.order.domain.entity.OrderStatus;
import com.reboot_course.firstcome_system.order.domain.repository.OrderRepository;
import com.reboot_course.firstcome_system.order.orderstock.service.OrderStockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReturnOrderScheduler {
    private final OrderRepository orderRepository;
    private final OrderStockService orderStockService;

    @Scheduled(cron = "0 0 * * * *") // 매시 정각
    @Transactional
    public void processReturnOrders() {
        try {
            // 1. 24시간 이상 지난 반품 요청 주문들 조회
            LocalDateTime beforeTime = LocalDateTime.now().minusHours(24);
            List<Order> returnOrders = orderRepository.findByStatusAndUpdatedAtBefore(
                    OrderStatus.RETURN_REQUESTED,
                    beforeTime
            );

            for (Order order : returnOrders) {
                try {
                    // 2. 재고 증가
                    orderStockService.increaseStockForReturn(order.getId());

                    // 3. 주문 상태 변경
                    order.returnComplete();
                    orderRepository.save(order);

                    log.info("반품 처리 완료 - orderId: {}, 반품 요청 시간: {}",
                            order.getId(), order.getUpdatedAt());
                } catch (Exception e) {
                    log.error("반품 처리 실패 - orderId: {}", order.getId(), e);
                }
            }
        } catch (Exception e) {
            log.error("반품 처리 스케줄러 실행 중 오류 발생", e);
        }
    }
}