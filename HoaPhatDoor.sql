CREATE DATABASE HoaPhatDoor;
GO

USE HoaPhatDoor;
GO


-- ========================================
-- Bảng Admins (quản trị viên)
-- ========================================
CREATE TABLE Admins (
    admin_id     INT IDENTITY(1,1) PRIMARY KEY,
    username     NVARCHAR(100) NOT NULL UNIQUE,   -- Tên đăng nhập
    password_hash NVARCHAR(255) NOT NULL,         -- Mật khẩu băm
    email        NVARCHAR(150),                   -- Email liên hệ
    phone        NVARCHAR(50),                    -- Số điện thoại
    );
GO


-- ========================================
-- Bảng Categories (danh mục sản phẩm)
-- ========================================
CREATE TABLE Categories (
    category_id   INT IDENTITY(1,1) PRIMARY KEY,
    category_name NVARCHAR(200) NOT NULL,         -- Tên danh mục
    description   NVARCHAR(MAX),                  -- Mô tả chi tiết
    status        NVARCHAR(20) DEFAULT 'visible'  -- Trạng thái
        CHECK (status IN ('visible','hidden'))
);
GO






-- ========================================
-- Bảng Products (sản phẩm)
-- ========================================
CREATE TABLE Products (
    product_id   INT IDENTITY(1,1) PRIMARY KEY,
    category_id  INT NOT NULL,                    -- FK -> Categories
	sku NVARCHAR(50) NOT NULL UNIQUE,
    name         NVARCHAR(255) NOT NULL,          -- Tên sản phẩm
    price        DECIMAL(18,2) NOT NULL 
                    CHECK (price >= 0),           -- Giá >= 0
    short_desc   NVARCHAR(500),                   -- Mô tả ngắn
    spec_html    NVARCHAR(MAX),                   -- Nội dung chi tiết / spec
    main_image   NVARCHAR(300),                   -- Ảnh chính
    status       NVARCHAR(20) DEFAULT 'visible'   -- Trạng thái
        CHECK (status IN ('visible','hidden')),
    created_at   DATETIME DEFAULT GETDATE(),      -- Ngày tạo
    updated_at   DATETIME DEFAULT GETDATE(),      -- Ngày cập nhật
    FOREIGN KEY (category_id) REFERENCES Categories(category_id),
);
GO


-- ========================================
-- Bảng Media (thư viện ảnh chung)
-- ========================================
CREATE TABLE Media (
    media_id     INT IDENTITY(1,1) PRIMARY KEY,
    file_name    NVARCHAR(255) NOT NULL,          -- Tên file
    file_path    NVARCHAR(500) NOT NULL,          -- Đường dẫn file
    uploaded_at  DATETIME DEFAULT GETDATE(),      -- Ngày upload
    uploaded_by  INT NULL,                        -- Ai upload
    FOREIGN KEY (uploaded_by) REFERENCES Admins(admin_id)
);
GO


-- ========================================
-- Bảng ProductImages (ảnh phụ của sản phẩm)
-- ========================================
CREATE TABLE ProductImages (
    id          INT IDENTITY(1,1) PRIMARY KEY,
    product_id  INT NOT NULL,                     -- FK -> Products
    media_id    INT NOT NULL,                     -- FK -> Media
    sort_order  INT DEFAULT 0,                    -- Thứ tự hiển thị
    FOREIGN KEY (product_id) REFERENCES Products(product_id),
    FOREIGN KEY (media_id) REFERENCES Media(media_id)
);
GO


-- ========================================
-- Bảng Discounts (khuyến mãi)
-- ========================================
CREATE TABLE Discounts (
    discount_id      INT IDENTITY(1,1) PRIMARY KEY,
    product_id       INT NOT NULL,                -- FK -> Products
    discount_percent INT NOT NULL 
                        CHECK (discount_percent BETWEEN 0 AND 100),
    start_date       DATE NOT NULL,               -- Ngày bắt đầu
    end_date         DATE NOT NULL,               -- Ngày kết thúc
    status           NVARCHAR(20) DEFAULT 'active'-- Trạng thái
        CHECK (status IN ('active','inactive')),
    FOREIGN KEY (product_id) REFERENCES Products(product_id)
);
GO


