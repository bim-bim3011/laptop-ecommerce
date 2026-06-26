-- Dữ liệu mẫu (Sample Data) cho CSDL Laptop E-commerce (Microsoft SQL Server)
-- Vui lòng chạy các lệnh này theo thứ tự để không bị lỗi Foreign Key Constraint.

-- =======================================================
-- 1. Thêm dữ liệu cho bảng brands (Thương hiệu)
-- =======================================================
SET IDENTITY_INSERT brands ON;
INSERT INTO brands (brand_id, brand_name, created_at, updated_at) 
VALUES 
(1, 'Apple', GETDATE(), GETDATE()),
(2, 'Dell', GETDATE(), GETDATE()),
(3, 'HP', GETDATE(), GETDATE()),
(4, 'Lenovo', GETDATE(), GETDATE()),
(5, 'Asus', GETDATE(), GETDATE()),
(6, 'Acer', GETDATE(), GETDATE()),
(7, 'MSI', GETDATE(), GETDATE());
SET IDENTITY_INSERT brands OFF;

-- =======================================================
-- 2. Thêm dữ liệu cho bảng categories (Danh mục)
-- =======================================================
SET IDENTITY_INSERT categories ON;
INSERT INTO categories (category_id, category_name, created_at, updated_at) 
VALUES 
(1, N'Gaming', GETDATE(), GETDATE()),
(2, N'Văn Phòng', GETDATE(), GETDATE()),
(3, N'Học Sinh - Sinh Viên', GETDATE(), GETDATE()),
(4, N'Đồ Họa - Kỹ Thuật', GETDATE(), GETDATE()),
(5, N'Mỏng Nhẹ Cao Cấp', GETDATE(), GETDATE());
SET IDENTITY_INSERT categories OFF;

-- =======================================================
-- 3. Thêm dữ liệu cho bảng laptops (Laptop)
-- =======================================================
SET IDENTITY_INSERT laptops ON;
INSERT INTO laptops (laptop_id, laptop_name, description, brand_id, category_id, created_at, updated_at) 
VALUES 
(1, 'Dell XPS 15 9530', N'Laptop doanh nhân cao cấp, thiết kế nhôm nguyên khối, màn hình OLED sắc nét, phù hợp làm việc đa nhiệm và đồ họa 2D.', 2, 5, GETDATE(), GETDATE()),
(2, 'MacBook Pro 16 inch M3 Max', N'Cỗ máy đồ họa mạnh mẽ nhất của Apple với chip M3 Max, phù hợp cho coder, dựng phim chuyên nghiệp.', 1, 4, GETDATE(), GETDATE()),
(3, 'Asus ROG Strix G15', N'Laptop Gaming với tản nhiệt tốt, dải đèn LED RGB cực ngầu xung quanh viền máy.', 5, 1, GETDATE(), GETDATE()),
(4, 'HP Pavilion 14', N'Mẫu laptop giá rẻ lý tưởng cho học sinh, sinh viên với thiết kế nhỏ gọn, dễ mang theo.', 3, 3, GETDATE(), GETDATE()),
(5, 'Lenovo ThinkPad X1 Carbon Gen 11', N'Dòng laptop doanh nhân siêu bền bỉ, trọng lượng chỉ khoảng 1.1kg, bàn phím gõ đỉnh cao.', 4, 2, GETDATE(), GETDATE()),
(6, 'Acer Nitro 5 Tiger', N'Laptop gaming quốc dân, hiệu năng ấn tượng trong tầm giá, tản nhiệt tốt.', 6, 1, GETDATE(), GETDATE()),
(7, 'MacBook Air M1', N'Mẫu laptop mỏng nhẹ, pin siêu trâu của Apple, luôn đứng top bán chạy.', 1, 5, GETDATE(), GETDATE());
SET IDENTITY_INSERT laptops OFF;

