-- V4__Alter_member_role_column.sql
ALTER TABLE member
    MODIFY COLUMN role ENUM ('ROLE_UNVERIFIED', 'ROLE_USER') NOT NULL DEFAULT 'ROLE_UNVERIFIED';

-- 기존 데이터의 role 값이 일치하지 않는 경우를 위한 업데이트
UPDATE member
SET role = 'ROLE_UNVERIFIED'
WHERE role = 'ROLE_UNVERIFIED';

UPDATE member
SET role = 'ROLE_USER'
WHERE role = 'ROLE_USER';