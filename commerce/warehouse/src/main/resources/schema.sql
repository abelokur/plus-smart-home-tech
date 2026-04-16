CREATE TABLE IF NOT EXISTS addresses
(
    address_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    country    VARCHAR(65),
    city       VARCHAR(50),
    street     VARCHAR(255),
    house      VARCHAR(20),
    flat       VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS dimensions
(
    dimensions_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    width         INT NOT NULL CHECK (width > 0),
    height        INT NOT NULL CHECK (height > 0),
    depth         INT NOT NULL CHECK (depth > 0)
);

CREATE TABLE IF NOT EXISTS warehouses
(
    warehouse_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name         VARCHAR(255),
    address_id   UUID REFERENCES addresses (address_id) NOT NULL,
    created_at   TIMESTAMP        DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS warehouse_products
(
    warehouse_product_id UUID PRIMARY KEY                                    DEFAULT gen_random_uuid(), -- НОВЫЙ суррогатный ключ
    warehouse_id         UUID REFERENCES warehouses (warehouse_id)  NOT NULL,
    product_id           UUID                                       NOT NULL,
    quantity             INTEGER                                    NOT NULL CHECK (quantity >= 0),
    fragile              BOOLEAN                                    NOT NULL DEFAULT false,
    dimensions_id        UUID REFERENCES dimensions (dimensions_id) NOT NULL,
    weight               FLOAT                                      NOT NULL CHECK (weight >= 0),
    created_at           TIMESTAMP                                           DEFAULT CURRENT_TIMESTAMP,
    updated_at           TIMESTAMP                                           DEFAULT CURRENT_TIMESTAMP,

    -- Уникальность сочетания склад+товар
    UNIQUE (warehouse_id, product_id)
);

CREATE TABLE IF NOT EXISTS booked_products
(
    order_id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    shopping_cart_id     UUID                                                      NOT NULL,
    warehouse_product_id UUID REFERENCES warehouse_products (warehouse_product_id) NOT NULL,
    quantity             INTEGER                                                   NOT NULL CHECK (quantity >= 0),
    booked_at            TIMESTAMP        DEFAULT CURRENT_TIMESTAMP
);

-- ИНДЕКСЫ ДЛЯ ОПТИМИЗАЦИИ:

-- Для быстрого поиска товаров на всех складах
CREATE INDEX IF NOT EXISTS idx_warehouse_products_product
    ON warehouse_products (product_id)
    WHERE quantity > 0;
-- Только доступные товары

-- Для поиска конкретного товара на конкретном складе
CREATE INDEX IF NOT EXISTS idx_warehouse_products_warehouse_product
    ON warehouse_products (warehouse_id, product_id);

-- Для поиска всех товаров на складе
CREATE INDEX IF NOT EXISTS idx_warehouse_products_warehouse
    ON warehouse_products (warehouse_id);

-- Для бронирований
CREATE INDEX IF NOT EXISTS idx_booked_products_cart
    ON booked_products (shopping_cart_id);

-- Для поиска бронирований по товару (через warehouse_products)
CREATE INDEX IF NOT EXISTS idx_booked_products_warehouse_product
    ON booked_products (warehouse_product_id);


