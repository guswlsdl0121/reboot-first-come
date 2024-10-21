package com.reboot_course.firstcome_system.wishlist.service;

import com.reboot_course.firstcome_system.wishlist.controller.ProductWishlistResponse;
import com.reboot_course.firstcome_system.wishlist.controller.WishlistUpdateType;
import com.reboot_course.firstcome_system.wishlist.repository.WishListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WishListService {
    private final WishListRepository wishListRepository;

    public Integer createWishList(String username, Integer productId) {
        return null;
    }

    public ProductWishlistResponse getWishList(String username) {
        return null;
    }

    public Integer deleteWishlist(String username, Integer wishlistId) {
        return null;
    }

    public void updateWishlistQuantity(String username, Integer wishlistId, WishlistUpdateType updateType) {

    }
}
