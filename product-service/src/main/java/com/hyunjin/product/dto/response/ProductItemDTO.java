package com.hyunjin.product.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class ProductItemDTO {
    private final Integer id;
    private final String name;
    private final BigDecimal price;
}