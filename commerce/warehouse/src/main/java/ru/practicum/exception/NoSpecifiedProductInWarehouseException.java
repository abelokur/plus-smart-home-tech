package ru.practicum.exception;

/**
 * Исключение при отсутствии указанного товара на складе.
 */
public class NoSpecifiedProductInWarehouseException extends RuntimeException {
    public NoSpecifiedProductInWarehouseException(String message) {
        super(message);
    }
}