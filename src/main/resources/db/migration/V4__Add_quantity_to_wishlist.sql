ALTER TABLE `wishlist`
    ADD COLUMN `quantity` INT NOT NULL DEFAULT 1 COMMENT '위시리스트 상품 수량';

-- 기존 데이터에 대해 기본값 1 설정
UPDATE `wishlist` SET `quantity` = 1 WHERE `quantity` IS NULL;

-- NOT NULL 제약조건 추가
ALTER TABLE `wishlist` MODIFY COLUMN `quantity` INT NOT NULL;