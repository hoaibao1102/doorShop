/* =====================================================================================================================
                                           SQL Server 2019+  —  vietphatdoor
   ===================================================================================================================== */

-- 0) Tạo database & chọn DB
IF DB_ID(N'hoaphatdoor') IS NULL
    CREATE DATABASE hoaphatdoor;
GO
USE hoaphatdoor;
GO

/* ================================================
   1) DROP objects (theo đúng thứ tự an toàn, khi tạo lần đầu thì bỏ qua mục này)
   ================================================ */
-- Triggers
DROP TRIGGER IF EXISTS dbo.trg_Discounts_NoOverlap_INS;
DROP TRIGGER IF EXISTS dbo.trg_Discounts_NoOverlap_UPD;
DROP TRIGGER IF EXISTS dbo.trg_Products_MainImg_Check_INS;
DROP TRIGGER IF EXISTS dbo.trg_Products_MainImg_Check_UPD;
DROP TRIGGER IF EXISTS dbo.trg_Products_touch_updated_at;
DROP TRIGGER IF EXISTS dbo.trg_Posts_touch_updated_at;
GO

-- Views
DROP VIEW IF EXISTS dbo.v_product_price;
DROP VIEW IF EXISTS dbo.v_product_active_discount;
GO

-- Functions
DROP FUNCTION IF EXISTS dbo.fn_check_discount_overlap;
DROP FUNCTION IF EXISTS dbo.fn_is_product_image;
GO

-- Foreign Keys có thể vòng lặp
ALTER TABLE dbo.Products DROP CONSTRAINT IF EXISTS fk_products_main_image;
GO

-- Tables (drop theo FK-safe order)
DROP TABLE IF EXISTS dbo.Posts;
DROP TABLE IF EXISTS dbo.ContactMessages;
DROP TABLE IF EXISTS dbo.Banners;
DROP TABLE IF EXISTS dbo.Discounts;
DROP TABLE IF EXISTS dbo.ProductImages;
DROP TABLE IF EXISTS dbo.Products;
DROP TABLE IF EXISTS dbo.Brands;
DROP TABLE IF EXISTS dbo.Categories;
DROP TABLE IF EXISTS dbo.Admin;
GO


/* ================================================
   2) Tables
   ================================================ */
-- Admin
CREATE TABLE dbo.Admin (
    admin_id      INT IDENTITY(1,1) PRIMARY KEY,
    username      NVARCHAR(50)  NOT NULL UNIQUE,
    password_hash NVARCHAR(255) NOT NULL,
    email         NVARCHAR(100) NOT NULL UNIQUE,
    full_name     NVARCHAR(100) NULL,
    phone         NVARCHAR(20)  NULL,
    created_at    DATETIME2(0)  NOT NULL CONSTRAINT DF_Admin_created_at DEFAULT (SYSDATETIME())
);
GO

-- Categories
CREATE TABLE dbo.Categories (
    category_id   INT IDENTITY(1,1) PRIMARY KEY,
    category_name NVARCHAR(100) NOT NULL UNIQUE,
    description   NVARCHAR(MAX) NULL
);
GO

-- Brands
CREATE TABLE dbo.Brands (
    brand_id     INT IDENTITY(1,1) PRIMARY KEY,
    category_id  INT NOT NULL,
    brand_name   NVARCHAR(100) NOT NULL,
    description  NVARCHAR(MAX) NULL,

    CONSTRAINT uq_brand_name_per_category UNIQUE (category_id, brand_name),
    CONSTRAINT fk_brands_category
        FOREIGN KEY (category_id) REFERENCES dbo.Categories(category_id)
        ON UPDATE CASCADE ON DELETE CASCADE
);
GO

