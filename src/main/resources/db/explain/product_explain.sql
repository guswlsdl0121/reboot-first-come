-- 제품 데이터 삽입
INSERT INTO product (name, description, price, quantity) VALUES ('Product 1', 'Description 1', 10000.00, 100);
INSERT INTO product (name, description, price, quantity) VALUES ('Product 2', 'Description 2', 20000.00, 200);

-- 삽입된 데이터 확인
SELECT * FROM product;

-- 커서 기반 페이지네이션 쿼리에 대한 EXPLAIN
EXPLAIN
SELECT p.product_id, p.name, p.price
FROM product p
WHERE p.product_id < 1000
ORDER BY p.product_id DESC
LIMIT 10;