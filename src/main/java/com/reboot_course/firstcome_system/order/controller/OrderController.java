package com.reboot_course.firstcome_system.order.controller;

import com.reboot_course.firstcome_system.common.dto.CommonResponse;
import com.reboot_course.firstcome_system.order.dto.request.create.OrderCreateRequest;
import com.reboot_course.firstcome_system.order.dto.response.read.OrderDetailResponse;
import com.reboot_course.firstcome_system.order.dto.response.read.OrderMainResponse;
import com.reboot_course.firstcome_system.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController implements OrderWebAPI {
    private final OrderService orderService;

    @Override
    @GetMapping
    public ResponseEntity<OrderMainResponse> getOrders(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "10") int size) {

        OrderMainResponse response = orderService.getOrders(
                userDetails.getUsername(),
                cursor,
                size
        );

        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDetailResponse> getOrderDetail(@PathVariable Integer orderId) {
        OrderDetailResponse response = orderService.getOrderDetail(orderId);
        return ResponseEntity.ok(response);
    }

    @Override
    @PostMapping
    public ResponseEntity<CommonResponse<Integer>> createOrder(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody OrderCreateRequest request) {

        Integer orderId = orderService.createOrder(userDetails.getUsername(), request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CommonResponse.success("주문이 성공적으로 생성되었습니다.", orderId));
    }

    @Override
    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<CommonResponse<Integer>> cancelOrder(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Integer orderId) {

        orderService.cancelOrder(userDetails.getUsername(), orderId);

        return ResponseEntity
                .ok(CommonResponse.success("주문이 성공적으로 취소되었습니다.", orderId));
    }

    @Override
    @PostMapping("/{orderId}/return")
    public ResponseEntity<CommonResponse<Integer>> returnOrder(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Integer orderId) {

        orderService.returnOrder(userDetails.getUsername(), orderId);

        return ResponseEntity
                .ok(CommonResponse.success("반품 신청이 성공적으로 처리되었습니다.", orderId));
    }

}
