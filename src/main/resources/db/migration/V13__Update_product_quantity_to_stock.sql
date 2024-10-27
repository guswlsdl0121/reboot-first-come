-- Product 테이블의 quantity 컬럼을 stock으로 이름 변경
ALTER TABLE `product`
    CHANGE COLUMN `quantity` `stock` INT NOT NULL COMMENT '재고 수량';