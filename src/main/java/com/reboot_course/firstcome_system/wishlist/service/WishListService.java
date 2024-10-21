package com.reboot_course.firstcome_system.wishlist.service;

import com.reboot_course.firstcome_system.member.entity.Member;
import com.reboot_course.firstcome_system.member.usecase.MemberFinder;
import com.reboot_course.firstcome_system.product.entity.Product;
import com.reboot_course.firstcome_system.product.usecase.ProductFinder;
import com.reboot_course.firstcome_system.wishlist.dto.request.WishlistUpdateType;
import com.reboot_course.firstcome_system.wishlist.dto.response.WishlistItemDTO;
import com.reboot_course.firstcome_system.wishlist.dto.response.WishlistResponse;
import com.reboot_course.firstcome_system.wishlist.dto.response.WishlistResult;
import com.reboot_course.firstcome_system.wishlist.entity.Wishlist;
import com.reboot_course.firstcome_system.wishlist.usecase.WishlistAppender;
import com.reboot_course.firstcome_system.wishlist.usecase.WishlistModifier;
import com.reboot_course.firstcome_system.wishlist.usecase.WishlistReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WishListService {
    private final MemberFinder memberFinder;
    private final ProductFinder productFinder;
    private final WishlistAppender wishlistAppender;
    private final WishlistReader wishlistReader;
    private final WishlistModifier wishlistModifier;

    public Integer createWishList(String email, Integer productId) {
        Member member = memberFinder.fetchByEmail(email);
        Product product = productFinder.fetchById(productId);

        Wishlist newWishList = wishlistAppender.validateUniqueAndSave(member, product);

        return newWishList.getId();
    }

    public WishlistResponse getWishList(String email, String cursor, int size) {
        Member member = memberFinder.fetchByEmail(email);

        WishlistResult result = wishlistReader.getIdsForPagination(member.getId(), cursor, size);
        List<WishlistItemDTO> wishlistItems = wishlistReader.getItemByIds(result.ids());

        return WishlistResponse.builder()
                .items(wishlistItems)
                .nextCursor(result.nextCursor())
                .build();
    }

    public Integer deleteWishlist(Integer wishlistId) {
        return wishlistModifier.removeWishlist(wishlistId);
    }

    public void updateWishlistQuantity(Integer wishlistId, WishlistUpdateType updateType) {
        wishlistModifier.updateQuantity(wishlistId, updateType);
    }
}
