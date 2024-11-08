package com.hyunjin.wishlist.api.dto.response;

import com.hyunjin.wishlist.entity.Wishlist;
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