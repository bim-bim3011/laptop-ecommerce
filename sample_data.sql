-- Dữ liệu mẫu (Sample Data) cho CSDL Laptop E-commerce (Microsoft SQL Server)
-- Đã sửa lỗi trùng lặp Unique Constraint và khớp cấu trúc tự động sinh của JPA

-- =======================================================
-- 1. Thêm dữ liệu cho bảng brands (Thương hiệu)
-- =======================================================
SET IDENTITY_INSERT brands ON;
IF NOT EXISTS (SELECT 1 FROM brands WHERE brand_id = 1)
    BEGIN
        INSERT INTO brands (brand_id, brand_name, created_at, updated_at)
        VALUES
            (1, 'Apple', GETDATE(), GETDATE()),
            (2, 'Dell', GETDATE(), GETDATE()),
            (3, 'HP', GETDATE(), GETDATE()),
            (4, 'Lenovo', GETDATE(), GETDATE()),
            (5, 'Asus', GETDATE(), GETDATE()),
            (6, 'Acer', GETDATE(), GETDATE()),
            (7, 'MSI', GETDATE(), GETDATE());
    END
SET IDENTITY_INSERT brands OFF;

-- =======================================================
-- 2. Thêm dữ liệu cho bảng categories (Danh mục)
-- =======================================================
SET IDENTITY_INSERT categories ON;
IF NOT EXISTS (SELECT 1 FROM categories WHERE category_id = 1)
    BEGIN
        INSERT INTO categories (category_id, category_name, created_at, updated_at)
        VALUES
            (1, N'Gaming', GETDATE(), GETDATE()),
            (2, N'Văn Phòng', GETDATE(), GETDATE()),
            (3, N'Học Sinh - Sinh Viên', GETDATE(), GETDATE()),
            (4, N'Đồ Họa - Kỹ Thuật', GETDATE(), GETDATE()),
            (5, N'Mỏng Nhẹ Cao Cấp', GETDATE(), GETDATE());
    END
SET IDENTITY_INSERT categories OFF;

-- =======================================================
-- 3. Thêm dữ liệu cho bảng laptops (Laptop)
-- =======================================================
SET IDENTITY_INSERT laptops ON;
IF NOT EXISTS (SELECT 1 FROM laptops WHERE laptop_id = 1)
BEGIN
INSERT INTO laptops (laptop_id, laptop_name, description, brand_id, category_id, created_at, updated_at)
VALUES
    (1, 'Dell XPS 15 9530', N'Laptop doanh nhân cao cấp, thiết kế nhôm nguyên khối, màn hình OLED sắc nét, phù hợp làm việc đa nhiệm và đồ họa 2D.', 2, 5, GETDATE(), GETDATE()),
    (2, 'MacBook Pro 16 inch M3 Max', N'Cỗ máy đồ họa mạnh mẽ nhất của Apple với chip M3 Max, phù hợp cho coder, dựng phim chuyên nghiệp.', 1, 4, GETDATE(), GETDATE()),
    (3, 'Asus ROG Strix G15', N'Laptop Gaming với tản nhiệt tốt, dải đèn LED RGB cực ngầu xung quanh viền máy.', 5, 1, GETDATE(), GETDATE()),
    (4, 'HP Pavilion 14', N'Mẫu laptop giá rẻ lý tưởng cho học sinh, sinh viên với thiết kế nhỏ gọn, dễ mang theo.', 3, 3, GETDATE(), GETDATE()),
    (5, 'Lenovo ThinkPad X1 Carbon Gen 11', N'Dòng laptop doanh nhân siêu bền bỉ, trọng lượng chỉ khoảng 1.1kg, bàn phím gõ đỉnh cao.', 4, 2, GETDATE(), GETDATE()),
    (6, 'Acer Nitro 5 Tiger', N'Laptop gaming quốc dân, hiệu năng ấn tượng trong tầm giá, tản nhiệt tốt.', 6, 1, GETDATE(), GETDATE()),
    (7, 'MacBook Air M1', N'Mẫu laptop mỏng nhẹ, pin siêu trâu của Apple, luôn đứng top bán chạy.', 1, 5, GETDATE(), GETDATE());
