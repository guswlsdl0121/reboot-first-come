package com.reboot_course.firstcome_system.order.service;

import com.reboot_course.firstcome_system.member.entity.Member;
import com.reboot_course.firstcome_system.member.usecase.MemberFinder;
import com.reboot_course.firstcome_system.order.dto.internal.OrderProductResult;
import com.reboot_course.firstcome_system.order.dto.internal.OrderResult;
import com.reboot_course.firstcome_system.order.dto.request.create.OrderCreateRequest;
import com.reboot_course.firstcome_system.order.dto.response.read.OrderDetailResponse;
import com.reboot_course.firstcome_system.order.dto.response.read.OrderMainItem;
import com.reboot_course.firstcome_system.order.dto.response.read.OrderMainResponse;
import com.reboot_course.firstcome_system.order.entity.Order;
import com.reboot_course.firstcome_system.order.mapper.OrderMapper;
import com.reboot_course.firstcome_system.order.usecase.OrderProductReader;
import com.reboot_course.firstcome_system.order.usecase.OrderReader;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final MemberFinder memberFinder;
    private final OrderReader orderReader;
    private final OrderProductReader orderProductReader;


    public OrderMainResponse getOrders(String email, String cursor, int size) {
        // 1. 사용자 찾기
        Member member = memberFinder.fetchByEmail(email);

        // 2. 사용자의 주문 목록 id 및 주문 정보 찾기 (pagination으로)
        OrderResult result = orderReader.getIdsForPagination(member.getId(), cursor, size);
        List<Order> orders = orderReader.getByIds(result.ids());

        // 3. 주문 id를 통해 주문 상품 정보와 카운트 맵 조회
        OrderProductResult orderProductResult = orderProductReader.getOrderProductAndCount(result.ids());

        // 반환
        List<OrderMainItem> orderItems = OrderMapper.toOrderMainItems(orders, orderProductResult);
        return OrderMainResponse.builder()
                .items(orderItems)
                .nextCursor(result.nextCursor())
                .totalCount(orderItems.size())
                .build();
    }

    public OrderDetailResponse getOrderDetail(String email, Integer orderId) {
        return null;
    }

    public Integer createOrder(String email, @Valid OrderCreateRequest request) {
        return null;
    }

    public void cancelOrder(String email, Integer orderId) {
    }

    public void returnOrder(String email, Integer orderId) {
    }
}
