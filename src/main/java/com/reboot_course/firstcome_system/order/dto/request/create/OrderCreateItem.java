package com.reboot_course.firstcome_system.order.dto.request.create;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderCreateItem {
    private Integer productId;
    private Integer quantity;
}