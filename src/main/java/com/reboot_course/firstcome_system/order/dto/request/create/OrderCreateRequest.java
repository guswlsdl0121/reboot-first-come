package com.reboot_course.firstcome_system.order.dto.request.create;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class OrderCreateRequest {
    @NotNull
    @NotEmpty
    private List<OrderCreateItem> products;
}
