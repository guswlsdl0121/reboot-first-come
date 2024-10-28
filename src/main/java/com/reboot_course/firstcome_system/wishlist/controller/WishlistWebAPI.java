package com.reboot_course.firstcome_system.wishlist.controller;

import com.hyunjin.common.dto.CommonResponse;
import com.reboot_course.firstcome_system.wishlist.dto.request.WishlistUpdateType;
import com.reboot_course.firstcome_system.wishlist.dto.response.WishlistMainResponse;
import com.reboot_course.firstcome_system.wishlist.dto.response.WishlistUpdateResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Wishlist", description = "위시리스트(장바구니) 관련 API")
public interface WishlistWebAPI {

    @Operation(summary = "위시리스트에 상품 추가", description = "로그인한 사용자의 위시리스트에 상품을 추가합니다.")
    @ApiResponse(responseCode = "200", description = "성공적으로 상품을 위시리스트에 추가함",
            content = @Content(schema = @Schema(implementation = CommonResponse.class)))
    CommonResponse<Integer> addProductToWishlist(
            @Parameter(description = "인증된 사용자 정보", hidden = true)
            @AuthenticationPrincipal UserDetails userDetails,
            @Parameter(description = "추가할 상품의 ID", example = "1")
            @PathVariable Integer productId
    );

    @Operation(summary = "위시리스트 조회", description = "로그인한 사용자의 위시리스트를 커서 기반 페이지네이션으로 조회합니다.")
    @ApiResponse(responseCode = "200", description = "성공적으로 위시리스트를 조회함",
            content = @Content(schema = @Schema(implementation = WishlistMainResponse.class)))
    ResponseEntity<WishlistMainResponse> getWishList(
            @Parameter(description = "인증된 사용자 정보", hidden = true)
            @AuthenticationPrincipal UserDetails userDetails,
            @Parameter(description = "다음 페이지 조회를 위한 커서. 첫 페이지 조회 시 null")
            @RequestParam(required = false) String cursor,
            @Parameter(description = "한 페이지에 조회할 상품 수", example = "10")
            @RequestParam(defaultValue = "10") int size
    );

    @Operation(summary = "위시리스트에서 상품 삭제", description = "위시리스트에서 특정 상품을 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "성공적으로 상품을 위시리스트에서 삭제함",
            content = @Content(schema = @Schema(implementation = CommonResponse.class)))
    CommonResponse<Integer> deleteWishlist(
            @Parameter(description = "삭제할 위시리스트 항목의 ID", example = "1")
            @PathVariable Integer wishlistId
    );

    @Operation(summary = "위시리스트 상품 수량 수정", description = "위시리스트에 있는 상품의 수량을 수정합니다.")
    @ApiResponse(responseCode = "200", description = "성공적으로 상품 수량을 수정함",
            content = @Content(schema = @Schema(implementation = CommonResponse.class)))
    CommonResponse<WishlistUpdateResponse> updateQuantity(
            @Parameter(description = "수정할 위시리스트 항목의 ID", example = "1")
            @PathVariable Integer wishlistId,
            @Parameter(description = "수량 업데이트 타입 (increase 또는 decrease)", example = "increase")
            @RequestParam WishlistUpdateType update
    );
}