-- ========================================
-- Bảng Posts (tin tức / blog)
-- ========================================
CREATE TABLE Posts (
    post_id      INT IDENTITY(1,1) PRIMARY KEY,
    title        NVARCHAR(255) NOT NULL,          -- Tiêu đề bài viết
    summary      NVARCHAR(500),                   -- Tóm tắt
    content      NVARCHAR(MAX),                   -- Nội dung
    media_id     INT NULL,                        -- Ảnh đại diện
    published_at DATETIME DEFAULT GETDATE(),      -- Thời điểm xuất bản
    author_id    INT NOT NULL,                    -- Tác giả (FK -> Admins)
    status       NVARCHAR(20) DEFAULT 'visible'   -- Trạng thái
        CHECK (status IN ('visible','hidden')),
    created_at   DATETIME DEFAULT GETDATE(),      -- Ngày tạo
    updated_at   DATETIME DEFAULT GETDATE(),      -- Ngày cập nhật
    FOREIGN KEY (media_id) REFERENCES Media(media_id),
    FOREIGN KEY (author_id) REFERENCES Admins(admin_id)
);
GO


-- ========================================
-- Bảng Banners (banner / slider)
-- ========================================
CREATE TABLE Banners (
    banner_id   INT IDENTITY(1,1) PRIMARY KEY,
    title       NVARCHAR(200),                    -- Tiêu đề banner
    media_id    INT NOT NULL,                     -- Ảnh (FK -> Media)
    status      NVARCHAR(20) DEFAULT 'visible'    -- Trạng thái
        CHECK (status IN ('visible','hidden')),
    FOREIGN KEY (media_id) REFERENCES Media(media_id)
);
GO


-- ========================================
-- Bảng ContactMessages (form liên hệ)
-- ========================================
CREATE TABLE ContactMessages (
    message_id  INT IDENTITY(1,1) PRIMARY KEY,
    name        NVARCHAR(150) NOT NULL,           -- Tên người gửi
    email       NVARCHAR(150) NOT NULL,           -- Email
    phone       NVARCHAR(50),                     -- Số điện thoại
    subject     NVARCHAR(200),                    -- Chủ đề
    message     NVARCHAR(MAX) NOT NULL,           -- Nội dung
    status      NVARCHAR(20) DEFAULT 'new'        -- Trạng thái
        CHECK (status IN ('new','read','replied')),
    created_at  DATETIME DEFAULT GETDATE()        -- Ngày gửi
);
GO


-- ========================================
-- Trigger: tự động inactive Discount khi hết hạn
-- ========================================
CREATE TRIGGER trg_Discounts_AutoExpire
ON Discounts
AFTER INSERT, UPDATE
AS
BEGIN
    SET NOCOUNT ON;

    UPDATE d
    SET d.status = 'inactive'
    FROM Discounts d
    INNER JOIN inserted i ON d.discount_id = i.discount_id
    WHERE d.end_date < CAST(GETDATE() AS DATE);
END;
GO



-- ========================================
-- Admins
-- ========================================
INSERT INTO Admins (username, password_hash, email, phone)
VALUES 
('admin', '123123', 'admin@vietduchome.vn', '0901234567');

-- ========================================
-- Categories
-- ========================================
INSERT INTO Categories (category_name, description, status)
VALUES 
    (N'Cửa gỗ', N'Cửa gỗ tự nhiên và công nghiệp', N'visible'),
    (N'Cửa nhựa', N'Cửa nhựa giả gỗ và composite', N'visible'),
    (N'Cửa thép', N'Cửa thép chống cháy, cửa thép vân gỗ', N'visible'),
    (N'Nội thất', N'Nội thất gia đình, văn phòng', N'visible'),
    (N'Phụ kiện cửa', N'Bản lề, khóa, tay đẩy cửa', N'visible'),
    (N'Cửa kính', N'Cửa kính cường lực', N'visible'),
    (N'Cửa xếp', N'Cửa xếp nhựa, thép', N'hidden'),
    (N'Cửa cổng', N'Cửa cổng sắt, inox', N'visible'),
    (N'Cửa ra vào', N'Cửa ra vào văn phòng, nhà ở', N'visible'),
    (N'Cửa nhôm', N'Cửa nhôm kính', N'visible'),
    (N'Cửa phòng tắm', N'Cửa phòng tắm nhựa, kính', N'hidden'),
    (N'Cửa phòng ngủ', N'Cửa phòng ngủ gỗ, nhựa', N'visible'),
    (N'Cửa thoát hiểm', N'Cửa thoát hiểm thép', N'visible'),
    (N'Cửa gỗ công nghiệp', N'Cửa gỗ công nghiệp MDF', N'visible'),
    (N'Cửa chống nước', N'Cửa chống nước cho nhà vệ sinh', N'visible'),
    (N'Cửa sổ', N'Cửa sổ nhôm, kính', N'visible'),
    (N'Cửa cuốn', N'Cửa cuốn tự động', N'visible'),
    (N'Cửa vòm', N'Cửa vòm thép, gỗ', N'hidden'),
    (N'Cửa đặc biệt', N'Cửa đặc biệt theo yêu cầu', N'visible');
