package com.hyunjin.firstcome_system.wishlist.usecase;

import com.hyunjin.firstcome_system.wishlist.entity.Wishlist;
import com.hyunjin.firstcome_system.wishlist.repository.WishListRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WishlistFinder {
    private final WishListRepository wishListRepository;

    public Wishlist fetchById(int wishlistId) {
        return wishListRepository.findById(wishlistId)
                .orElseThrow(() -> new EntityNotFoundException("위시리스트를 찾을 수 없습니다."));
    }
}
