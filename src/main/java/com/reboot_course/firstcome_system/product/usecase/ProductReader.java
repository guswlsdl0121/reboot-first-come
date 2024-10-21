package com.reboot_course.firstcome_system.product.usecase;

import com.reboot_course.firstcome_system.product.dto.response.ProductMain;
import com.reboot_course.firstcome_system.product.dto.response.ProductMainResponse;
import com.reboot_course.firstcome_system.product.repository.ProductRepository;
import com.reboot_course.firstcome_system.product.utils.CursorUtils;
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
        List<ProductMain> products = productRepository.getProducts(size, cursorValue);
        String nextCursor = CursorUtils.getNextCursor(size, products, ProductMain::id);

        return ProductMainResponse.builder()
                .products(products)
                .nextCursor(nextCursor)
                .build();
    }
}