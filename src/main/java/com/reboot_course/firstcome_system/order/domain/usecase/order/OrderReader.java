package com.reboot_course.firstcome_system.order.domain.usecase.order;

import com.reboot_course.firstcome_system.common.utils.CursorUtils;
import com.reboot_course.firstcome_system.order.order.dto.internal.OrderPaging;
import com.reboot_course.firstcome_system.order.domain.entity.Order;
import com.reboot_course.firstcome_system.order.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderReader {
    private final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public OrderPaging getByIdsForPagination(Integer memberId, String cursor, int size) {
        int cursorValue = CursorUtils.determineCursor(cursor);
        List<Integer> orderIds = orderRepository.getOrderIds(memberId, cursorValue, size);
        String nextCursor = CursorUtils.getNextCursor(size, orderIds, id -> id);

        return new OrderPaging(orderIds, nextCursor);
    }

    @Transactional(readOnly = true)
    public List<Order> getByIds(List<Integer> orderIds) {
        if (orderIds.isEmpty()) {
            return Collections.emptyList();
        }
        return orderRepository.findByIds(orderIds);
    }
}