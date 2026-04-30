package ru.practicum.client.payment;

import ru.practicum.exception.BaseErrorDecoder;

/**
 * Декодер ошибок для Feign клиента оплаты.
 * <p>
 * Расширяет {@link BaseErrorDecoder} для обработки HTTP ошибок,
 * возникающих при вызове API оплаты через Feign.
 */
public class PaymentErrorDecoder extends BaseErrorDecoder {
    public PaymentErrorDecoder() {
        super("payment");
    }
}
