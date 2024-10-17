ALTER TABLE `member`
    ADD COLUMN `last_password_updated` DATETIME(6) NULL DEFAULT CURRENT_TIMESTAMP(6);

-- 기존 레코드의 last_password_updated 값을 설정
UPDATE `member`
SET `last_password_updated` = `created_at`
WHERE `last_password_updated` IS NULL;

-- 향후 INSERT 시 자동으로 값이 설정되도록 DEFAULT 값 변경
ALTER TABLE `member`
    MODIFY COLUMN `last_password_updated` DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6);