package com.reboot_course.firstcome_system.wishlist.service;

import com.reboot_course.firstcome_system.common.exception.exception.DuplicatedException;
import com.reboot_course.firstcome_system.member.entity.Member;
import com.reboot_course.firstcome_system.member.usecase.MemberFinder;
import com.reboot_course.firstcome_system.product.entity.Product;
import com.reboot_course.firstcome_system.product.usecase.ProductFinder;
import com.reboot_course.firstcome_system.wishlist.dto.response.ProductWishlistResponse;
import com.reboot_course.firstcome_system.wishlist.dto.WishlistUpdateType;
import com.reboot_course.firstcome_system.wishlist.entity.Wishlist;
import com.reboot_course.firstcome_system.wishlist.repository.WishListRepository;
import com.reboot_course.firstcome_system.wishlist.usecase.WishlistAppender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WishListService {
    private final MemberFinder memberFinder;
    private final ProductFinder productFinder;
    private final WishlistAppender wishlistAppender;
    private final WishListRepository wishListRepository;


    public Integer createWishList(String email, Integer productId) {
        Member member = memberFinder.fetchByEmail(email);
        Product product = productFinder.fetchById(productId);
        Wishlist newWishList = wishlistAppender.validateUniqueAndSave(member, product);

        return newWishList.getId();
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
