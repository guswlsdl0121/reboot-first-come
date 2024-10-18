package com.reboot_course.firstcome_system.product.controller;

import com.reboot_course.firstcome_system.product.dto.response.ProductDetailResponse;
import com.reboot_course.firstcome_system.product.dto.response.ProductListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Product", description = "상품 관련 API")
public interface ProductWebAPI {

    @Operation(summary = "상품 목록 조회", description = "커서 기반 페이지네이션을 사용하여 상품 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 상품 목록을 조회함",
                    content = @Content(schema = @Schema(implementation = ProductListResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    ResponseEntity<ProductListResponse> getProductList(
            @Parameter(description = "다음 페이지 조회를 위한 커서. 첫 페이지 조회 시 null")
            @RequestParam(required = false) String cursor,
            @Parameter(description = "한 페이지에 조회할 상품 수", example = "10")
            @RequestParam int size
    );

    @Operation(summary = "상품 상세 조회", description = "상품 ID를 이용하여 상품의 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 상품 상세 정보를 조회함",
                    content = @Content(schema = @Schema(implementation = ProductDetailResponse.class))),
            @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    ResponseEntity<ProductDetailResponse> getProductDetail(
            @Parameter(description = "조회할 상품의 ID", example = "1")
            @PathVariable int productId
    );
}