-- V9__Insert_order_dummy_data.sql

-- Order 데이터 삽입
INSERT INTO `order` (member_id, total_amount, status, created_at)
VALUES
    -- 최근 주문 (오늘)
    (1, 4119000.00, 'PENDING', DATE_SUB(NOW(), INTERVAL 0 HOUR)),
    (1, 3849000.00, 'PENDING', DATE_SUB(NOW(), INTERVAL 2 HOUR)),

    -- 어제 주문
    (1, 2279000.00, 'PROCESSING', DATE_SUB(NOW(), INTERVAL 1 DAY)),
    (1, 5289000.00, 'PROCESSING', DATE_SUB(NOW(), INTERVAL 1 DAY)),

    -- 2일 전 주문
    (1, 929000.00, 'SHIPPED', DATE_SUB(NOW(), INTERVAL 2 DAY)),
    (1, 4319000.00, 'SHIPPED', DATE_SUB(NOW(), INTERVAL 2 DAY)),

    -- 3일 전 주문
    (1, 3690000.00, 'DELIVERED', DATE_SUB(NOW(), INTERVAL 3 DAY)),
    (1, 2339000.00, 'DELIVERED', DATE_SUB(NOW(), INTERVAL 3 DAY)),

    -- 4일 전 주문
    (1, 1718000.00, 'CANCELLED', DATE_SUB(NOW(), INTERVAL 4 DAY)),
    (1, 4900000.00, 'CANCELLED', DATE_SUB(NOW(), INTERVAL 4 DAY)),

    -- 5일 전 주문
    (1, 788000.00, 'RETURNED', DATE_SUB(NOW(), INTERVAL 5 DAY)),
    (1, 1288000.00, 'RETURNED', DATE_SUB(NOW(), INTERVAL 5 DAY)),

    -- 1주일 전 주문
    (1, 2879000.00, 'DELIVERED', DATE_SUB(NOW(), INTERVAL 7 DAY)),
    (1, 3919000.00, 'DELIVERED', DATE_SUB(NOW(), INTERVAL 7 DAY)),

    -- 2주일 전 주문
    (1, 5639000.00, 'DELIVERED', DATE_SUB(NOW(), INTERVAL 14 DAY)),
    (1, 1847000.00, 'RETURNED', DATE_SUB(NOW(), INTERVAL 14 DAY)),

    -- 3주일 전 주문
    (1, 2190000.00, 'DELIVERED', DATE_SUB(NOW(), INTERVAL 21 DAY)),
    (1, 3779000.00, 'DELIVERED', DATE_SUB(NOW(), INTERVAL 21 DAY)),

    -- 1달 전 주문
    (1, 4990000.00, 'DELIVERED', DATE_SUB(NOW(), INTERVAL 1 MONTH)),
    (1, 2549000.00, 'DELIVERED', DATE_SUB(NOW(), INTERVAL 1 MONTH)),

    -- 2달 전 주문
    (1, 1879000.00, 'DELIVERED', DATE_SUB(NOW(), INTERVAL 2 MONTH)),
    (1, 3088000.00, 'DELIVERED', DATE_SUB(NOW(), INTERVAL 2 MONTH)),

    -- 3달 전 주문
    (1, 4479000.00, 'DELIVERED', DATE_SUB(NOW(), INTERVAL 3 MONTH)),
    (1, 2678000.00, 'DELIVERED', DATE_SUB(NOW(), INTERVAL 3 MONTH)),

    -- 추가 주문들
    (1, 3890000.00, 'DELIVERED', DATE_SUB(NOW(), INTERVAL 100 DAY)),
    (1, 2890000.00, 'DELIVERED', DATE_SUB(NOW(), INTERVAL 101 DAY)),
    (1, 1890000.00, 'DELIVERED', DATE_SUB(NOW(), INTERVAL 102 DAY)),
    (1, 5900000.00, 'DELIVERED', DATE_SUB(NOW(), INTERVAL 103 DAY)),
    (1, 1390000.00, 'DELIVERED', DATE_SUB(NOW(), INTERVAL 104 DAY)),
    (1, 3490000.00, 'DELIVERED', DATE_SUB(NOW(), INTERVAL 105 DAY)),
    (1, 2990000.00, 'DELIVERED', DATE_SUB(NOW(), INTERVAL 106 DAY));

