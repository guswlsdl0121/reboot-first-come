package com.reboot_course.firstcome_system.product.repository;

import com.reboot_course.firstcome_system.factory.TestProductFactory;
import com.reboot_course.firstcome_system.product.dto.response.ProductMain;
import com.reboot_course.firstcome_system.product.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Import(TestProductFactory.class)
@DisplayName("Product Repository 테스트")
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TestProductFactory testProductFactory;

    private List<Product> testProducts;

    @BeforeEach
    void setUp() {
        testProductFactory.clearProducts();
        testProducts = testProductFactory.createMultipleProducts(5);
    }

    @Test
    @DisplayName("상품 목록 조회: 커서 없음")
    void testGetProducts() {
        // When
        List<ProductMain> result = productRepository.getProducts(3, Integer.MAX_VALUE);

        // Then (마지막에 넣었던 것 부터, 최신순 정렬로 나와야 함)
        assertThat(result).hasSize(3);
        assertThat(result.get(0).id()).isEqualTo(testProducts.get(4).getId());
        assertThat(result.get(1).id()).isEqualTo(testProducts.get(3).getId());
        assertThat(result.get(2).id()).isEqualTo(testProducts.get(2).getId());
    }

    @Test
    @DisplayName("상품 목록 조회: 커서 사용")
    void testGetProductsWithCursor() {
        // When
        List<ProductMain> result = productRepository.getProducts(2, testProducts.get(3).getId());

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).id()).isEqualTo(testProducts.get(2).getId());
        assertThat(result.get(1).id()).isEqualTo(testProducts.get(1).getId());
    }

    @Test
    @DisplayName("ID로 상품 조회: 존재하는 상품")
    void testFindById() {
        // Given
        Product testProduct = testProducts.get(0);

        // When
        Optional<Product> found = productRepository.findById(testProduct.getId());

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(testProduct.getId());
    }

    @Test
    @DisplayName("ID로 상품 조회: 존재하지 않는 상품")
    void testFindByIdNotFound() {
        // When
        Optional<Product> found = productRepository.findById(999);

        // Then
        assertThat(found).isEmpty();
    }
}