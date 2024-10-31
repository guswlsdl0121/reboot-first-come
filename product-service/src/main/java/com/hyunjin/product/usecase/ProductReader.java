package com.hyunjin.product.usecase;


import com.hyunjin.product.dto.response.ProductItemDTO;
import com.hyunjin.product.dto.response.ProductMainResponse;
import com.hyunjin.product.repository.ProductRepository;
import com.hyunjin.product.utils.CursorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductReader {
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public ProductMainResponse getMainList(String cursor, int size) {
        int cursorValue = CursorUtils.determineCursor(cursor);
        List<ProductItemDTO> products = productRepository.getProducts(size, cursorValue);
        String nextCursor = CursorUtils.getNextCursor(size, products, ProductItemDTO::getId);

        return ProductMainResponse.builder()
                .items(products)
                .nextCursor(nextCursor)
                .build();
    }


    //TODO: 주문을 생성할 때 상품을 조회하는 부분을 다른 API로 파야한다.
//    @Transactional(readOnly = true)
//    public OrderProductMap getOrderProduct(List<OrderCreateItem> orderItems) {
//        // 1. 상품 정보 조회
//        List<Product> products = productRepository.findAllByIdIn(
//                orderItems.stream()
//                        .map(OrderCreateItem::getProductId)
//                        .toList()
//        );
//
//        OrderProductMap orderProductMap = OrderProductMap.from(products);
//        orderProductMap.validateProductsExist(orderItems);
//
//        return orderProductMap;
//    }
}