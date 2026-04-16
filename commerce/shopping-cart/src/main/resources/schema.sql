CREATE TABLE IF NOT EXISTS shopping_carts
(
    shopping_cart_id UUID PRIMARY KEY      DEFAULT gen_random_uuid(),
    username         VARCHAR(255) NOT NULL,
    active           BOOLEAN      NOT NULL DEFAULT true,
    created_at       TIMESTAMP             DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP             DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS shopping_cart_items
(
    shopping_cart_id UUID    NOT NULL REFERENCES shopping_carts (shopping_cart_id) ON DELETE CASCADE,
    product_id       UUID    NOT NULL,
    quantity         INTEGER NOT NULL CHECK (quantity > 0),
    PRIMARY KEY (shopping_cart_id, product_id)
);

CREATE INDEX IF NOT EXISTS idx_shopping_carts_username ON shopping_carts (username);
CREATE INDEX IF NOT EXISTS idx_shopping_carts_active ON shopping_carts (active);