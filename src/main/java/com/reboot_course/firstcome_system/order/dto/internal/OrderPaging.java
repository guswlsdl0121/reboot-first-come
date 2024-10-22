package com.reboot_course.firstcome_system.order.dto.internal;

import lombok.Builder;

import java.util.List;


@Builder
public record OrderPaging(List<Integer> ids, String nextCursor) {
}