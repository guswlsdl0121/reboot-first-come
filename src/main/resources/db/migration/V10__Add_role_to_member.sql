-- 1. role 컬럼 추가
ALTER TABLE member
    ADD COLUMN role VARCHAR(20);

-- 2. NOT NULL 제약조건 추가 전에 기존 데이터 마이그레이션
UPDATE member
SET role = 'ROLE_UNVERIFIED'
WHERE role IS NULL;

-- 3. NOT NULL 제약조건 추가
ALTER TABLE member
    MODIFY COLUMN role VARCHAR(20) NOT NULL;