END
SET IDENTITY_INSERT laptops OFF;

-- =======================================================
-- 4. Thêm dữ liệu cho bảng configuration_version (Cấu hình)
-- =======================================================
SET IDENTITY_INSERT configuration_version ON;
IF NOT EXISTS (SELECT 1 FROM configuration_version WHERE configuration_id = 1)
BEGIN
INSERT INTO configuration_version (configuration_id, cpu, ram, storage, gpu, price, stock_quantity, laptop_id, created_at, updated_at)
VALUES
    (1, 'Intel Core i7-13700H', '16GB DDR5', '512GB NVMe SSD', 'NVIDIA RTX 4050 6GB', 45000000, 10, 1, GETDATE(), GETDATE()),
    (2, 'Intel Core i9-13900H', '32GB DDR5', '1TB NVMe SSD', 'NVIDIA RTX 4060 8GB', 55000000, 5, 1, GETDATE(), GETDATE()),
    (3, 'Apple M3 Pro', '18GB Unified', '512GB SSD', 'Integrated 18-core GPU', 65000000, 15, 2, GETDATE(), GETDATE()),
    (4, 'Apple M3 Max', '36GB Unified', '1TB SSD', 'Integrated 30-core GPU', 85000000, 8, 2, GETDATE(), GETDATE()),
    (5, 'AMD Ryzen 7 6800H', '16GB DDR5', '512GB NVMe SSD', 'NVIDIA RTX 3050 4GB', 25000000, 20, 3, GETDATE(), GETDATE()),
    (6, 'AMD Ryzen 9 6900HX', '16GB DDR5', '1TB NVMe SSD', 'NVIDIA RTX 3060 6GB', 32000000, 10, 3, GETDATE(), GETDATE()),
    (7, 'Intel Core i5-1240P', '8GB DDR4', '256GB NVMe SSD', 'Intel Iris Xe Graphics', 15000000, 30, 4, GETDATE(), GETDATE()),
    (8, 'Intel Core i7-1355U', '16GB LPDDR5', '512GB NVMe SSD', 'Intel Iris Xe Graphics', 40000000, 12, 5, GETDATE(), GETDATE()),
    (9, 'Intel Core i5-12500H', '8GB DDR4', '512GB NVMe SSD', 'NVIDIA RTX 3050 Ti 4GB', 21000000, 25, 6, GETDATE(), GETDATE()),
    (10, 'Apple M1', '8GB Unified', '256GB SSD', 'Integrated 7-core GPU', 18500000, 50, 7, GETDATE(), GETDATE());
END
SET IDENTITY_INSERT configuration_version OFF;

-- =======================================================
-- 5. Thêm dữ liệu cho bảng role (Vai trò)
-- Sử dụng điều kiện lồng để tránh lỗi trùng lặp cột UNIQUE 'name'
-- =======================================================
SET IDENTITY_INSERT role ON;
IF NOT EXISTS (SELECT 1 FROM role WHERE id = 1 OR name = 'USER')
    INSERT INTO role (id, name) VALUES (1, 'USER');

IF NOT EXISTS (SELECT 1 FROM role WHERE id = 2 OR name = 'ADMIN')
    INSERT INTO role (id, name) VALUES (2, 'ADMIN');
SET IDENTITY_INSERT role OFF;

-- =======================================================
-- 6. Thêm dữ liệu cho bảng users (Người dùng)
-- =======================================================
SET IDENTITY_INSERT users ON;
IF NOT EXISTS (SELECT 1 FROM users WHERE user_id = 1)
BEGIN
INSERT INTO users (user_id, name, email, password, phone_number, address, status, created_at, updated_at)
VALUES
    (1, N'Quản Trị Viên', 'admin@laptopshop.com', '123456', '0987654321', N'Hà Nội', 'ACTIVE', GETDATE(), GETDATE()),
    (2, N'Khách Hàng A', 'user@laptopshop.com', '123456', '0123456789', N'Hồ Chí Minh', 'ACTIVE', GETDATE(), GETDATE());
