package com.hyunjin.wishlist.api.dto.request;

import lombok.Getter;

@Getter
public enum WishlistUpdateType {
    INCREASE("increase"),
    DECREASE("decrease");

    private final String value;

    WishlistUpdateType(String value) {
        this.value = value;
    }
}