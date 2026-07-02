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