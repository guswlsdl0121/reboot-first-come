package com.hyunjin.wishlist.api.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class WishlistItemDTO {
    @JsonProperty("wishlist_id")
    private final int wishlistId;
    private final int quantity;
    private final int productId;
    private final String productName;
    private final BigDecimal productPrice;

    public WishlistItemDTO(@JsonProperty("wishlist_id") int wishlistId,
                           @JsonProperty("product_id") int productId,
                           String name,
                           BigDecimal price,
                           int quantity) {
        this.productId = productId;
        this.productName = name;
        this.productPrice = price;
        this.wishlistId = wishlistId;
        this.quantity = quantity;
    }
}