GO

-- ========================================
-- Products
-- ========================================
INSERT INTO Products (category_id, name, price, short_desc, spec_html, main_image, status, sku)
VALUES 
    (1, N'Cửa gỗ tự nhiên', 3000000, N'Cửa gỗ tự nhiên cao cấp', N'Cửa gỗ tự nhiên với vân đẹp', N'image1.jpg', N'visible', N'SKU001'),
    (2, N'Cửa nhựa giả gỗ', 1500000, N'Cửa nhựa giả gỗ Hàn Quốc', N'Cửa nhựa chất lượng cao', N'image2.jpg', N'visible', N'SKU002'),
    (3, N'Cửa thép chống cháy', 4500000, N'Cửa thép chống cháy tiêu chuẩn PCCC', N'Cửa thép an toàn', N'image3.jpg', N'visible', N'SKU003'),
    (4, N'Bàn làm việc', 2000000, N'Bàn làm việc gỗ công nghiệp', N'Bàn làm việc hiện đại', N'image4.jpg', N'visible', N'SKU004'),
    (5, N'Bản lề cửa', 100000, N'Bản lề inox chịu lực', N'Bản lề chất lượng cao', N'image5.jpg', N'visible', N'SKU005'),
    (6, N'Cửa nhựa Composite', 1800000, N'Cửa nhựa Composite cao cấp', N'Cửa nhựa bền đẹp', N'image6.jpg', N'visible', N'SKU006'),
    (7, N'Cửa kính cường lực', 3500000, N'Cửa kính cường lực cao cấp', N'Cửa kính an toàn', N'image7.jpg', N'visible', N'SKU007'),
    (8, N'Cửa cuốn tự động', 5000000, N'Cửa cuốn tự động tiện lợi', N'Cửa cuốn với động cơ mạnh mẽ', N'image8.jpg', N'visible', N'SKU008'),
    (9, N'Cửa vòm gỗ', 6000000, N'Cửa vòm gỗ tự nhiên', N'Cửa vòm tinh xảo', N'image9.jpg', N'visible', N'SKU009'),
    (10, N'Cửa xếp thép', 2500000, N'Cửa xếp thép chất lượng', N'Cửa xếp an toàn, bền bỉ', N'image10.jpg', N'visible', N'SKU010'),
    (11, N'Cửa phòng tắm nhựa', 1200000, N'Cửa phòng tắm nhựa cao cấp', N'Cửa nhựa chịu nước', N'image11.jpg', N'visible', N'SKU011'),
    (12, N'Cửa phòng ngủ gỗ', 4000000, N'Cửa phòng ngủ gỗ MDF', N'Cửa gỗ đẹp, sang trọng', N'image12.jpg', N'visible', N'SKU012'),
    (13, N'Cửa phòng vệ sinh', 800000, N'Cửa phòng vệ sinh nhựa', N'Cửa chịu nước, bền bỉ', N'image13.jpg', N'visible', N'SKU013'),
    (14, N'Cửa phòng khách', 3500000, N'Cửa phòng khách gỗ công nghiệp', N'Cửa gỗ đẹp, hiện đại', N'image14.jpg', N'visible', N'SKU014'),
    (15, N'Cửa sổ nhôm', 1500000, N'Cửa sổ nhôm kính', N'Cửa sổ chắc chắn', N'image15.jpg', N'visible', N'SKU015'),
    (16, N'Cửa gỗ MDF', 2800000, N'Cửa gỗ MDF chống cong vênh', N'Cửa gỗ chống nước', N'image16.jpg', N'visible', N'SKU016'),
    (17, N'Cửa ra vào văn phòng', 2500000, N'Cửa ra vào văn phòng đẹp', N'Cửa văn phòng hiện đại', N'image17.jpg', N'visible', N'SKU017'),
    (18, N'Cửa thép vân gỗ', 4000000, N'Cửa thép vân gỗ cao cấp', N'Cửa thép bền bỉ', N'image18.jpg', N'visible', N'SKU018'),
    (19, N'Cửa nhựa ABS', 1600000, N'Cửa nhựa ABS giả gỗ', N'Cửa nhựa chất lượng cao', N'image19.jpg', N'visible', N'SKU019'),
    (19, N'Cửa thoát hiểm', 3500000, N'Cửa thoát hiểm thép', N'Cửa thép chống cháy, an toàn', N'image20.jpg', N'visible', N'SKU020');
