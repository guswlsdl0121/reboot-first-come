package com.hyunjin.order.service;

import com.hyunjin.order.dto.request.create.OrderCreateItem;
import com.hyunjin.order.dto.request.create.OrderCreateRequest;
import com.hyunjin.order.dto.response.read.OrderDetailItem;
import com.hyunjin.order.dto.response.read.OrderDetailResponse;
import com.hyunjin.order.dto.response.read.OrderMainItem;
import com.hyunjin.order.dto.response.read.OrderMainResponse;
import com.hyunjin.order.entity.Order;
import com.hyunjin.order.entity.OrderProduct;
import com.hyunjin.order.entity.OrderStatus;
import com.hyunjin.order.repository.OrderProductRepository;
import com.hyunjin.order.repository.OrderRepository;
import com.hyunjin.wishlist.client.member.MemberServiceClient;
import com.hyunjin.wishlist.client.product.ProductResponse;
import com.hyunjin.wishlist.client.product.ProductServiceClient;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final MemberServiceClient memberClient;
    private final ProductServiceClient productClient;
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final OrderStockService orderStockService;

    public OrderMainResponse getOrders(Integer memberId, String cursor, int size) {
        // 회원 검증
        memberClient.getMember(memberId);

        // 주문 목록 조회를 위한 ID 목록 가져오기
        Integer cursorValue = determineCursor(cursor);
        List<Integer> orderIds = orderRepository.getOrderIds(memberId, cursorValue, size);

        if (orderIds.isEmpty()) {
            return OrderMainResponse.builder()
                    .items(Collections.emptyList())
                    .build();
        }

        // 주문 정보 조회
        List<Order> orders = orderRepository.findByIds(orderIds);
        List<OrderProduct> orderProducts = orderProductRepository.findByOrderIdsWithProduct(orderIds);

        // 주문별 상품 개수 맵 생성
        Map<Integer, Integer> orderProductCountMap = orderProducts.stream()
                .collect(Collectors.groupingBy(
                        op -> op.getOrder().getId(),
                        Collectors.collectingAndThen(
                                Collectors.counting(),
                                Long::intValue
                        )
                ));

        // Response 생성
        List<OrderMainItem> items = orders.stream()
                .map(order -> OrderMainItem.builder()
                        .orderId(order.getId())
                        .status(order.getStatus())
                        .totalAmount(order.getTotalAmount())
                        .orderDate(order.getCreatedAt())
                        .totalQuantity(orderProductCountMap.getOrDefault(order.getId(), 0))
                        .build())
                .collect(Collectors.toList());

        String nextCursor = getNextCursor(size, orderIds);

        return OrderMainResponse.builder()
                .items(items)
                .nextCursor(nextCursor)
                .totalCount(items.size())
                .build();
    }

    public OrderDetailResponse getOrderDetail(Integer orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("주문을 찾을 수 없습니다."));

        List<OrderProduct> orderProducts = orderProductRepository.findByOrderId(orderId);

        List<OrderDetailItem> items = orderProducts.stream()
                .map(op -> {
                    var product = productClient.getProduct(op.getProductId());
                    return OrderDetailItem.builder()
                            .orderProductId(op.getId())
                            .productId(product.getId())
                            .productName(product.getName())
                            .quantity(op.getQuantity())
                            .price(op.getPrice())
                            .build();
                })
                .collect(Collectors.toList());

        return OrderDetailResponse.builder()
                .orderId(order.getId())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .orderDate(order.getCreatedAt())
                .items(items)
                .build();
    }

    @Transactional
    public Integer createOrder(Integer memberId, OrderCreateRequest request) {
        // 회원 검증
        memberClient.getMember(memberId);

        // 상품 정보 조회 및 검증
        List<ProductResponse> products = productClient.getProducts(
                request.getProducts().stream()
                        .map(OrderCreateItem::getProductId)
                        .collect(Collectors.toList())
        );

        // 재고 감소
        orderStockService.decreaseStock(request.getProducts());

        // 주문 총액 계산
        BigDecimal totalAmount = calculateTotalAmount(request.getProducts(), products);

        // 주문 생성
        Order order = Order.builder()
                .memberId(memberId)
                .totalAmount(totalAmount)
                .status(OrderStatus.PROCESSING)
                .build();
        orderRepository.save(order);

        // 주문 상품 생성
        List<OrderProduct> orderProducts = request.getProducts().stream()
                .map(item -> {
                    var product = products.stream()
                            .filter(p -> p.getId().equals(item.getProductId()))
                            .findFirst()
                            .orElseThrow();

                    return OrderProduct.builder()
                            .order(order)
                            .productId(item.getProductId())
                            .quantity(item.getQuantity())
                            .price(product.getPrice())
                            .build();
                })
                .collect(Collectors.toList());

        orderProductRepository.saveAll(orderProducts);

        return order.getId();
    }

    @Transactional
    public void cancelOrder(Integer memberId, Integer orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("주문을 찾을 수 없습니다."));

        // 주문 취소 가능 여부 검증
        validateForCancel(order, memberId);

        // 재고 원복
        List<OrderProduct> orderProducts = orderProductRepository.findByOrderId(orderId);
        orderStockService.increaseStockForCancel(orderProducts);

        // 주문 상태 변경
        order.cancel();
        orderRepository.save(order);
    }

    @Transactional
    public void applyReturnOrder(Integer memberId, Integer orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("주문을 찾을 수 없습니다."));

        validateForReturn(order, memberId);
        order.returnRequest();
        orderRepository.save(order);
    }

    private BigDecimal calculateTotalAmount(List<OrderCreateItem> orderItems, List<ProductResponse> products) {
        return orderItems.stream()
                .map(item -> {
                    var product = products.stream()
                            .filter(p -> p.getId().equals(item.getProductId()))
                            .findFirst()
                            .orElseThrow();
                    return product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void validateForCancel(Order order, Integer memberId) {
        if (!order.getMemberId().equals(memberId)) {
            throw new IllegalArgumentException("주문자만 주문을 취소할 수 있습니다.");
        }

        OrderStatus status = order.getStatus();
        if (status == OrderStatus.SHIPPED ||
                status == OrderStatus.DELIVERED ||
                status == OrderStatus.CANCELLED ||
                status == OrderStatus.RETURN_REQUESTED ||
                status == OrderStatus.RETURN_COMPLETED) {
            throw new IllegalStateException(
                    "취소할 수 없는 주문 상태입니다. (현재 상태: " + status.getDescription() + ")"
            );
        }
    }

    private void validateForReturn(Order order, Integer memberId) {
        if (!order.getMemberId().equals(memberId)) {
            throw new IllegalArgumentException("주문자만 반품을 신청할 수 있습니다.");
        }

        if (order.getStatus() != OrderStatus.DELIVERED) {
            throw new IllegalStateException(
                    "반품은 배송 완료 상태에서만 가능합니다. (현재 상태: " + order.getStatus().getDescription() + ")"
            );
        }

        LocalDateTime deliveredDate = order.getUpdatedAt();
        LocalDateTime returnDeadline = deliveredDate.plusDays(1);

        if (LocalDateTime.now().isAfter(returnDeadline)) {
            throw new IllegalStateException("반품 신청 기한이 지났습니다. (기한: 배송완료 후 1일 이내)");
        }
    }

    private int determineCursor(String cursor) {
        if (cursor == null) {
            return Integer.MAX_VALUE;
        }
        return Integer.parseInt(cursor);
    }

    private String getNextCursor(int requestedSize, List<Integer> items) {
        if (items.size() != requestedSize) {
            return null;
        }
        return String.valueOf(items.getLast());
    }
}