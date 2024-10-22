package com.reboot_course.firstcome_system.order.dto.request.create;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class OrderCreateRequest {
    private List<OrderCreateItem> products;
}