END
SET IDENTITY_INSERT users OFF;

-- =======================================================
-- 7. Thêm liên kết vào bảng trung gian mặc định của Hibernate (user_roles)
-- =======================================================
IF EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[user_roles]') AND type in (N'U'))
BEGIN
    IF NOT EXISTS (SELECT 1 FROM user_roles WHERE user_user_id = 1 AND roles_id = 2)
        INSERT INTO user_roles (user_user_id, roles_id) VALUES (1, 2); -- ADMIN

    IF NOT EXISTS (SELECT 1 FROM user_roles WHERE user_user_id = 2 AND roles_id = 1)
        INSERT INTO user_roles (user_user_id, roles_id) VALUES (2, 1); -- USER
END

-- =======================================================
-- 8. Thêm dữ liệu cho bảng promotions (Chương trình khuyến mãi)
-- =======================================================
SET IDENTITY_INSERT promotions ON;
IF NOT EXISTS (SELECT 1 FROM promotions WHERE promotion_id = 1)
    BEGIN
        INSERT INTO promotions (promotion_id, coupon_code, title, discount_type, discount_value, min_order_value, max_discount_amount, start_date, end_date, created_at, updated_at)
        VALUES
            (1, 'SUMMER2026', N'Khuyến mãi chào hè 2026', 'PERCENTAGE', 10.00, 15000000.00, 2000000.00, '2026-06-01 00:00:00', '2026-07-31 23:59:59', GETDATE(), GETDATE()),
            (2, 'GAMERPRO', N'Giảm giá laptop Gaming', 'FIXED_AMOUNT', 1000000.00, 20000000.00, 1000000.00, '2026-07-01 00:00:00', '2026-08-31 23:59:59', GETDATE(), GETDATE()),
            (3, 'NEWUSER', N'Ưu đãi khách hàng mới', 'PERCENTAGE', 5.00, 0.00, 500000.00, '2026-01-01 00:00:00', '2026-12-31 23:59:59', GETDATE(), GETDATE());
    END
SET IDENTITY_INSERT promotions OFF;

-- =======================================================
-- 9. Thêm dữ liệu cho bảng gift_items (Danh mục quà tặng)
-- =======================================================
SET IDENTITY_INSERT gift_items ON;
IF NOT EXISTS (SELECT 1 FROM gift_items WHERE gift_item_id = 1)
    BEGIN
        INSERT INTO gift_items (gift_item_id, item_name, description, price, image_url, created_at, updated_at)
        VALUES
            (1, N'Chuột không dây Logitech G304', N'Chuột gaming không dây, độ trễ thấp, pin trâu.', 750000.00, 'https://example.com/images/logitech-g304.jpg', GETDATE(), GETDATE()),
            (2, N'Balo chống sốc Laptop 15.6 inch', N'Balo cao cấp chống nước, bảo vệ laptop an toàn.', 450000.00, 'https://example.com/images/balo-156.jpg', GETDATE(), GETDATE()),
            (3, N'Tai nghe Bluetooth Sony WH-CH520', N'Tai nghe chụp tai, âm thanh chất lượng cao.', 1200000.00, 'https://example.com/images/sony-headphone.jpg', GETDATE(), GETDATE()),
            (4, N'Lót chuột bản đồ thế giới', N'Lót chuột size lớn 80x30cm.', 150000.00, 'https://example.com/images/mousepad.jpg', GETDATE(), GETDATE());
    END
SET IDENTITY_INSERT gift_items OFF;

