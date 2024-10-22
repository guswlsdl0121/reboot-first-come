package com.reboot_course.firstcome_system.wishlist.dto.response;

import com.reboot_course.firstcome_system.wishlist.entity.Wishlist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class WishlistUpdateResponse {
    private Integer wishlistId;
    private Integer quantity;

    public static WishlistUpdateResponse from(Wishlist wishlist) {
        return WishlistUpdateResponse.builder()
                .wishlistId(wishlist.getId())
                .quantity(wishlist.getQuantity())
                .build();
    }
}