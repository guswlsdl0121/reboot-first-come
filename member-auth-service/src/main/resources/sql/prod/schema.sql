CREATE TABLE IF NOT EXISTS `member`
(
    `member_id`             INT AUTO_INCREMENT                    NOT NULL PRIMARY KEY,
    `name`                  VARCHAR(255)                          NOT NULL COMMENT '이름',
    `password`              VARCHAR(255)                          NOT NULL COMMENT '비밀번호',
    `email`                 VARCHAR(255)                          NOT NULL COMMENT '이메일 주소',
    `phone`                 VARCHAR(255)                          NOT NULL COMMENT '전화번호',
    `address`               VARCHAR(255)                          NOT NULL COMMENT '주소',
    `last_password_updated` DATETIME(6)                           NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    `role`                  ENUM ('ROLE_UNVERIFIED', 'ROLE_USER') NOT NULL DEFAULT 'ROLE_UNVERIFIED',
    `created_at`            DATETIME(6)                           NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    `updated_at`            DATETIME(6)                           NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(6),
    UNIQUE KEY `uk_email` (`email`) COMMENT '이메일 중복 방지'
);