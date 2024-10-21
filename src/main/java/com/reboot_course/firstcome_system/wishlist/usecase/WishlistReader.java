package com.reboot_course.firstcome_system.wishlist.usecase;

import com.reboot_course.firstcome_system.wishlist.dto.response.WishlistResult;
import com.reboot_course.firstcome_system.common.utils.CursorUtils;
import com.reboot_course.firstcome_system.wishlist.dto.response.WishlistItemDTO;
import com.reboot_course.firstcome_system.wishlist.entity.Wishlist;
import com.reboot_course.firstcome_system.wishlist.repository.WishListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class WishlistReader {
    private final WishListRepository wishListRepository;

    @Transactional(readOnly = true)
    public WishlistResult getIdsForPagination(Integer memberId, String cursor, int size) {
        int cursorValue = CursorUtils.determineCursor(cursor);
        List<Integer> wishlistIds = wishListRepository.getWishlistIds(memberId, cursorValue, size);
        String nextCursor = CursorUtils.getNextCursor(size, wishlistIds, id -> id);

        return new WishlistResult(wishlistIds, nextCursor);
    }

    @Transactional(readOnly = true)
    public List<WishlistItemDTO> getItemByIds(List<Integer> wishlistIds) {
        if (wishlistIds.isEmpty()) {
            return Collections.emptyList();
        }

        List<Wishlist> wishlists = wishListRepository.findByIdsWithProduct(wishlistIds);

        return wishlists.stream()
                .map(w -> new WishlistItemDTO(
                        w.getId(),
                        w.getProductId(),
                        w.getProductName(),
                        w.getProductPrice(),
                        w.getQuantity())
                ).collect(Collectors.toList());
    }
}