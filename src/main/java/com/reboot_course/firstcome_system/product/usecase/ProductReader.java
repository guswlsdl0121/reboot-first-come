package com.reboot_course.firstcome_system.product.usecase;

import com.reboot_course.firstcome_system.product.dto.response.ProductItemDTO;
import com.reboot_course.firstcome_system.product.dto.response.ProductMainResponse;
import com.reboot_course.firstcome_system.product.repository.ProductRepository;
import com.reboot_course.firstcome_system.common.utils.CursorUtils;
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
                .products(products)
                .nextCursor(nextCursor)
                .build();
    }
}