GO


-- ========================================
-- Media
-- ========================================

INSERT INTO Media (file_name, file_path, uploaded_at, uploaded_by)
VALUES 
    ('image1.jpg', 'images/image1.jpg', GETDATE(), 1),
    ('image2.jpg', 'images/image2.jpg', GETDATE(), 1),
    ('image3.jpg', 'images/image3.jpg', GETDATE(), 1),
    ('image4.jpg', 'images/image4.jpg', GETDATE(), 1),
    ('image5.jpg', 'images/image5.jpg', GETDATE(), 1),
    ('image6.jpg', 'images/image6.jpg', GETDATE(), 1),
    ('image7.jpg', 'images/image7.jpg', GETDATE(), 1),
    ('image8.jpg', 'images/image8.jpg', GETDATE(), 1),
    ('image9.jpg', 'images/image9.jpg', GETDATE(), 1),
    ('image10.jpg', 'images/image10.jpg', GETDATE(), 1),
    ('image11.jpg', 'images/image11.jpg', GETDATE(), 1),
    ('image12.jpg', 'images/image12.jpg', GETDATE(), 1),
    ('image13.jpg', 'images/image13.jpg', GETDATE(), 1),
    ('image14.jpg', 'images/image14.jpg', GETDATE(), 1),
    ('image15.jpg', 'images/image15.jpg', GETDATE(), 1),
    ('image16.jpg', 'images/image16.jpg', GETDATE(), 1),
    ('image17.jpg', 'images/image17.jpg', GETDATE(), 1),
    ('image18.jpg', 'images/image18.jpg', GETDATE(), 1),
    ('image19.jpg', 'images/image19.jpg', GETDATE(), 1),
    ('image20.jpg', 'images/image20.jpg', GETDATE(), 1);
GO

-- ========================================
-- Products
-- ========================================

INSERT INTO ProductImages (product_id, media_id, sort_order)
VALUES 
    (1, 1, 1),
    (2, 2, 1),
    (3, 3, 1),
    (4, 4, 1),
    (5, 5, 1),
    (6, 6, 1),
    (7, 7, 1),
    (8, 8, 1),
    (9, 9, 1),
    (10, 10, 1),
    (11, 11, 1),
    (12, 12, 1),
    (13, 13, 1),
    (14, 14, 1),
    (15, 15, 1),
    (16, 16, 1),
    (17, 17, 1),
    (18, 18, 1),
    (19, 19, 1),
    (19, 20, 1);
GO

-- ========================================
-- Discounts
-- ========================================
INSERT INTO Discounts (product_id, discount_percent, start_date, end_date, status)
VALUES 
    (1, 10, '2025-01-01', '2025-01-15', 'active'),
    (2, 15, '2025-02-01', '2025-02-28', 'active'),
    (3, 20, '2025-03-01', '2025-03-15', 'active'),
    (4, 5, '2025-04-01', '2025-04-10', 'active'),
    (5, 25, '2025-05-01', '2025-05-20', 'active'),
    (6, 30, '2025-06-01', '2025-06-30', 'active'),
    (7, 10, '2025-07-01', '2025-07-10', 'active'),
    (8, 15, '2025-08-01', '2025-08-15', 'active'),
    (9, 20, '2025-09-01', '2025-09-30', 'active'),
    (10, 5, '2025-10-01', '2025-10-10', 'active'),
    (11, 15, '2025-11-01', '2025-11-15', 'active'),
    (12, 10, '2025-12-01', '2025-12-15', 'active'),
    (13, 30, '2025-01-01', '2025-01-20', 'active'),
    (14, 25, '2025-02-01', '2025-02-28', 'active'),
    (15, 5, '2025-03-01', '2025-03-15', 'active'),
    (16, 10, '2025-04-01', '2025-04-10', 'active'),
    (17, 20, '2025-05-01', '2025-05-20', 'active'),
    (18, 10, '2025-06-01', '2025-06-30', 'active'),
    (19, 15, '2025-07-01', '2025-07-10', 'active');
GO