-- Таблица адресов доставки заказов
CREATE TABLE IF NOT EXISTS addresses
(
    address_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    country    VARCHAR(100) NOT NULL,
    city       VARCHAR(100) NOT NULL,
    street     VARCHAR(200) NOT NULL,
    house      VARCHAR(50)  NOT NULL,
    flat       VARCHAR(20),
    created_at TIMESTAMP        DEFAULT CURRENT_TIMESTAMP
);

-- Таблица заказов
CREATE TABLE IF NOT EXISTS orders
(
    order_id            UUID PRIMARY KEY                                DEFAULT gen_random_uuid(),
    shopping_cart_id    UUID                                   NOT NULL,
    payment_id          UUID,
    delivery_id         UUID,
    order_state         VARCHAR(50)                            NOT NULL DEFAULT 'NEW',
    delivery_weight     DOUBLE PRECISION,
    delivery_volume     DOUBLE PRECISION,
    fragile             BOOLEAN                                         DEFAULT FALSE,
    total_price         DECIMAL(15, 2),
    delivery_price      DECIMAL(15, 2),
    product_price       DECIMAL(15, 2),
    username            VARCHAR(100)                           NOT NULL,
    delivery_address_id UUID REFERENCES addresses (address_id) NOT NULL,
    created_at          TIMESTAMP                                       DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP                                       DEFAULT CURRENT_TIMESTAMP
);

-- Таблица продуктов
CREATE TABLE IF NOT EXISTS orders_products
(
    order_id   UUID REFERENCES orders (order_id),
    product_id UUID   NOT NULL,
    quantity   BIGINT NOT NULL,
    CONSTRAINT PK_orders_products
        PRIMARY KEY (order_id, product_id)
);