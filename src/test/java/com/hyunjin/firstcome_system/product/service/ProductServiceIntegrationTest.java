package com.hyunjin.firstcome_system.product.service;

import com.hyunjin.firstcome_system.factory.TestProductFactory;
import com.hyunjin.firstcome_system.product.config.ProductTestConfig;
import com.hyunjin.firstcome_system.product.dto.response.ProductDetailResponse;
import com.hyunjin.firstcome_system.product.dto.response.ProductMainResponse;
import com.hyunjin.firstcome_system.product.entity.Product;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Import({ProductService.class, TestProductFactory.class, ProductTestConfig.class})
@DisplayName("Product Service 통합 테스트")
class ProductServiceIntegrationTest {
    @Autowired
    private ProductService productService;

    @Autowired
    private TestProductFactory testProductFactory;

    private List<Product> testProducts;

    @BeforeEach
    void setUp() {
        testProductFactory.clearProducts();
        testProducts = testProductFactory.createMultipleProducts(5);
    }

    @Test
    @DisplayName("상품 목록 조회: 초기 커서")
    void testGetProductListWithInitialCursor() {
        // When
        ProductMainResponse response = productService.getProductList(null, 3);

        // Then
        assertThat(response.items()).hasSize(3);
        assertThat(response.items().get(0).getId()).isEqualTo(testProducts.get(4).getId());
        assertThat(response.items().get(1).getId()).isEqualTo(testProducts.get(3).getId());
        assertThat(response.items().get(2).getId()).isEqualTo(testProducts.get(2).getId());
        assertThat(response.nextCursor()).isEqualTo(testProducts.get(2).getId().toString());
    }

    @Test
    @DisplayName("상품 목록 조회: 중간 커서")
    void testGetProductListWithMiddleCursor() {
        // When
        ProductMainResponse response = productService.getProductList(testProducts.get(3).getId().toString(), 2);

        // Then
        assertThat(response.items()).hasSize(2);
        assertThat(response.items().get(0).getId()).isEqualTo(testProducts.get(2).getId());
        assertThat(response.items().get(1).getId()).isEqualTo(testProducts.get(1).getId());
        assertThat(response.nextCursor()).isEqualTo(testProducts.get(1).getId().toString());
    }

    @Test
    @DisplayName("상품 목록 조회: 마지막 페이지")
    void testGetProductListLastPage() {
        // When
        ProductMainResponse response = productService.getProductList(testProducts.get(1).getId().toString(), 2);

        // Then
        assertThat(response.items()).hasSize(1);
        assertThat(response.items().getFirst().getId()).isEqualTo(testProducts.get(0).getId());
        assertThat(response.nextCursor()).isNull();
    }

    @Test
    @DisplayName("상품 상세 조회: 존재하는 상품")
    void testGetProductDetail() {
        // Given
        Product testProduct = testProducts.getFirst();

        // When
        ProductDetailResponse response = productService.getProductDetail(testProduct.getId());

        // Then
        assertThat(response.productId()).isEqualTo(testProduct.getId().longValue());
    }

    @Test
    @DisplayName("상품 상세 조회: 존재하지 않는 상품")
    void testGetProductDetailNotFound() {
        // When & Then
        assertThatThrownBy(() -> productService.getProductDetail(999))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("해당 상품을 찾을 수 없습니다. (productId : 999)");
    }
}