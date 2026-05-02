package ru.practicum.client.delivery;

import ru.practicum.exception.BaseErrorDecoder;

/**
 * Декодер ошибок для Feign клиента доставки.
 * <p>
 * Расширяет {@link BaseErrorDecoder} для обработки HTTP ошибок,
 * возникающих при вызове API доставки через Feign.
 */
public class DeliveryErrorDecoder extends BaseErrorDecoder {
    public DeliveryErrorDecoder() {
        super("delivery");
    }
}