-- =======================================================
-- 10. Thêm dữ liệu cho bảng gift_details (Chi tiết quà tặng)
-- =======================================================
SET IDENTITY_INSERT gift_details ON;
IF NOT EXISTS (SELECT 1 FROM gift_details WHERE gift_id = 1)
    BEGIN
        INSERT INTO gift_details (gift_id, quantity, promotion_id, configuration_version_id, gift_item_id, created_at, updated_at)
        VALUES
            -- Tặng chuột Logitech (gift_item=1) khi áp dụng mã khuyến mãi 2 (GAMERPRO)
            (1, 1, 2, NULL, 1, GETDATE(), GETDATE()),

            -- Tặng lót chuột (gift_item=4) đi kèm khuyến mãi 2 (GAMERPRO)
            (2, 1, 2, NULL, 4, GETDATE(), GETDATE()),

            -- Tặng trực tiếp Balo (gift_item=2) cho cấu hình id=1 (Dell XPS 15) mà không cần mã
            (3, 1, NULL, 1, 2, GETDATE(), GETDATE()),

            -- Tặng Balo (gift_item=2) cho cấu hình id=3 (Apple M3 Pro)
            (4, 1, NULL, 3, 2, GETDATE(), GETDATE()),

            -- Tặng Tai nghe Sony (gift_item=3) và Chuột Logitech (gift_item=1) cho cấu hình id=4 (Apple M3 Max)
            (5, 1, NULL, 4, 3, GETDATE(), GETDATE()),
            (6, 1, NULL, 4, 1, GETDATE(), GETDATE());
    END
SET IDENTITY_INSERT gift_details OFF;


-- =======================================================
-- BỔ SUNG DỮ LIỆU: Bảng laptops (Thêm 5 sản phẩm mỗi hãng)
-- =======================================================
SET IDENTITY_INSERT laptops ON;

