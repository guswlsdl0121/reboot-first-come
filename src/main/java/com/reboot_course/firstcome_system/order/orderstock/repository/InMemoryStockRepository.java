package com.reboot_course.firstcome_system.order.orderstock.repository;

import com.reboot_course.firstcome_system.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Repository
@RequiredArgsConstructor
public class InMemoryStockRepository implements StockRepository {
    private final ProductRepository productRepository;
    private final ReentrantLock lock = new ReentrantLock();
    private final ConcurrentHashMap<Integer, Integer> stockMap = new ConcurrentHashMap<>();

    @Override
    public int getStock(Integer productId) {
        // 1. 캐시 확인
        Integer cachedStock = stockMap.get(productId);
        if (cachedStock != null) {
            log.debug("재고 캐시 히트 - productId: {}, stock: {}", productId, cachedStock);
            return cachedStock;
        }

        // 2. 캐시 미스: DB에서 조회
        log.debug("재고 캐시 미스 - productId: {}", productId);
        return productRepository.findById(productId)
                .map(product -> {
                    int stock = product.getStock();
                    stockMap.put(productId, stock);
                    log.debug("재고 캐시 로드 완료 - productId: {}, stock: {}", productId, stock);
                    return stock;
                })
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다: " + productId));
    }

    @Override
    @Transactional
    public boolean decrease(Integer productId, int quantity) {
        lock.lock();
        try {
            int currentStock = getStock(productId);
            if (currentStock < quantity) {
                log.debug("재고 부족 - productId: {}, 현재 재고: {}, 요청 수량: {}",
                        productId, currentStock, quantity);
                return false;
            }

            int updatedRows = productRepository.decreaseStock(productId, quantity);
            if (updatedRows != 1) {
                log.debug("재고 감소 실패 - productId: {}, quantity: {}", productId, quantity);
                return false;
            }

            stockMap.put(productId, currentStock - quantity);
            log.debug("재고 감소 성공 - productId: {}, 새로운 재고: {}",
                    productId, currentStock - quantity);
            return true;

        } catch (Exception e) {
            log.error("재고 감소 중 예외 발생 - productId: {}, quantity: {}", productId, quantity, e);
            return false;
        } finally {
            lock.unlock();
        }
    }

    @Override
    @Transactional
    public void increase(Integer productId, int quantity) {
        // 기본적으로 재고 증가는 음수가 될 일도 없고, 경합 상황도 적음
        // 따라서 별도의 락을 얻어서 하진 않고, concurrentHashmap으로도 처리가 가능하다고 판단
        int currentStock = getStock(productId);
        productRepository.increaseStock(productId, quantity);
        stockMap.put(productId, currentStock + quantity);
    }

    @Override
    @Transactional
    public void initialize(Integer productId, int quantity) {
        lock.lock();
        try {
            productRepository.updateStock(productId, quantity);
            stockMap.put(productId, quantity);
            log.debug("재고 초기화 성공 - productId: {}, quantity: {}", productId, quantity);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void evictCache(Integer productId) {
        stockMap.remove(productId);
        log.debug("재고 캐시 제거 - productId: {}", productId);
    }

    @Override
    public void clearCache() {
        stockMap.clear();
        log.info("전체 재고 캐시 초기화 완료");
    }
}
