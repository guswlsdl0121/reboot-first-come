-- 커서 기반 페이지네이션을 위한 내림차순 인덱스 추가
CREATE INDEX idx_product_id_desc ON product (product_id DESC);