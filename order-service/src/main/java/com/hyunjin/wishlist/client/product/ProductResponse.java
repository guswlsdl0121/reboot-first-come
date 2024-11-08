package com.hyunjin.wishlist.client.product;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class ProductResponse {
    private Integer id;
    private String name;
    private BigDecimal price;
    private Integer stock;
}