-- Таблица платежей
CREATE TABLE IF NOT EXISTS payments
(
    payment_id     UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id       UUID           NOT NULL,
    total_payment  DECIMAL(15, 2) NOT NULL,
    delivery_total DECIMAL(15, 2) NOT NULL,
    product_total  DECIMAL(15, 2) NOT NULL,
    tax_total      DECIMAL(15, 2) NOT NULL,
    payment_status VARCHAR(20)    NOT NULL,
    created_at     TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP        DEFAULT CURRENT_TIMESTAMP
);

