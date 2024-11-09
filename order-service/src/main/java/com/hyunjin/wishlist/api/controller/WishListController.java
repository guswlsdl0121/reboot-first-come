package com.hyunjin.wishlist.api.controller;

import com.hyunjin.wishlist.api.dto.request.WishlistUpdateType;
import com.hyunjin.wishlist.api.dto.response.WishlistMainResponse;
import com.hyunjin.wishlist.api.dto.response.WishlistUpdateResponse;
import com.hyunjin.wishlist.service.WishListService;
import com.hyunjin.wishlist.utils.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/wishlist")
public class WishListController {
    private final WishListService wishListService;

    @PostMapping("/{productId}")
    public CommonResponse<Integer> addProductToWishlist(@PathVariable Integer productId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Integer memberId = Integer.parseInt(auth.getName());
        Integer wishlistId = wishListService.createWishList(memberId, productId);
        return CommonResponse.success("장바구니에 상품을 성공적으로 추가했습니다.", wishlistId);
    }

    @GetMapping
    public ResponseEntity<WishlistMainResponse> getWishList(
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "10") int size) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Integer memberId = Integer.parseInt(auth.getName());
        WishlistMainResponse response = wishListService.getWishList(memberId, cursor, size);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{wishlistId}")
    public CommonResponse<Integer> deleteWishlist(@PathVariable Integer wishlistId) {
        Integer id = wishListService.deleteWishlist(wishlistId);
        return CommonResponse.success("장바구니에서 상품을 삭제했습니다.", id);
    }

    @PatchMapping("/{wishlistId}")
    public CommonResponse<WishlistUpdateResponse> updateQuantity(
            @PathVariable Integer wishlistId,
            @RequestParam WishlistUpdateType update) {
        WishlistUpdateResponse response = wishListService.updateWishlistQuantity(wishlistId, update);
        return CommonResponse.success("장바구니에서 상품의 수량을 성공적으로 업데이트 했습니다.", response);
    }
}