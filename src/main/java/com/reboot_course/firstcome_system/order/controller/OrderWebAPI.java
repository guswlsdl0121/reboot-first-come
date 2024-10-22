package com.reboot_course.firstcome_system.order.controller;

import com.reboot_course.firstcome_system.common.dto.CommonResponse;
import com.reboot_course.firstcome_system.order.dto.request.create.OrderCreateRequest;
import com.reboot_course.firstcome_system.order.dto.response.read.OrderDetailResponse;
import com.reboot_course.firstcome_system.order.dto.response.read.OrderMainResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Order", description = "주문 관련 API")
public interface OrderWebAPI {

    @Operation(summary = "주문 목록 조회", description = "사용자의 주문 목록을 페이지네이션하여 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = OrderMainResponse.class)))
    })
    ResponseEntity<OrderMainResponse> getOrders(
            @Parameter(hidden = true) UserDetails userDetails,
            @Parameter(description = "마지막으로 조회된 주문 ID")
            @RequestParam(required = false) String cursor,
            @Parameter(description = "조회할 주문 개수", example = "10")
            @RequestParam(defaultValue = "10") int size
    );

    @Operation(summary = "주문 상세 조회", description = "특정 주문의 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = OrderDetailResponse.class)))
    })
    ResponseEntity<OrderDetailResponse> getOrderDetail(
            @Parameter(hidden = true) UserDetails userDetails,
            @Parameter(description = "조회할 주문 ID", example = "12345")
            @PathVariable Integer orderId
    );

    @Operation(summary = "주문 생성", description = """
            선택한 상품들로 새로운 주문을 생성합니다.
            - 재고 부족으로 인한 주문 불가는 1주차에서는 고려하지 않습니다.
            - 주문 생성 시 상태는 PENDING으로 시작합니다.
            """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "주문 생성 성공",
                    content = @Content(schema = @Schema(implementation = CommonResponse.class)))
    })
    ResponseEntity<CommonResponse<Integer>> createOrder(
            @Parameter(hidden = true) UserDetails userDetails,
            @Parameter(description = "주문 생성 요청 정보")
            @Valid @RequestBody OrderCreateRequest request
    );

    @Operation(summary = "주문 취소", description = """
            주문을 취소합니다.
            - 배송중(SHIPPED) 상태 이전까지만 취소가 가능합니다.
            - 취소 시 상품의 재고가 자동으로 복구됩니다.
            - 취소 완료 후 상태는 CANCELLED로 변경됩니다.
            """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "주문 취소 성공",
                    content = @Content(schema = @Schema(implementation = CommonResponse.class)))
    })
    ResponseEntity<CommonResponse<Integer>> cancelOrder(
            @Parameter(hidden = true) UserDetails userDetails,
            @Parameter(description = "취소할 주문 ID", example = "12345")
            @PathVariable Integer orderId
    );

    @Operation(summary = "주문 반품", description = """
            주문을 반품 처리합니다.
            - 배송 완료(DELIVERED) 후 D+1일까지만 반품이 가능합니다.
            - 반품 신청 후 D+1에 재고가 자동으로 반영됩니다.
            - 반품 완료 후 상태는 RETURNED로 변경됩니다.
            """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "반품 신청 성공",
                    content = @Content(schema = @Schema(implementation = CommonResponse.class)))
    })
    ResponseEntity<CommonResponse<Integer>> returnOrder(
            @Parameter(hidden = true) UserDetails userDetails,
            @Parameter(description = "반품할 주문 ID", example = "12345")
            @PathVariable Integer orderId
    );
}