-- V1__Create_member_table.sql
CREATE TABLE IF NOT EXISTS `member`
(
    `member_id`             INT          NOT NULL AUTO_INCREMENT,
    `name`                  VARCHAR(255) NOT NULL COMMENT '이름',
    `password`              VARCHAR(255) NOT NULL COMMENT '비밀번호',
    `email`                 VARCHAR(255) NOT NULL COMMENT '이메일 주소',
    `phone`                 VARCHAR(255) NOT NULL COMMENT '전화번호',
    `address`               VARCHAR(255) NOT NULL COMMENT '주소',
    `last_password_updated` DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    `role`                  VARCHAR(20)  NOT NULL DEFAULT 'ROLE_UNVERIFIED',
    `created_at`            DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    `updated_at`            DATETIME(6)  NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (`member_id`),
    UNIQUE KEY `uk_email` (`email`) COMMENT '이메일 중복 방지'
);

-- 초기 dummy 데이터 삽입
INSERT INTO member (name,
                    password,
                    email,
                    phone,
                    address,
                    created_at,
                    last_password_updated)
VALUES
-- 암호화된 데이터
('79736acbba57e83da4a5dac406875d75cce6c417c0961da025adb83f29a5ca20',
 '$2a$10$L7lnE/VTlY6DUuCcXyiDv.kBsGcKFleCRQ3HQcmc.oVxganDgRpZm',
 'G+uTQ0B9CTiEoUJ3onpc3jRhgYf+2IrUxxtpRoSDpeQ=',
 'cb236555f3662f2d34dfd920cb7eeb8b415f4b1391824f44e3f581619249c810',
 'ebecdcffc2f790ace974f1bd471bc21bf2e9f52b897587f96036a5a4e866bd0aa280ea318dff086e1fc9e67c48b3d2dee22720d4806ee32816b58ec251fe6741',
 CURRENT_TIMESTAMP(6),
 CURRENT_TIMESTAMP(6)),
('91465806f9352e5e720b08209a1992c6626af6a6a7b35985eacdd24f61a42202',
 '$2a$10$jDhCeAEUTvkry7tgl2sywu9rdeZZuDhB4O8bsxDhIIwTg2M/bHHmO',
 'L8mMMlXAN3rpuhgNMskA2KjGf08Lpryb/DSfgaVCmsg=',
 '29d27806385aa8f5a42c4b620df9eed275982936117cc09bf0c57406dd43f5cd',
 '6c53d5fd688db7b3221cd0448fb3f2c05e64287726701885a49b63521b0f4f89865654245b4122d79d3a79461d68a61450fbfa79e887a8ad2ddaa7ade08d3f06',
 CURRENT_TIMESTAMP(6),
 CURRENT_TIMESTAMP(6));