INSERT INTO laptops (laptop_id, laptop_name, description, brand_id, category_id, created_at, updated_at)
VALUES
    -- BRAND 1: Apple
    (8, 'Laptop Apple MacBook Pro 14 M2', N'Hiệu năng mạnh mẽ với chip M2 Pro, màn hình Mini-LED.', 1, 4, GETDATE(), GETDATE()),
    (9, 'Laptop Apple MacBook Air 15', N'Màn hình lớn 15 inch nhưng vẫn siêu mỏng nhẹ, pin cả ngày.', 1, 5, GETDATE(), GETDATE()),
    (10, 'Laptop Apple MacBook Pro 13 M2', N'Laptop đồ họa có quạt tản nhiệt, duy trì hiệu năng ổn định.', 1, 4, GETDATE(), GETDATE()),
    (11, 'Laptop Apple MacBook 12 Retina', N'Cực kỳ mỏng nhẹ, phù hợp mang đi lại thường xuyên.', 1, 5, GETDATE(), GETDATE()),
    (12, 'Laptop Apple MacBook Air M3', N'Chip M3 thế hệ mới nhất, thiết kế notch hiện đại.', 1, 5, GETDATE(), GETDATE()),

    -- BRAND 2: Dell
    (13, 'Laptop Dell Alienware m16', N'Cỗ máy chiến game hạng nặng, hệ thống tản nhiệt Cryo-tech.', 2, 1, GETDATE(), GETDATE()),
    (14, 'Laptop Dell Inspiron 15 3520', N'Laptop văn phòng cơ bản, màn hình 120Hz mượt mà.', 2, 3, GETDATE(), GETDATE()),
    (15, 'Laptop Dell Vostro 14 3430', N'Thiết kế cứng cáp, bảo mật tốt cho doanh nghiệp nhỏ.', 2, 2, GETDATE(), GETDATE()),
    (16, 'Laptop Dell Latitude 7440', N'Laptop doanh nhân cao cấp, vỏ hợp kim magie.', 2, 2, GETDATE(), GETDATE()),
    (17, 'Laptop Dell Precision 5480', N'Máy trạm di động siêu mỏng nhẹ cho kỹ sư đồ họa.', 2, 4, GETDATE(), GETDATE()),

    -- BRAND 3: HP
    (18, 'Laptop HP Omen 16', N'Laptop gaming cao cấp với thiết kế thanh lịch, tối giản.', 3, 1, GETDATE(), GETDATE()),
    (19, 'Laptop HP Envy x360', N'Laptop lai 2-in-1, màn hình cảm ứng lật xoay 360 độ.', 3, 5, GETDATE(), GETDATE()),
    (20, 'Laptop HP EliteBook 840 G10', N'Dòng máy doanh nhân siêu bền chuẩn quân đội.', 3, 2, GETDATE(), GETDATE()),
    (21, 'Laptop HP Victus 15', N'Laptop gaming tầm trung, thiết kế đẹp, tản nhiệt khá.', 3, 1, GETDATE(), GETDATE()),
    (22, 'Laptop HP ProBook 450 G10', N'Màn hình lớn 15.6 inch, bàn phím số tiện lợi nhập liệu.', 3, 2, GETDATE(), GETDATE()),

    -- BRAND 4: Lenovo
    (23, 'Laptop Lenovo Legion 5 Pro', N'Màn hình 2K 165Hz chuẩn màu, tản nhiệt cực mát.', 4, 1, GETDATE(), GETDATE()),
    (24, 'Laptop Lenovo IdeaPad 5', N'Vỏ nhôm sang trọng, giá sinh viên, hiệu năng ổn định.', 4, 3, GETDATE(), GETDATE()),
    (25, 'Laptop Lenovo ThinkBook 15', N'Cân bằng giữa giải trí và công việc doanh nghiệp.', 4, 2, GETDATE(), GETDATE()),
    (26, 'Laptop Lenovo Yoga 9i', N'Tuyệt tác thiết kế, mỏng nhẹ, loa soundbar cực hay.', 4, 5, GETDATE(), GETDATE()),
    (27, 'Laptop Lenovo LOQ 15', N'Dòng gaming mới thay thế IdeaPad Gaming, build chắc chắn.', 4, 1, GETDATE(), GETDATE()),

    -- BRAND 5: Asus
    (28, 'Laptop Asus Zenbook 14 OLED', N'Màn hình OLED rực rỡ, mỏng chưa đến 1.5cm.', 5, 5, GETDATE(), GETDATE()),
    (29, 'Laptop Asus TUF Dash F15', N'Laptop gaming mỏng nhẹ chuẩn quân đội MIL-STD.', 5, 1, GETDATE(), GETDATE()),
    (30, 'Laptop Asus Vivobook 15', N'Màu sắc trẻ trung, phù hợp học sinh sinh viên.', 5, 3, GETDATE(), GETDATE()),
    (31, 'Laptop Asus ROG Zephyrus G14', N'Màn hình LED AniMe Matrix phía sau mặt A cực ngầu.', 5, 1, GETDATE(), GETDATE()),
    (32, 'Laptop Asus ExpertBook B5', N'Trọng lượng siêu nhẹ, thời lượng pin dài cho dân văn phòng.', 5, 2, GETDATE(), GETDATE()),

    -- BRAND 6: Acer
    (33, 'Laptop Acer Predator Helios 300', N'Sức mạnh vô song cho game thủ eSports.', 6, 1, GETDATE(), GETDATE()),
    (34, 'Laptop Acer Swift 3', N'Vỏ nhôm nguyên khối, chuẩn Intel Evo mỏng nhẹ.', 6, 5, GETDATE(), GETDATE()),
    (35, 'Laptop Acer Aspire 5', N'Laptop học tập - văn phòng bán chạy nhất phân khúc.', 6, 3, GETDATE(), GETDATE()),
    (36, 'Laptop Acer Nitro 16', N'Nâng cấp màn hình 16:10, tản nhiệt kim loại lỏng.', 6, 1, GETDATE(), GETDATE()),
    (37, 'Laptop Acer ConceptD 3', N'Thiết kế riêng cho dân sáng tạo nội dung (Creator).', 6, 4, GETDATE(), GETDATE()),

    -- BRAND 7: MSI
    (38, 'Laptop MSI Katana 15', N'Cấu hình đời mới nhất, led phím RGB 4 vùng.', 7, 1, GETDATE(), GETDATE()),
    (39, 'Laptop MSI Stealth 16 Studio', N'Laptop gaming lai Studio vỏ nhôm nguyên khối siêu mỏng.', 7, 5, GETDATE(), GETDATE()),
    (40, 'Laptop MSI Modern 14', N'Mỏng nhẹ thời trang, giá cực tốt cho sinh viên.', 7, 3, GETDATE(), GETDATE()),
    (41, 'Laptop MSI Raider GE78', N'Dải led matrix phía trước máy rực rỡ, cấu hình Max-out.', 7, 1, GETDATE(), GETDATE()),
    (42, 'Laptop MSI Prestige 14', N'Chuẩn mực doanh nhân, màu sắc tinh tế, bảo mật cao.', 7, 5, GETDATE(), GETDATE());

