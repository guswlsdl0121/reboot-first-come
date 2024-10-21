package com.reboot_course.firstcome_system.wishlist.usecase;

import com.reboot_course.firstcome_system.common.exception.exception.DuplicatedException;
import com.reboot_course.firstcome_system.member.entity.Member;
import com.reboot_course.firstcome_system.product.entity.Product;
import com.reboot_course.firstcome_system.wishlist.entity.Wishlist;
import com.reboot_course.firstcome_system.wishlist.repository.WishListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WishlistAppender {
    private final WishListRepository wishListRepository;

    public Wishlist validateUniqueAndSave(Member member, Product product) {
        if(wishListRepository.existsByMemberAndProduct(member, product)) {
            throw new DuplicatedException("해당 상품이 이미 위시리스트에 있습니다.");
        }

        Wishlist newWishList = Wishlist.builder()
                .member(member)
                .product(product)
                .quantity(1)
                .build();

        return wishListRepository.save(newWishList);
    }
}
