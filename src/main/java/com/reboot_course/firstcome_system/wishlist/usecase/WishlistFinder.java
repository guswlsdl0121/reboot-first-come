package com.reboot_course.firstcome_system.wishlist.usecase;

import com.reboot_course.firstcome_system.wishlist.entity.Wishlist;
import com.reboot_course.firstcome_system.wishlist.repository.WishListRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WishlistFinder {
    private final WishListRepository wishListRepository;

    public Wishlist fetchWishlistByIdAndMemberId(int wishlistId, int memberId) {
        return wishListRepository.findByIdAndMemberId(wishlistId, memberId)
                .orElseThrow(() -> new EntityNotFoundException("위시리스트를 찾을 수 없습니다."));
    }
}
