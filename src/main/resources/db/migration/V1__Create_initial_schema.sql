-- Member 테이블 생성
CREATE TABLE IF NOT EXISTS `member`
(
    `member_id`  INT          NOT NULL AUTO_INCREMENT,
    `name`       VARCHAR(255) NOT NULL COMMENT '이름',
    `password`   VARCHAR(255) NOT NULL COMMENT '비밀번호',
    `email`      VARCHAR(255) NOT NULL COMMENT '이메일 주소',
    `phone`      VARCHAR(255) NOT NULL COMMENT '전화번호',
    `address`    VARCHAR(255) NOT NULL COMMENT '주소',
    `created_at` DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    `updated_at` DATETIME(6)  NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (`member_id`),
    UNIQUE KEY `uk_email` (`email`) COMMENT '이메일 중복 방지'
);

-- Product 테이블 생성
CREATE TABLE IF NOT EXISTS `product`
(
    `product_id`  INT            NOT NULL AUTO_INCREMENT,
    `name`        VARCHAR(255)   NOT NULL COMMENT '상품명',
    `description` VARCHAR(255) COMMENT '상품 설명',
    `price`       DECIMAL(10, 2) NOT NULL COMMENT '상품 가격',
    `quantity`    INT            NOT NULL COMMENT '재고 수량',
    `created_at`  DATETIME(6)    NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    `updated_at`  DATETIME(6)    NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (`product_id`)
);

-- Order 테이블 생성
CREATE TABLE IF NOT EXISTS `order`
(
    `order_id`     INT                                                                             NOT NULL AUTO_INCREMENT,
    `total_amount` DECIMAL(10, 2)                                                                  NOT NULL COMMENT '총 주문 액수',
    `status`       ENUM ('PENDING', 'PROCESSING', 'SHIPPED', 'DELIVERED', 'CANCELLED', 'RETURNED') NOT NULL COMMENT '주문 상태 (대기중, 처리중, 배송됨, 배달완료, 취소됨, 반품됨)',
    `created_at`   DATETIME(6)                                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    `updated_at`   DATETIME(6)                                                                     NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(6),
    `member_id`    INT                                                                             NOT NULL COMMENT '주문한 회원의 ID',
    PRIMARY KEY (`order_id`)
);

-- Order_Product 테이블 생성
CREATE TABLE IF NOT EXISTS `order_product`
(
    `order_product_id` INT            NOT NULL AUTO_INCREMENT,
    `quantity`         INT            NOT NULL COMMENT '주문 상품 한개에 대한 수량',
    `price`            DECIMAL(10, 2) NOT NULL COMMENT '주문 당시 상품 가격',
    `created_at`       DATETIME(6)    NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    `updated_at`       DATETIME(6)    NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(6),
    `order_id`         INT            NOT NULL COMMENT '주문 ID',
    `product_id`       INT            NOT NULL COMMENT '상품 ID',
    PRIMARY KEY (`order_product_id`)
);

-- Wishlist 테이블 생성
CREATE TABLE IF NOT EXISTS `wishlist`
(
    `wishlist_id` INT         NOT NULL AUTO_INCREMENT,
    `created_at`  DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    `updated_at`  DATETIME(6) NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(6),
    `member_id`   INT         NOT NULL,
    `product_id`  INT         NOT NULL,
    PRIMARY KEY (`wishlist_id`),
    UNIQUE KEY `uk_member_product` (`member_id`, `product_id`) COMMENT '회원별 상품 중복 등록 방지를 위한 유니크 키'
);