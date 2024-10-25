-- 7번 주문을 반품 가능한 상태로 업데이트
-- DELIVERED 상태이며, 배송 완료(updated_at)가 1일 이내인 상태로 설정
UPDATE `order`
SET status     = 'DELIVERED',
    created_at = DATE_SUB(NOW(), INTERVAL 2 DAY),  -- 주문 생성일
    updated_at = DATE_SUB(NOW(), INTERVAL 12 HOUR) -- 배송 완료일 (12시간 전)
WHERE order_id = 7;