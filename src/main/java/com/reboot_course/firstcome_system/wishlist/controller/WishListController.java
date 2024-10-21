package com.reboot_course.firstcome_system.wishlist.controller;

import com.reboot_course.firstcome_system.common.dto.CommonResponse;
import com.reboot_course.firstcome_system.wishlist.dto.request.WishlistUpdateType;
import com.reboot_course.firstcome_system.wishlist.dto.response.WishlistResponse;
import com.reboot_course.firstcome_system.wishlist.service.WishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wishlist")
public class WishListController {
    private final WishListService wishListService;

    @PostMapping("/{productId}")
    public CommonResponse<Integer> addProductToWishlist(@AuthenticationPrincipal UserDetails userDetails,
                                                        @PathVariable Integer productId) {
        Integer id = wishListService.createWishList(userDetails.getUsername(), productId);
        return CommonResponse.success("장바구니에 상품을 성공적으로 추가했습니다.", id);
    }

    @GetMapping
    public ResponseEntity<WishlistResponse> getWishList(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "10") int size) {
        WishlistResponse response = wishListService.getWishList(userDetails.getUsername(), cursor, size);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{wishlistId}")
    public CommonResponse<Integer> deleteWishlist(@AuthenticationPrincipal UserDetails userDetails,
                                                  @PathVariable Integer wishlistId) {
        Integer id = wishListService.deleteWishlist(userDetails.getUsername(), wishlistId);
        return CommonResponse.success("장바구니에서 상품을 삭제했습니다.", id);
    }

    @PatchMapping("/{wishlistId}")
    public CommonResponse<String> updateQuantity(@AuthenticationPrincipal UserDetails userDetails,
                                                 @PathVariable Integer wishlistId,
                                                 @RequestParam String update) {
        WishlistUpdateType updateType = WishlistUpdateType.fromString(update);
        wishListService.updateWishlistQuantity(userDetails.getUsername(), wishlistId, updateType);
        return CommonResponse.success("장바구니에서 상품의 수량을 성공적으로 업데이트 했습니다.");
    }
}