-- 1. ENUM 타입 수정 (새로운 상태값 추가)
ALTER TABLE `order` MODIFY COLUMN `status` ENUM(
    'PENDING',
    'PROCESSING',
    'SHIPPED',
    'DELIVERED',
    'CANCELLED',
    'RETURNED',  -- 기존 값 유지
    'RETURN_REQUESTED',
    'RETURN_COMPLETED'
    ) NOT NULL COMMENT '주문 상태 (대기중, 처리중, 배송됨, 배달완료, 취소됨, 반품됨, 반품요청, 반품완료)';

-- 2. 기존 RETURNED 상태를 RETURN_COMPLETED로 변환
UPDATE `order` SET status = 'RETURN_COMPLETED' WHERE status = 'RETURNED';

-- 3. 최종 ENUM 타입 수정 (RETURNED 제거)
ALTER TABLE `order` MODIFY COLUMN `status` ENUM(
    'PENDING',
    'PROCESSING',
    'SHIPPED',
    'DELIVERED',
    'CANCELLED',
    'RETURN_REQUESTED',
    'RETURN_COMPLETED'
    ) NOT NULL COMMENT '주문 상태 (대기중, 처리중, 배송됨, 배달완료, 취소됨, 반품요청, 반품완료)';