SET IDENTITY_INSERT laptops OFF;

-- =======================================================
-- BỔ SUNG DỮ LIỆU: Bảng configuration_version (Cấu hình)
-- =======================================================
SET IDENTITY_INSERT configuration_version ON;

INSERT INTO configuration_version (configuration_id, cpu, ram, storage, gpu, price, stock_quantity, laptop_id, created_at, updated_at)
VALUES
    -- Apple (Laptops 8-12)
    (11, 'Apple M2 Pro', '16GB Unified', '512GB SSD', '16-core GPU', 48000000, 15, 8, GETDATE(), GETDATE()),
    (12, 'Apple M2', '8GB Unified', '256GB SSD', '10-core GPU', 32000000, 20, 9, GETDATE(), GETDATE()),
    (13, 'Apple M2', '16GB Unified', '512GB SSD', '10-core GPU', 38000000, 10, 10, GETDATE(), GETDATE()),
    (14, 'Intel Core i5', '8GB RAM', '512GB SSD', 'Intel HD Graphics', 22000000, 5, 11, GETDATE(), GETDATE()),
    (15, 'Apple M3', '16GB Unified', '512GB SSD', '10-core GPU', 35000000, 25, 12, GETDATE(), GETDATE()),

    -- Dell (Laptops 13-17)
    (16, 'Intel Core i9-13900HX', '32GB DDR5', '1TB NVMe SSD', 'NVIDIA RTX 4080 12GB', 89000000, 5, 13, GETDATE(), GETDATE()),
    (17, 'Intel Core i5-1235U', '8GB DDR4', '256GB NVMe SSD', 'Intel Iris Xe', 14500000, 40, 14, GETDATE(), GETDATE()),
    (18, 'Intel Core i5-1335U', '16GB DDR4', '512GB NVMe SSD', 'Intel Iris Xe', 18000000, 30, 15, GETDATE(), GETDATE()),
    (19, 'Intel Core i7-1365U', '16GB LPDDR5', '512GB NVMe SSD', 'Intel Iris Xe', 36000000, 12, 16, GETDATE(), GETDATE()),
    (20, 'Intel Core i7-13800H', '32GB LPDDR5', '1TB NVMe SSD', 'NVIDIA RTX 3000 Ada', 65000000, 8, 17, GETDATE(), GETDATE()),

    -- HP (Laptops 18-22)
    (21, 'AMD Ryzen 7 7840HS', '16GB DDR5', '512GB NVMe SSD', 'NVIDIA RTX 4060 8GB', 36000000, 15, 18, GETDATE(), GETDATE()),
    (22, 'Intel Core i7-1355U', '16GB LPDDR5', '512GB NVMe SSD', 'Intel Iris Xe', 28000000, 20, 19, GETDATE(), GETDATE()),
    (23, 'Intel Core i5-1340P', '16GB DDR5', '512GB NVMe SSD', 'Intel Iris Xe', 31000000, 15, 20, GETDATE(), GETDATE()),
    (24, 'Intel Core i5-12450H', '8GB DDR4', '512GB NVMe SSD', 'NVIDIA GTX 1650 4GB', 17000000, 35, 21, GETDATE(), GETDATE()),
    (25, 'Intel Core i5-1335U', '8GB DDR4', '512GB NVMe SSD', 'Intel Iris Xe', 19000000, 25, 22, GETDATE(), GETDATE()),

    -- Lenovo (Laptops 23-27)
    (26, 'AMD Ryzen 7 7745HX', '16GB DDR5', '1TB NVMe SSD', 'NVIDIA RTX 4070 8GB', 41000000, 10, 23, GETDATE(), GETDATE()),
    (27, 'Intel Core i5-1235U', '16GB DDR4', '512GB NVMe SSD', 'Intel Iris Xe', 16000000, 45, 24, GETDATE(), GETDATE()),
    (28, 'AMD Ryzen 5 7530U', '8GB DDR4', '512GB NVMe SSD', 'AMD Radeon Graphics', 15500000, 30, 25, GETDATE(), GETDATE()),
    (29, 'Intel Core i7-1360P', '16GB LPDDR5', '1TB NVMe SSD', 'Intel Iris Xe', 45000000, 8, 26, GETDATE(), GETDATE()),
    (30, 'Intel Core i5-13420H', '16GB DDR5', '512GB NVMe SSD', 'NVIDIA RTX 4050 6GB', 24000000, 20, 27, GETDATE(), GETDATE()),

    -- Asus (Laptops 28-32)
    (31, 'Intel Core i5-1340P', '16GB LPDDR5', '512GB NVMe SSD', 'Intel Iris Xe', 25500000, 22, 28, GETDATE(), GETDATE()),
    (32, 'Intel Core i7-12650H', '16GB DDR5', '512GB NVMe SSD', 'NVIDIA RTX 3060 6GB', 26000000, 18, 29, GETDATE(), GETDATE()),
    (33, 'AMD Ryzen 5 7520U', '8GB LPDDR5', '512GB NVMe SSD', 'AMD Radeon Graphics', 13000000, 50, 30, GETDATE(), GETDATE()),
    (34, 'AMD Ryzen 9 7940HS', '32GB DDR5', '1TB NVMe SSD', 'NVIDIA RTX 4060 8GB', 48000000, 10, 31, GETDATE(), GETDATE()),
    (35, 'Intel Core i5-1240P', '16GB DDR5', '512GB NVMe SSD', 'Intel Iris Xe', 22000000, 15, 32, GETDATE(), GETDATE()),

    -- Acer (Laptops 33-37)
    (36, 'Intel Core i7-12700H', '16GB DDR5', '512GB NVMe SSD', 'NVIDIA RTX 3070 Ti 8GB', 35000000, 12, 33, GETDATE(), GETDATE()),
    (37, 'Intel Core i5-1240P', '16GB LPDDR4x', '512GB NVMe SSD', 'Intel Iris Xe', 21000000, 25, 34, GETDATE(), GETDATE()),
    (38, 'Intel Core i3-1215U', '8GB DDR4', '256GB NVMe SSD', 'Intel UHD Graphics', 11500000, 60, 35, GETDATE(), GETDATE()),
    (39, 'AMD Ryzen 7 7840HS', '16GB DDR5', '512GB NVMe SSD', 'NVIDIA RTX 4050 6GB', 29000000, 15, 36, GETDATE(), GETDATE()),
    (40, 'Intel Core i7-12700H', '16GB DDR4', '1TB NVMe SSD', 'NVIDIA RTX 3050 Ti 4GB', 33000000, 8, 37, GETDATE(), GETDATE()),

    -- MSI (Laptops 38-42)
    (41, 'Intel Core i7-13620H', '16GB DDR5', '512GB NVMe SSD', 'NVIDIA RTX 4060 8GB', 32000000, 20, 38, GETDATE(), GETDATE()),
    (42, 'Intel Core i9-13900H', '32GB DDR5', '2TB NVMe SSD', 'NVIDIA RTX 4070 8GB', 62000000, 5, 39, GETDATE(), GETDATE()),
    (43, 'Intel Core i5-1235U', '8GB DDR4', '512GB NVMe SSD', 'Intel Iris Xe', 13500000, 40, 40, GETDATE(), GETDATE()),
    (44, 'Intel Core i9-13980HX', '64GB DDR5', '2TB NVMe SSD', 'NVIDIA RTX 4090 16GB', 125000000, 2, 41, GETDATE(), GETDATE()),
    (45, 'Intel Core i7-1280P', '16GB LPDDR4x', '1TB NVMe SSD', 'NVIDIA GTX 1650 Max-Q', 30000000, 10, 42, GETDATE(), GETDATE());

