package ru.practicum.client;

import ru.practicum.exception.BaseErrorDecoder;

/**
 * Декодер ошибок для Feign клиента магазина.
 */
public class ShoppingStoreErrorDecoder extends BaseErrorDecoder {
    public ShoppingStoreErrorDecoder() {
        super("shopping-store");
    }
}