-- =======================================================
-- 4. Thêm dữ liệu cho bảng configuration_version (Cấu hình)
-- =======================================================
SET IDENTITY_INSERT configuration_version ON;
INSERT INTO configuration_version (configuration_id, cpu, ram, storage, gpu, price, stock_quantity, laptop_id, created_at, updated_at) 
VALUES 
-- Cấu hình Dell XPS 15
(1, 'Intel Core i7-13700H', '16GB DDR5', '512GB NVMe SSD', 'NVIDIA RTX 4050 6GB', 45000000, 10, 1, GETDATE(), GETDATE()),
(2, 'Intel Core i9-13900H', '32GB DDR5', '1TB NVMe SSD', 'NVIDIA RTX 4060 8GB', 55000000, 5, 1, GETDATE(), GETDATE()),

-- Cấu hình MacBook Pro 16
(3, 'Apple M3 Pro', '18GB Unified', '512GB SSD', 'Integrated 18-core GPU', 65000000, 15, 2, GETDATE(), GETDATE()),
(4, 'Apple M3 Max', '36GB Unified', '1TB SSD', 'Integrated 30-core GPU', 85000000, 8, 2, GETDATE(), GETDATE()),

-- Cấu hình Asus ROG Strix G15
(5, 'AMD Ryzen 7 6800H', '16GB DDR5', '512GB NVMe SSD', 'NVIDIA RTX 3050 4GB', 25000000, 20, 3, GETDATE(), GETDATE()),
(6, 'AMD Ryzen 9 6900HX', '16GB DDR5', '1TB NVMe SSD', 'NVIDIA RTX 3060 6GB', 32000000, 10, 3, GETDATE(), GETDATE()),

-- Cấu hình HP Pavilion 14
(7, 'Intel Core i5-1240P', '8GB DDR4', '256GB NVMe SSD', 'Intel Iris Xe Graphics', 15000000, 30, 4, GETDATE(), GETDATE()),

-- Cấu hình ThinkPad X1 Carbon
(8, 'Intel Core i7-1355U', '16GB LPDDR5', '512GB NVMe SSD', 'Intel Iris Xe Graphics', 40000000, 12, 5, GETDATE(), GETDATE()),

-- Cấu hình Acer Nitro 5 Tiger
(9, 'Intel Core i5-12500H', '8GB DDR4', '512GB NVMe SSD', 'NVIDIA RTX 3050 Ti 4GB', 21000000, 25, 6, GETDATE(), GETDATE()),

-- Cấu hình MacBook Air M1
(10, 'Apple M1', '8GB Unified', '256GB SSD', 'Integrated 7-core GPU', 18500000, 50, 7, GETDATE(), GETDATE());
SET IDENTITY_INSERT configuration_version OFF;

-- =======================================================
-- 5. Thêm dữ liệu cho bảng role (Vai trò)
-- =======================================================
SET IDENTITY_INSERT role ON;
INSERT INTO role (id, name) 
VALUES 
(1, 'USER'),
(2, 'ADMIN');
SET IDENTITY_INSERT role OFF;

-- =======================================================
-- 6. Thêm dữ liệu cho bảng users (Người dùng) - MẬT KHẨU CẦN ĐƯỢC MÃ HOÁ (BCRYPT) TRONG THỰC TẾ
-- Lưu ý: Mật khẩu ở đây đang là plain text giả định, bạn có thể tự thay bằng hash nếu đã dùng Spring Security.
-- =======================================================
SET IDENTITY_INSERT users ON;
INSERT INTO users (user_id, name, email, password, phone_number, address, status, created_at, updated_at)
VALUES 
(1, N'Quản Trị Viên', 'admin@laptopshop.com', '123456', '0987654321', N'Hà Nội', 'ACTIVE', GETDATE(), GETDATE()),
(2, N'Khách Hàng A', 'user@laptopshop.com', '123456', '0123456789', N'Hồ Chí Minh', 'ACTIVE', GETDATE(), GETDATE());
SET IDENTITY_INSERT users OFF;

-- Nếu bảng users_roles được tạo tự động bởi JPA:
INSERT INTO users_roles (user_user_id, roles_id) VALUES
                                                     (1, 2), -- Cấp quyền ADMIN (id = 2) cho tài khoản admin (user_id = 1)
                                                     (2, 1); -- Cấp quyền USER (id = 1) cho tài khoản khách hàng (user_id = 2)