SET IDENTITY_INSERT configuration_version OFF;

-- =======================================================
-- BỔ SUNG DỮ LIỆU: Bảng gift_details (Quà tặng cho cấu hình mới)
-- =======================================================
SET IDENTITY_INSERT gift_details ON;

INSERT INTO gift_details (gift_id, quantity, promotion_id, configuration_version_id, gift_item_id, created_at, updated_at)
VALUES
    -- Tặng Balo (2) cho dòng Apple & Mỏng nhẹ
    (7, 1, NULL, 11, 2, GETDATE(), GETDATE()),
    (8, 1, NULL, 12, 2, GETDATE(), GETDATE()),
    (9, 1, NULL, 13, 2, GETDATE(), GETDATE()),
    (10, 1, NULL, 15, 2, GETDATE(), GETDATE()),
    (11, 1, NULL, 28, 2, GETDATE(), GETDATE()),
    (12, 1, NULL, 29, 2, GETDATE(), GETDATE()),

    -- Tặng Chuột (1) và Lót chuột (4) cho các dòng Gaming
    (13, 1, NULL, 16, 1, GETDATE(), GETDATE()),
    (14, 1, NULL, 16, 4, GETDATE(), GETDATE()),
    (15, 1, NULL, 21, 1, GETDATE(), GETDATE()),
    (16, 1, NULL, 21, 4, GETDATE(), GETDATE()),
    (17, 1, NULL, 26, 1, GETDATE(), GETDATE()),
    (18, 1, NULL, 30, 1, GETDATE(), GETDATE()),
    (19, 1, NULL, 36, 1, GETDATE(), GETDATE()),
    (20, 1, NULL, 41, 1, GETDATE(), GETDATE()),
    (21, 1, NULL, 44, 1, GETDATE(), GETDATE()),
    (22, 1, NULL, 44, 3, GETDATE(), GETDATE()), -- Riêng RTX4090 tặng thêm Tai nghe (3)

    -- Tặng Tai nghe Bluetooth (3) cho một số dòng doanh nhân/sáng tạo
    (23, 1, NULL, 20, 3, GETDATE(), GETDATE()),
    (24, 1, NULL, 26, 3, GETDATE(), GETDATE()),
    (25, 1, NULL, 42, 3, GETDATE(), GETDATE()),

    -- Tặng Lót chuột (4) cho sinh viên/văn phòng
    (26, 1, NULL, 14, 4, GETDATE(), GETDATE()),
    (27, 1, NULL, 17, 4, GETDATE(), GETDATE()),
    (28, 1, NULL, 24, 4, GETDATE(), GETDATE()),
    (29, 1, NULL, 33, 4, GETDATE(), GETDATE()),
    (30, 1, NULL, 38, 4, GETDATE(), GETDATE()),
    (31, 1, NULL, 43, 4, GETDATE(), GETDATE());

SET IDENTITY_INSERT gift_details OFF;

ALTER TABLE configuration_version
    ALTER COLUMN price DECIMAL(18, 2);