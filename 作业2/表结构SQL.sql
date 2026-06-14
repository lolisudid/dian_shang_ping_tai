-- ============================================
-- 电商购物平台 — 订单模块表结构 SQL
-- 兼容 SQLite / MySQL
-- ============================================

-- 用户表
CREATE TABLE IF NOT EXISTS sys_user (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    role TEXT NOT NULL DEFAULT 'user',
    create_time TEXT DEFAULT (datetime('now','localtime'))
);

-- 商品表
CREATE TABLE IF NOT EXISTS product (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    category TEXT,
    price REAL NOT NULL,
    stock INTEGER NOT NULL DEFAULT 0,
    image_url TEXT,
    description TEXT,
    deleted INTEGER NOT NULL DEFAULT 0,
    create_time TEXT DEFAULT (datetime('now','localtime')),
    update_time TEXT DEFAULT (datetime('now','localtime'))
);

-- 购物车表
CREATE TABLE IF NOT EXISTS cart_item (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    product_id INTEGER NOT NULL,
    quantity INTEGER NOT NULL DEFAULT 1,
    create_time TEXT DEFAULT (datetime('now','localtime')),
    UNIQUE(user_id, product_id)
);

-- 订单主表（核心）
CREATE TABLE IF NOT EXISTS shop_order (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    total_amount REAL NOT NULL,
    status TEXT NOT NULL DEFAULT 'PENDING',
    create_time TEXT DEFAULT (datetime('now','localtime')),
    update_time TEXT DEFAULT (datetime('now','localtime'))
);

-- 订单明细表
CREATE TABLE IF NOT EXISTS order_item (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    order_id INTEGER NOT NULL,
    product_id INTEGER NOT NULL,
    product_name TEXT NOT NULL,
    price REAL NOT NULL,
    quantity INTEGER NOT NULL
);

-- 评价表
CREATE TABLE IF NOT EXISTS product_review (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    product_id INTEGER NOT NULL,
    order_id INTEGER NOT NULL,
    rating INTEGER NOT NULL,
    content TEXT,
    create_time TEXT DEFAULT (datetime('now','localtime'))
);

-- 索引
CREATE INDEX IF NOT EXISTS idx_product_name ON product(name);
CREATE INDEX IF NOT EXISTS idx_product_category ON product(category);
CREATE INDEX IF NOT EXISTS idx_cart_user ON cart_item(user_id);
CREATE INDEX IF NOT EXISTS idx_order_user ON shop_order(user_id);
CREATE INDEX IF NOT EXISTS idx_order_item_order ON order_item(order_id);
CREATE INDEX IF NOT EXISTS idx_review_product ON product_review(product_id);
