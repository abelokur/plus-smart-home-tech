-- Вставка адреса с использованием CTE для случайного значения
WITH address_value AS (SELECT CASE WHEN random() < 0.5 THEN 'ADDRESS_1' ELSE 'ADDRESS_2' END as value)
INSERT
INTO addresses (address_id, country, city, street, house, flat)
SELECT 'a1b2c3d4-e5f6-7890-abcd-ef1234567890',
       value,
       value,
       value,
       value,
       value
FROM address_value
ON CONFLICT (address_id) DO NOTHING;

-- Вставка склада
INSERT INTO warehouses (warehouse_id, name, address_id)
VALUES ('b2c3d4e5-f6a7-8901-bcde-f23456789012',
        'Основной склад',
        'a1b2c3d4-e5f6-7890-abcd-ef1234567890')
ON CONFLICT (warehouse_id) DO NOTHING;