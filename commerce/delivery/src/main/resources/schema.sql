-- Таблица адресов
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

-- Таблица доставок
CREATE TABLE IF NOT EXISTS deliveries
(
    delivery_id     UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id        UUID                                   NOT NULL,
    from_address_id UUID REFERENCES addresses (address_id) NOT NULL,
    to_address_id   UUID REFERENCES addresses (address_id) NOT NULL,
    delivery_state  VARCHAR(20)      DEFAULT 'CREATED'     NOT NULL,
    created_at      TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP        DEFAULT CURRENT_TIMESTAMP
);