-- Order Product 데이터 삽입
INSERT INTO order_product (order_id, product_id, quantity, price, created_at)
VALUES
    -- 오늘 주문 상품
    (1, 1, 1, 3690000.00, DATE_SUB(NOW(), INTERVAL 0 HOUR)),   -- MacBook Pro
    (1, 4, 1, 429000.00, DATE_SUB(NOW(), INTERVAL 0 HOUR)),    -- Sony WH-1000XM5

    (2, 7, 1, 4990000.00, DATE_SUB(NOW(), INTERVAL 2 HOUR)),   -- Canon EOS R5
    (2, 10, 2, 359000.00, DATE_SUB(NOW(), INTERVAL 2 HOUR)),   -- AirPods Pro 2

    -- 어제 주문 상품
    (3, 5, 1, 929000.00, DATE_SUB(NOW(), INTERVAL 1 DAY)),     -- iPad Air
    (3, 9, 1, 399000.00, DATE_SUB(NOW(), INTERVAL 1 DAY)),     -- Galaxy Watch 6
    (3, 17, 1, 489000.00, DATE_SUB(NOW(), INTERVAL 1 DAY)),    -- Bose 980

    (4, 3, 1, 4900000.00, DATE_SUB(NOW(), INTERVAL 1 DAY)),    -- LG OLED TV
    (4, 10, 1, 359000.00, DATE_SUB(NOW(), INTERVAL 1 DAY)),    -- AirPods Pro 2

    -- 2일 전 주문 상품
    (5, 5, 1, 929000.00, DATE_SUB(NOW(), INTERVAL 2 DAY)),     -- iPad Air

    (6, 13, 1, 598000.00, DATE_SUB(NOW(), INTERVAL 2 DAY)),    -- PS5
    (6, 15, 1, 4290000.00, DATE_SUB(NOW(), INTERVAL 2 DAY)),   -- ASUS ROG

    -- 3일 전 주문 상품
    (7, 1, 1, 3690000.00, DATE_SUB(NOW(), INTERVAL 3 DAY)),    -- MacBook Pro

    (8, 16, 1, 1190000.00, DATE_SUB(NOW(), INTERVAL 3 DAY)),   -- Apple Watch Ultra 2
    (8, 29, 1, 329000.00, DATE_SUB(NOW(), INTERVAL 3 DAY)),    -- Sony WF-1000XM5

    -- 4일 전 주문 상품 (취소됨)
    (9, 20, 4, 429000.00, DATE_SUB(NOW(), INTERVAL 4 DAY)),    -- Apple HomePod

    (10, 3, 1, 4900000.00, DATE_SUB(NOW(), INTERVAL 4 DAY)),   -- LG OLED TV

    -- 5일 전 주문 상품 (반품됨)
    (11, 4, 1, 429000.00, DATE_SUB(NOW(), INTERVAL 5 DAY)),    -- Sony WH-1000XM5
    (11, 10, 1, 359000.00, DATE_SUB(NOW(), INTERVAL 5 DAY)),   -- AirPods Pro 2

    (12, 14, 1, 1290000.00, DATE_SUB(NOW(), INTERVAL 5 DAY)),  -- Dyson V15

    -- 1주일 전 주문 상품
    (13, 35, 1, 7890000.00, DATE_SUB(NOW(), INTERVAL 7 DAY)),  -- Apple Mac Studio

    (14, 27, 1, 1890000.00, DATE_SUB(NOW(), INTERVAL 7 DAY)),  -- Apple iMac 24"
    (14, 49, 1, 769000.00, DATE_SUB(NOW(), INTERVAL 7 DAY)),   -- Apple AirPods Max

    -- 2주일 전 주문 상품
    (15, 25, 1, 5900000.00, DATE_SUB(NOW(), INTERVAL 14 DAY)),-- Samsung QLED 8K TV

    (16, 16, 1, 1190000.00, DATE_SUB(NOW(), INTERVAL 14 DAY)),-- Apple Watch Ultra 2
    (16, 29, 2, 329000.00, DATE_SUB(NOW(), INTERVAL 14 DAY)),  -- Sony WF-1000XM5

    -- 3주일 전 주문 상품
    (17, 8, 1, 2190000.00, DATE_SUB(NOW(), INTERVAL 21 DAY)),  -- LG 그램 17

    (18, 28, 1, 3890000.00, DATE_SUB(NOW(), INTERVAL 21 DAY)),-- Razer Blade 17

    -- 1달 전 주문 상품
    (19, 7, 1, 4990000.00, DATE_SUB(NOW(), INTERVAL 1 MONTH)),-- Canon EOS R5

    (20, 15, 1, 4290000.00, DATE_SUB(NOW(), INTERVAL 1 MONTH)),-- ASUS ROG

    -- 2달 전 주문 상품
    (21, 27, 1, 1890000.00, DATE_SUB(NOW(), INTERVAL 2 MONTH)),-- Apple iMac 24"

    (22, 28, 1, 3890000.00, DATE_SUB(NOW(), INTERVAL 2 MONTH)),-- Razer Blade 17

    -- 3달 전 주문 상품
    (23, 35, 1, 7890000.00, DATE_SUB(NOW(), INTERVAL 3 MONTH)),-- Apple Mac Studio

    (24, 15, 1, 4290000.00, DATE_SUB(NOW(), INTERVAL 3 MONTH)),-- ASUS ROG

    -- 추가 주문 상품들
    (25, 28, 1, 3890000.00, DATE_SUB(NOW(), INTERVAL 100 DAY)),-- Razer Blade 17

    (26, 22, 1, 2890000.00, DATE_SUB(NOW(), INTERVAL 101 DAY)),-- Sony A7 IV

    (27, 27, 1, 1890000.00, DATE_SUB(NOW(), INTERVAL 102 DAY)),-- Apple iMac 24"

    (28, 25, 1, 5900000.00, DATE_SUB(NOW(), INTERVAL 103 DAY)),-- Samsung QLED 8K TV

    (29, 26, 1, 1390000.00, DATE_SUB(NOW(), INTERVAL 104 DAY)),-- Google Pixel 8 Pro

    (30, 37, 1, 3490000.00, DATE_SUB(NOW(), INTERVAL 105 DAY)),-- Samsung 오디세이 Ark

    (31, 38, 1, 2990000.00, DATE_SUB(NOW(), INTERVAL 106 DAY));-- LG CineBeam