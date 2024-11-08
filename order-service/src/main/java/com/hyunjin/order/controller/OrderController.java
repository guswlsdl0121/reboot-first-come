package com.hyunjin.order.controller;

import com.hyunjin.order.dto.request.create.OrderCreateRequest;
import com.hyunjin.order.dto.response.read.OrderDetailResponse;
import com.hyunjin.order.dto.response.read.OrderMainResponse;
import com.hyunjin.order.service.OrderService;
import com.hyunjin.wishlist.utils.CommonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<OrderMainResponse> getOrders(
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "10") int size) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Integer memberId = Integer.parseInt(auth.getName());

        OrderMainResponse response = orderService.getOrders(
                memberId,
                cursor,
                size
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDetailResponse> getOrderDetail(@PathVariable Integer orderId) {
        OrderDetailResponse response = orderService.getOrderDetail(orderId);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<CommonResponse<Integer>> createOrder(
            @Valid @RequestBody OrderCreateRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Integer memberId = Integer.parseInt(auth.getName());
        Integer orderId = orderService.createOrder(memberId, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CommonResponse.success("주문이 성공적으로 생성되었습니다.", orderId));
    }

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<CommonResponse<Integer>> cancelOrder(
            @PathVariable Integer orderId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Integer memberId = Integer.parseInt(auth.getName());

        orderService.cancelOrder(memberId, orderId);

        return ResponseEntity
                .ok(CommonResponse.success("주문이 성공적으로 취소되었습니다.", orderId));
    }

    @PostMapping("/{orderId}/return")
    public ResponseEntity<CommonResponse<Integer>> returnOrder(
            @PathVariable Integer orderId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Integer memberId = Integer.parseInt(auth.getName());

        orderService.applyReturnOrder(memberId, orderId);
        return ResponseEntity
                .ok(CommonResponse.success("반품 신청이 성공적으로 처리되었습니다.", orderId));
    }
}