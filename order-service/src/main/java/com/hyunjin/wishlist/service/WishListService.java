package com.hyunjin.wishlist.service;

import com.hyunjin.wishlist.api.dto.request.WishlistUpdateType;
import com.hyunjin.wishlist.api.dto.response.WishlistItemDTO;
import com.hyunjin.wishlist.api.dto.response.WishlistMainResponse;
import com.hyunjin.wishlist.api.dto.response.WishlistUpdateResponse;
import com.hyunjin.wishlist.client.member.MemberServiceClient;
import com.hyunjin.wishlist.client.product.ProductResponse;
import com.hyunjin.wishlist.client.product.ProductServiceClient;
import com.hyunjin.wishlist.entity.Wishlist;
import com.hyunjin.wishlist.repository.WishListRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishListService {
    private static final int INITIAL_CURSOR = Integer.MAX_VALUE;

    private final MemberServiceClient memberClient;
    private final ProductServiceClient productClient;
    private final WishListRepository wishListRepository;

    @Transactional
    public Integer createWishList(Integer memberId, Integer productId) {
        // 회원 검증
        memberClient.getMember(memberId);
        // 상품 검증
        productClient.getProduct(productId);

        if (wishListRepository.existsByMemberIdAndProductId(memberId, productId)) {
            throw new com.hyunjin.firstcome_system.wishlist.exception.DuplicatedException("이미 위시리스트에 존재하는 상품입니다.");
        }

        Wishlist wishlist = Wishlist.builder()
                .memberId(memberId)
                .productId(productId)
                .quantity(1)
                .build();

        return wishListRepository.save(wishlist).getId();
    }

    @Transactional(readOnly = true)
    public WishlistMainResponse getWishList(Integer memberId, String cursor, int size) {
        // 회원 검증
        memberClient.getMember(memberId);

        int cursorValue = determineCursor(cursor);
        List<Integer> wishlistIds = wishListRepository.getWishlistIds(memberId, cursorValue, size);

        if (wishlistIds.isEmpty()) {
            return WishlistMainResponse.builder()
                    .items(Collections.emptyList())
                    .nextCursor(null)
                    .build();
        }

        List<Wishlist> wishlists = wishListRepository.findByIds(wishlistIds);
        List<WishlistItemDTO> items = wishlists.stream()
                .map(w -> {
                    ProductResponse product = productClient.getProduct(w.getProductId());
                    return new WishlistItemDTO(
                            w.getId(),
                            product.getId(),
                            product.getName(),
                            product.getPrice(),
                            w.getQuantity()
                    );
                })
                .collect(Collectors.toList());

        String nextCursor = getNextCursor(size, wishlistIds);

        return WishlistMainResponse.builder()
                .items(items)
                .nextCursor(nextCursor)
                .build();
    }

    private int determineCursor(String cursor) {
        if (cursor == null) {
            return INITIAL_CURSOR;
        }
        return Integer.parseInt(cursor);
    }

    private String getNextCursor(int requestedSize, List<Integer> items) {
        if (items.size() != requestedSize) {
            return null;
        }
        return String.valueOf(items.getLast());
    }

    @Transactional
    public Integer deleteWishlist(Integer wishlistId) {
        Wishlist wishlist = wishListRepository.findById(wishlistId)
                .orElseThrow(() -> new EntityNotFoundException("위시리스트를 찾을 수 없습니다."));

        wishListRepository.delete(wishlist);
        return wishlistId;
    }

    @Transactional
    public WishlistUpdateResponse updateWishlistQuantity(Integer wishlistId, WishlistUpdateType updateType) {
        Wishlist wishlist = wishListRepository.findById(wishlistId)
                .orElseThrow(() -> new EntityNotFoundException("위시리스트를 찾을 수 없습니다."));

        switch (updateType) {
            case INCREASE -> wishlist.increaseQuantity();
            case DECREASE -> wishlist.decreaseQuantity();
        }

        wishListRepository.save(wishlist);

        return WishlistUpdateResponse.builder()
                .wishlistId(wishlist.getId())
                .quantity(wishlist.getQuantity())
                .build();
    }
}