-- Products
CREATE TABLE dbo.Products (
    product_id    INT IDENTITY(1,1) PRIMARY KEY,
    category_id   INT NOT NULL,
    brand_id      INT NOT NULL,
    name          NVARCHAR(150) NOT NULL,
    price         DECIMAL(10,2) NOT NULL 
                     CONSTRAINT CK_Products_price_nonneg CHECK (price >= 0),
    spec_html     NVARCHAR(MAX) NULL,
    main_image_id INT NULL,
    status        NVARCHAR(10)  NULL 
                     CONSTRAINT CK_Products_status CHECK (status IN (N'active', N'inactive')),
    created_at    DATETIME2(0)  NOT NULL CONSTRAINT DF_Products_created_at DEFAULT (SYSDATETIME()),
    updated_at    DATETIME2(0)  NOT NULL CONSTRAINT DF_Products_updated_at DEFAULT (SYSDATETIME()),

    CONSTRAINT fk_products_category FOREIGN KEY (category_id) REFERENCES dbo.Categories(category_id),
    CONSTRAINT fk_products_brand    FOREIGN KEY (brand_id)    REFERENCES dbo.Brands(brand_id)
);

CREATE INDEX idx_products_category ON dbo.Products(category_id);
CREATE INDEX idx_products_brand    ON dbo.Products(brand_id);
CREATE INDEX idx_products_mainimg  ON dbo.Products(main_image_id);
GO

-- ProductImages
CREATE TABLE dbo.ProductImages (
    image_id    INT IDENTITY(1,1) PRIMARY KEY,
    product_id  INT NOT NULL,
    image_url   NVARCHAR(1024) NOT NULL,
    caption     NVARCHAR(255)  NULL,
	status      INT NOT NULL 
                   CONSTRAINT CK_ProductImages_status CHECK (status IN (0, 1))
                   CONSTRAINT DF_ProductImages_status DEFAULT (1), -- 1 = active
    created_at  DATETIME2(0)   NOT NULL CONSTRAINT DF_ProductImages_created_at DEFAULT (SYSDATETIME()),
    CONSTRAINT fk_images_product FOREIGN KEY (product_id) REFERENCES dbo.Products(product_id)
        ON UPDATE CASCADE ON DELETE CASCADE
);
CREATE INDEX idx_images_product ON dbo.ProductImages(product_id);
GO

-- Thêm FK cho main_image_id
ALTER TABLE dbo.Products
    ADD CONSTRAINT fk_products_main_image FOREIGN KEY (main_image_id)
        REFERENCES dbo.ProductImages(image_id)
        ON UPDATE NO ACTION ON DELETE NO ACTION;
GO

-- Discounts
CREATE TABLE dbo.Discounts (
    discount_id      INT IDENTITY(1,1) PRIMARY KEY,
    product_id       INT NOT NULL,
    discount_percent DECIMAL(5,2) NOT NULL
        CONSTRAINT CK_Discounts_percent_range CHECK (discount_percent BETWEEN 0 AND 100),
    start_date       DATE NOT NULL,
    end_date         DATE NOT NULL,
	status      INT NOT NULL 
                   CONSTRAINT CK_Discounts_status CHECK (status IN (0, 1))
                   CONSTRAINT DF_Discounts_status DEFAULT (1), -- 1 = active
    CONSTRAINT CK_Discounts_date_order CHECK (start_date <= end_date),
    CONSTRAINT fk_discount_product FOREIGN KEY (product_id) REFERENCES dbo.Products(product_id)
        ON UPDATE CASCADE ON DELETE CASCADE
);
CREATE INDEX idx_discounts_prod_dates ON dbo.Discounts(product_id, start_date, end_date);
GO

-- Banners
CREATE TABLE dbo.Banners (
    banner_id  INT IDENTITY(1,1) PRIMARY KEY,
    image_url  NVARCHAR(1024) NOT NULL,
    is_active  INT NOT NULL CONSTRAINT DF_Banners_is_active DEFAULT(1),
    created_at DATETIME2(0) NOT NULL CONSTRAINT DF_Banners_created_at DEFAULT (SYSDATETIME())
);
GO

