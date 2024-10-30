package com.hyunjin.firstcome_system.wishlist.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hyunjin.firstcome_system.product.dto.response.ProductItemDTO;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class WishlistItemDTO extends ProductItemDTO {
    @JsonProperty("wishlist_id")
    private final int wishlistId;
    private final int quantity;

    public WishlistItemDTO(@JsonProperty("wishlist_id") int wishlistId,
                           @JsonProperty("product_id") int productId,
                           String name,
                           BigDecimal price,
                           int quantity) {
        super(productId, name, price);
        this.wishlistId = wishlistId;
        this.quantity = quantity;
    }
}