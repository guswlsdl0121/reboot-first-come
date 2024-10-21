package com.reboot_course.firstcome_system.wishlist.usecase;

import com.reboot_course.firstcome_system.member.usecase.MemberFinder;
import com.reboot_course.firstcome_system.wishlist.dto.request.WishlistUpdateType;
import com.reboot_course.firstcome_system.wishlist.entity.Wishlist;
import com.reboot_course.firstcome_system.wishlist.repository.WishListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class WishlistModifier {
    private final WishListRepository wishListRepository;
    private final WishlistFinder wishlistFinder;

    @Transactional
    public Integer removeWishlist(int wishlistId, int memberId) {
        Wishlist wishlist = wishlistFinder.fetchWishlistByIdAndMemberId(wishlistId, memberId);
        wishListRepository.delete(wishlist);

        return wishlistId;
    }

    @Transactional
    public void updateQuantity(int wishlistId, int memberId, WishlistUpdateType updateType) {
        Wishlist wishlist = wishlistFinder.fetchWishlistByIdAndMemberId(wishlistId, memberId);

        switch (updateType) {
            case INCREASE:
                wishlist.increaseQuantity();
                break;
            case DECREASE:
                wishlist.decreaseQuantity();
                break;
        }
        wishListRepository.save(wishlist);
    }
}