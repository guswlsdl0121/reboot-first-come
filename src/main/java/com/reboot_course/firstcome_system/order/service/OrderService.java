package com.reboot_course.firstcome_system.order.service;

import com.reboot_course.firstcome_system.member.entity.Member;
import com.reboot_course.firstcome_system.member.usecase.MemberFinder;
import com.reboot_course.firstcome_system.order.dto.internal.OrderPaging;
import com.reboot_course.firstcome_system.order.dto.internal.OrderProductInfo;
import com.reboot_course.firstcome_system.order.dto.request.create.OrderCreateRequest;
import com.reboot_course.firstcome_system.order.dto.response.read.OrderDetailResponse;
import com.reboot_course.firstcome_system.order.dto.response.read.OrderMainItem;
import com.reboot_course.firstcome_system.order.dto.response.read.OrderMainResponse;
import com.reboot_course.firstcome_system.order.entity.Order;
import com.reboot_course.firstcome_system.order.entity.OrderProduct;
import com.reboot_course.firstcome_system.order.mapper.OrderMapper;
import com.reboot_course.firstcome_system.order.usecase.OrderAppender;
import com.reboot_course.firstcome_system.order.usecase.OrderProductAppender;
import com.reboot_course.firstcome_system.order.usecase.OrderProductReader;
import com.reboot_course.firstcome_system.order.usecase.OrderReader;
import com.reboot_course.firstcome_system.order.vo.OrderProductMap;
import com.reboot_course.firstcome_system.product.usecase.ProductReader;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final MemberFinder memberFinder;
    private final OrderFinder orderFinder;
    private final OrderReader orderReader;
    private final ProductReader productReader;
    private final OrderProductReader orderProductReader;
    private final OrderAppender orderAppender;
    private final OrderProductAppender orderProductAppender;


    public OrderMainResponse getOrders(String email, String cursor, int size) {
        // 1. 사용자 찾기
        Member member = memberFinder.fetchByEmail(email);

        // 2. 사용자의 주문 목록 id 및 주문 정보 찾기 (pagination으로)
        OrderPaging result = orderReader.getByIdsForPagination(member.getId(), cursor, size);
        List<Order> orders = orderReader.getByIds(result.ids());

        // 3. 주문 id를 통해 주문 상품 정보와 카운트 맵 조회
        OrderProductInfo orderProductInfo = orderProductReader.getByIdsWithProductCount(result.ids());

        // 4. 반환
        List<OrderMainItem> orderItems = OrderMapper.toOrderMainItems(orders, orderProductInfo);
        return OrderMainResponse.builder()
                .items(orderItems)
                .nextCursor(result.nextCursor())
                .totalCount(orderItems.size())
                .build();
    }

    public OrderDetailResponse getOrderDetail(Integer orderId) {
        // 1. orderId로 주문 찾기
        Order order = orderFinder.fetchById(orderId);

        // 2. orderId로 주문상품 목록 찾기
        List<OrderProduct> orderProducts = orderProductReader.getById(order.getId());

        // 3. 반환
        return OrderMapper.toOrderDetailResponse(order, orderProducts);
    }

    @Transactional
    public Integer createOrder(String email, @Valid OrderCreateRequest request) {
        // 1. 주문자 정보 조회
        Member member = memberFinder.fetchByEmail(email);

        // 2. 주문 상품 정보 조회 및 검증
        OrderProductMap orderProductMap = productReader.getOrderProduct(request.getProducts());

        // TODO: [재고 관리]
        // 1. Redis를 통해 재고 감소 처리
        // 2. 재고 부족시 실패 처리
        // 3. 고려사항:
        //    - Redis key 생명주기 관리
        //    - 트랜잭션 실패시 재고 복구 방안
        //    - 동시성 제어 방식

        // 3. 주문 생성
        Order order = orderAppender.create(member, orderProductMap.calculateTotalAmount(request.getProducts()));

        // 4. 주문 상품 생성
        orderProductAppender.create(order, request.getProducts(), orderProductMap);

        return order.getId();
    }

    public void cancelOrder(String email, Integer orderId) {
    }

    public void returnOrder(String email, Integer orderId) {
    }
}
