package com.hyunjin.wishlist.api.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class WishlistMainResponse {
    private List<WishlistItemDTO> items;
    private String nextCursor;
}
