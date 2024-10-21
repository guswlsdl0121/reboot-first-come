package com.reboot_course.firstcome_system.wishlist.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class WishlistResponse {
    private List<WishlistItemDTO> items;
    private String nextCursor;
}
