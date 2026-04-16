CREATE TABLE IF NOT EXISTS products
(
    product_id       UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    product_name     VARCHAR(255)   NOT NULL,
    description      TEXT           NOT NULL,
    image_src        VARCHAR(500),
    quantity_state   VARCHAR(20)    NOT NULL,
    product_state    VARCHAR(20)    NOT NULL,
    product_category VARCHAR(20)    NOT NULL,
    price            NUMERIC(10, 2) NOT NULL CHECK (price > 0),
    created_at       TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP        DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_products_category ON products (product_category);
CREATE INDEX IF NOT EXISTS idx_products_state ON products (product_state);
