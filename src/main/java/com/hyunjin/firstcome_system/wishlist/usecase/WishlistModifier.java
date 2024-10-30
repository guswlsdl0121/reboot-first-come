package com.hyunjin.firstcome_system.wishlist.usecase;

import com.hyunjin.firstcome_system.wishlist.dto.request.WishlistUpdateType;
import com.hyunjin.firstcome_system.wishlist.entity.Wishlist;
import com.hyunjin.firstcome_system.wishlist.repository.WishListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class WishlistModifier {
    private final WishListRepository wishListRepository;
    private final WishlistFinder wishlistFinder;

    @Transactional
    public Integer removeWishlist(int wishlistId) {
        Wishlist wishlist = wishlistFinder.fetchById(wishlistId);
        wishListRepository.delete(wishlist);
        return wishlistId;
    }

    @Transactional
    public Wishlist updateQuantity(int wishlistId, WishlistUpdateType updateType) {
        Wishlist wishlist = wishlistFinder.fetchById(wishlistId);

        switch (updateType) {
            case INCREASE:
                wishlist.increaseQuantity();
                break;
            case DECREASE:
                wishlist.decreaseQuantity();
                break;
        }
        return wishListRepository.save(wishlist);
    }
}