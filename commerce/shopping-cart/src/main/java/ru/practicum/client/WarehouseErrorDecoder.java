package ru.practicum.client;

import org.springframework.stereotype.Component;
import ru.practicum.exception.BaseErrorDecoder;

/**
 * Декодер ошибок для Feign клиента склада.
 * <p>
 * Расширяет {@link BaseErrorDecoder} для обработки HTTP ошибок,
 * возникающих при вызове API склада через Feign.
 */
@Component
public class WarehouseErrorDecoder extends BaseErrorDecoder {
}