-- ContactMessages
CREATE TABLE dbo.ContactMessages (
    message_id INT IDENTITY(1,1) PRIMARY KEY,
    name       NVARCHAR(100) NOT NULL,
    email      NVARCHAR(100) NOT NULL,
    phone      NVARCHAR(30)  NOT NULL,
    message    NVARCHAR(MAX) NOT NULL,
    created_at DATETIME2(0)  NOT NULL CONSTRAINT DF_ContactMessages_created_at DEFAULT (SYSDATETIME())
);
CREATE INDEX idx_contact_created ON dbo.ContactMessages(created_at);
GO

-- Posts
CREATE TABLE dbo.Posts (
    id           INT IDENTITY(1,1) PRIMARY KEY,
    author_id    INT NOT NULL,
    title        NVARCHAR(255) NOT NULL,
    main_image   NVARCHAR(1024) NULL,
    content      NVARCHAR(MAX) NOT NULL,
    caption      NVARCHAR(255) NULL,
	
    published_at DATETIME2(0) NULL,
	status      INT NOT NULL 
                   CONSTRAINT CK_Posts_status CHECK (status IN (0, 1))
                   CONSTRAINT DF_Posts_status DEFAULT (1), -- 1 = active
    created_at   DATETIME2(0) NOT NULL CONSTRAINT DF_Posts_created_at DEFAULT (SYSDATETIME()),
    updated_at   DATETIME2(0) NOT NULL CONSTRAINT DF_Posts_updated_at DEFAULT (SYSDATETIME()),
    CONSTRAINT fk_posts_author FOREIGN KEY (author_id) REFERENCES dbo.Admin(admin_id)
        ON UPDATE CASCADE ON DELETE NO ACTION
);
GO


/* ================================================
   3) Views
   ================================================ */
CREATE VIEW dbo.v_product_active_discount AS
SELECT p.product_id,
       MAX(CASE WHEN CAST(GETDATE() AS DATE) BETWEEN d.start_date AND d.end_date
                THEN d.discount_percent END) AS active_discount_percent
FROM dbo.Products AS p
LEFT JOIN dbo.Discounts AS d ON d.product_id = p.product_id
GROUP BY p.product_id;
GO

CREATE VIEW dbo.v_product_price AS
SELECT p.product_id,
       p.name,
       p.price,
       CAST(ROUND(COALESCE(p.price * (1 - (v.active_discount_percent/100.0)), p.price), 2) AS DECIMAL(10,2)) AS sale_price,
       v.active_discount_percent
FROM dbo.Products AS p
LEFT JOIN dbo.v_product_active_discount AS v ON v.product_id = p.product_id;
GO


/* ================================================
   4) Scalar functions
   ================================================ */
CREATE FUNCTION dbo.fn_check_discount_overlap
(
    @in_product_id INT,
    @in_start DATE,
    @in_end   DATE,
    @in_exclude_discount_id INT = NULL
)
RETURNS BIT
AS
BEGIN
    RETURN CASE WHEN EXISTS (
        SELECT 1 FROM dbo.Discounts d
        WHERE d.product_id = @in_product_id
          AND (@in_exclude_discount_id IS NULL OR d.discount_id <> @in_exclude_discount_id)
          AND NOT (@in_end < d.start_date OR @in_start > d.end_date)
    ) THEN 1 ELSE 0 END;
END;
GO

CREATE FUNCTION dbo.fn_is_product_image
(
    @in_product_id INT,
    @in_image_id   INT
)
RETURNS BIT
AS
BEGIN
    IF @in_image_id IS NULL RETURN 1;
    RETURN CASE WHEN EXISTS (
        SELECT 1 FROM dbo.ProductImages i
        WHERE i.image_id = @in_image_id AND i.product_id = @in_product_id
    ) THEN 1 ELSE 0 END;
END;
GO


/* ================================================
   5) Triggers
   ================================================ */
