package com.reboot_course.firstcome_system.order.order.dto.request.create;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class OrderCreateRequest {
    @NotNull
    @NotEmpty
    private List<OrderCreateItem> products;
}
