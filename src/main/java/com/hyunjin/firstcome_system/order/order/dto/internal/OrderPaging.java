package com.hyunjin.firstcome_system.order.order.dto.internal;

import lombok.Builder;

import java.util.List;


@Builder
public record OrderPaging(List<Integer> ids, String nextCursor) {
}