package com.hyunjin.firstcome_system.order.orderstock.repository;

public interface StockRepository {
    /**
     * 상품의 현재 재고 조회
     * Cache-Aside 패턴으로 구현
     *
     * @throws IllegalArgumentException 상품이 존재하지 않는 경우
     */
    int getStock(Integer productId);

    /**
     * 재고 감소 시도
     *
     * @return 재고 감소 성공 여부
     */
    boolean decrease(Integer productId, int quantity);

    /**
     * 재고 증가 (주문 취소, 반품 등)
     */
    void increase(Integer productId, int quantity);

    /**
     * 재고 초기화 또는 설정
     */
    void initialize(Integer productId, int quantity);

    /**
     * 특정 상품의 캐시 제거
     */
    void evictCache(Integer productId);

    /**
     * 전체 캐시 초기화
     */
    void clearCache();
}