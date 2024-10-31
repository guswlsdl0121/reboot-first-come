package com.hyunjin.firstcome_system.order.order.dto.request.create;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderCreateItem {
    @NotNull
    private Integer productId;

    @Min(1)
    private Integer quantity;
}