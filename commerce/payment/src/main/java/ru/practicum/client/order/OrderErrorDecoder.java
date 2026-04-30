package ru.practicum.client.order;

import ru.practicum.exception.BaseErrorDecoder;

/**
 * Декодер ошибок для Feign клиента заказов.
 * <p>
 * Расширяет {@link BaseErrorDecoder} для обработки HTTP ошибок,
 * возникающих при вызове API заказов через Feign.
 */
public class OrderErrorDecoder extends BaseErrorDecoder {
    public OrderErrorDecoder() {
        super("order");
    }
}
