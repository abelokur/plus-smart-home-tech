package ru.practicum.client;

import ru.practicum.exception.BaseErrorDecoder;

/**
 * Декодер ошибок для Feign клиента склада.
 * <p>
 * Расширяет {@link BaseErrorDecoder} для обработки HTTP ошибок,
 * возникающих при вызове API склада через Feign.
 */
public class WarehouseErrorDecoder extends BaseErrorDecoder {
    public WarehouseErrorDecoder() {
        super("warehouse");
    }
}