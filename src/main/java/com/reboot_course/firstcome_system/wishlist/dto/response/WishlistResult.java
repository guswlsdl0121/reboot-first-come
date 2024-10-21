package com.reboot_course.firstcome_system.wishlist.dto.response;

import java.util.List;


public record WishlistResult(List<Integer> ids, String nextCursor) {
}
