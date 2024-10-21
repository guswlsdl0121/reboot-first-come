package com.reboot_course.firstcome_system.wishlist.controller;

import lombok.Getter;

@Getter
public enum WishlistUpdateType {
    INCREASE("increase"),
    DECREASE("decrease");

    private final String value;

    WishlistUpdateType(String value) {
        this.value = value;
    }

    public static WishlistUpdateType fromString(String value) {
        for (WishlistUpdateType type : WishlistUpdateType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("타입이 맞지 않습니다. increase 또는 decrease를 선택해야 합니다.");
    }
}