package com.reboot_course.firstcome_system.product.service;

import com.reboot_course.firstcome_system.product.dto.response.ProductDTO;
import com.reboot_course.firstcome_system.product.dto.response.ProductDetailResponse;
import com.reboot_course.firstcome_system.product.dto.response.ProductListResponse;
import com.reboot_course.firstcome_system.product.entity.Product;
import com.reboot_course.firstcome_system.product.repository.ProductRepository;
import com.reboot_course.firstcome_system.product.utils.ProductMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private static final int INITIAL_CURSOR = Integer.MAX_VALUE;

    private final ProductRepository productRepository;

    /*
     * 첫번째 페이지에선 Integer.MAX_VALUE를 넘겨주어
     * 마지막에 삽입된 데이터들을 LIMIT 만큼 가져오도록 만들어주어야 함.
     */
    private static int determineCursor(String cursor) {
        if (cursor == null) {
            return INITIAL_CURSOR;
        }
        return Integer.parseInt(cursor);
    }

    /*
     * 요청 사이즈와 반환 사이즈가 같지않다면 마지막 페이지로 판단
     * 만약 마지막 페이지라면 null을 넘김
     */
    private static String getNextCursor(int size, List<ProductDTO> products) {
        if (products.size() != size) {
            return null;
        }

        return String.valueOf(products.getLast().id());
    }

    @Transactional(readOnly = true)
    public ProductListResponse getProductList(String cursor, int size) {
        int cursorValue = determineCursor(cursor);

        List<ProductDTO> products = productRepository.getProducts(size, cursorValue);

        String nextCursor = getNextCursor(size, products);
        return ProductListResponse.builder()
                .products(products)
                .nextCursor(nextCursor)
                .build();
    }

    @Transactional(readOnly = true)
    public ProductDetailResponse getProductDetail(int productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다."));

        return ProductMapper.toProductDetailResponse(product);
    }
}