package com.hyunjin.wishlist.api.dto.internal;

import java.util.List;


public record WishlistPaging(List<Integer> ids, String nextCursor) {
}