CREATE TRIGGER dbo.trg_Discounts_NoOverlap_INS
ON dbo.Discounts
AFTER INSERT
AS
BEGIN
    SET NOCOUNT ON;
    IF EXISTS (SELECT 1 FROM inserted i
               WHERE dbo.fn_check_discount_overlap(i.product_id, i.start_date, i.end_date, NULL) = 1)
        THROW 51001, N'Overlapping discount for this product', 1;
END;
GO

CREATE TRIGGER dbo.trg_Discounts_NoOverlap_UPD
ON dbo.Discounts
AFTER UPDATE
AS
BEGIN
    SET NOCOUNT ON;
    IF EXISTS (SELECT 1 FROM inserted i
               WHERE dbo.fn_check_discount_overlap(i.product_id, i.start_date, i.end_date, i.discount_id) = 1)
        THROW 51002, N'Overlapping discount for this product (update)', 1;
END;
GO

CREATE TRIGGER dbo.trg_Products_MainImg_Check_INS
ON dbo.Products
AFTER INSERT
AS
BEGIN
    SET NOCOUNT ON;
    IF EXISTS (SELECT 1 FROM inserted i
               WHERE dbo.fn_is_product_image(i.product_id, i.main_image_id) = 0)
        THROW 51003, N'main_image_id must belong to the same product', 1;
END;
GO

CREATE TRIGGER dbo.trg_Products_MainImg_Check_UPD
ON dbo.Products
AFTER UPDATE
AS
BEGIN
    SET NOCOUNT ON;
    IF EXISTS (SELECT 1 FROM inserted i
               WHERE dbo.fn_is_product_image(i.product_id, i.main_image_id) = 0)
        THROW 51004, N'main_image_id must belong to the same product', 1;
END;
GO

CREATE TRIGGER dbo.trg_Products_touch_updated_at
ON dbo.Products
AFTER UPDATE
AS
BEGIN
    SET NOCOUNT ON;
    UPDATE p SET updated_at = SYSDATETIME()
    FROM dbo.Products p
    JOIN inserted i ON p.product_id = i.product_id;
END;
GO

CREATE TRIGGER dbo.trg_Posts_touch_updated_at
ON dbo.Posts
AFTER UPDATE
AS
BEGIN
    SET NOCOUNT ON;
    UPDATE t SET updated_at = SYSDATETIME()
    FROM dbo.Posts t
    JOIN inserted i ON t.id = i.id;
END;
GO

/* ================================================
   6) Seed dữ liệu thật (demo)
   ================================================ */
-- Admin
INSERT INTO dbo.Admin (username, password_hash, email, full_name, phone) VALUES
(N'admin',   N'1', N'admin@vietphatdoor.com',  N'Nguyễn Văn A', N'0909123456'),
(N'manager', N'1', N'manager@vietphatdoor.com', N'Trần Thị B',   N'0912345678');

-- Thêm Categories
INSERT INTO dbo.Categories (category_name, description) VALUES
(N'Cửa cuốn', N'Các loại cửa cuốn tự động, cửa cuốn khe thoáng'),
(N'Cửa kính', N'Cửa kính cường lực, cửa kính trượt'),
(N'Cửa gỗ',   N'Cửa gỗ công nghiệp và gỗ tự nhiên');

-- Thêm Brands (gắn vào category_id)
-- Category 1: Cửa cuốn
INSERT INTO dbo.Brands (category_id, brand_name, description) VALUES
(1, N'Austdoor', N'Thương hiệu cửa cuốn hàng đầu Việt Nam'),
(1, N'Mitab',    N'Cửa cuốn Mitab cao cấp'),
(1, N'Bossdoor', N'Cửa cuốn Bossdoor – an toàn, thẩm mỹ');

-- Category 2: Cửa kính
INSERT INTO dbo.Brands (category_id, brand_name, description) VALUES
(2, N'PMI',        N'Nhôm PMI nhập khẩu Malaysia'),
(2, N'Xingfa',     N'Nhôm kính Xingfa Quảng Đông'),
(2, N'EuroWindow', N'Thương hiệu nhôm kính châu Âu');

