package com.reboot_course.firstcome_system.product.usecase;

import com.hyunjin.common.utils.CursorUtils;
import com.reboot_course.firstcome_system.order.order.dto.request.create.OrderCreateItem;
import com.reboot_course.firstcome_system.order.order.vo.OrderProductMap;
import com.reboot_course.firstcome_system.product.dto.response.ProductItemDTO;
import com.reboot_course.firstcome_system.product.dto.response.ProductMainResponse;
import com.reboot_course.firstcome_system.product.entity.Product;
import com.reboot_course.firstcome_system.product.repository.ProductRepository;
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

    @Transactional(readOnly = true)
    public OrderProductMap getOrderProduct(List<OrderCreateItem> orderItems) {
        // 1. 상품 정보 조회
        List<Product> products = productRepository.findAllByIdIn(
                orderItems.stream()
                        .map(OrderCreateItem::getProductId)
                        .toList()
        );

        OrderProductMap orderProductMap = OrderProductMap.from(products);
        orderProductMap.validateProductsExist(orderItems);

        return orderProductMap;
    }
}