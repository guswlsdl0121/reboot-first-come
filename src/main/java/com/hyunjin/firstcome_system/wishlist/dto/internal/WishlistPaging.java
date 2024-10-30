package com.hyunjin.firstcome_system.wishlist.dto.internal;

import java.util.List;


public record WishlistPaging(List<Integer> ids, String nextCursor) {
}