-- Category 3: Cửa gỗ
INSERT INTO dbo.Brands (category_id, brand_name, description) VALUES
(3, N'Hoabinh',   N'Nội thất gỗ Hòa Bình, Việt Nam'),
(3, N'An Cường',  N'Ván gỗ công nghiệp An Cường'),
(3, N'Woodsland', N'Gỗ tự nhiên và nội thất Woodsland');

-- Products
INSERT INTO dbo.Products (category_id, brand_id, name, price, spec_html, status) VALUES
(1, 1, N'Cửa cuốn khe thoáng Austdoor A48i', 12500000,
 N'<ul><li>Kích thước tối đa: 6m</li><li>Chất liệu: Hợp kim nhôm</li></ul>', N'active'),
(2, 2, N'Cửa kính cường lực trượt lùa PMI',   7800000,
 N'<ul><li>Độ dày: 10mm</li><li>Khung nhôm PMI</li></ul>', N'active'),
(3, 3, N'Cửa gỗ công nghiệp MDF Veneer',      5200000,
 N'<ul><li>Kích thước: 900x2200mm</li><li>Bề mặt Veneer sồi</li></ul>', N'active');

-- ProductImages
INSERT INTO dbo.ProductImages (product_id, image_url, caption) VALUES
(1, N'https://vietphatdoor.com/images/a48i.jpg',       N'Mẫu cửa cuốn khe thoáng Austdoor A48i'),
(2, N'https://vietphatdoor.com/images/kinh-pmi.jpg',   N'Cửa kính cường lực trượt lùa PMI'),
(3, N'https://vietphatdoor.com/images/mdf-veneer.jpg', N'Cửa gỗ công nghiệp MDF Veneer');

-- Cập nhật main_image_id theo thứ tự đã chèn ảnh
UPDATE dbo.Products SET main_image_id = 1 WHERE product_id = 1;
UPDATE dbo.Products SET main_image_id = 2 WHERE product_id = 2;
UPDATE dbo.Products SET main_image_id = 3 WHERE product_id = 3;

-- Discounts (trigger sẽ chặn overlap)
INSERT INTO dbo.Discounts (product_id, discount_percent, start_date, end_date) VALUES
(1, 10, '2025-08-01', '2025-08-31'),
(3, 15, '2025-08-15', '2025-09-15');

-- Banners
INSERT INTO dbo.Banners (image_url, is_active) VALUES
(N'https://vietphatdoor.com/banners/banner1.jpg', 1),
(N'https://vietphatdoor.com/banners/banner2.jpg', 0);

-- ContactMessages
INSERT INTO dbo.ContactMessages (name, email, phone, message) VALUES
(N'Phạm Văn C', N'phamc@gmail.com', N'0988123456', N'Tôi muốn báo giá cửa cuốn Austdoor A48i'),
(N'Lê Thị D',   N'led@yahoo.com',   N'0934234567', N'Cho tôi xin catalog cửa kính PMI');

-- Posts
INSERT INTO dbo.Posts (author_id, title, main_image, content, caption, published_at) VALUES
(1, N'5 Lý do nên chọn cửa cuốn Austdoor', N'https://vietphatdoor.com/posts/austdoor.jpg',
 N'<p>Cửa cuốn Austdoor nổi bật với độ bền, an toàn và thẩm mỹ...</p>',
 N'Giới thiệu về ưu điểm cửa cuốn Austdoor', '2025-08-01T10:00:00'),
(2, N'Cửa kính cường lực – Xu hướng nội thất hiện đại', N'https://vietphatdoor.com/posts/kinh.jpg',
 N'<p>Cửa kính giúp không gian rộng mở và sang trọng...</p>',
 N'Giới thiệu cửa kính cường lực', '2025-08-10T09:30:00');
GO
