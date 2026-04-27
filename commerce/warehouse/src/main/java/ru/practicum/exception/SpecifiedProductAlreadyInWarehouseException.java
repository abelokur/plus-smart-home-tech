package ru.practicum.exception;

/**
 * Исключение при попытке добавить уже существующий товар на склад.
 */
public class SpecifiedProductAlreadyInWarehouseException extends RuntimeException {
    public SpecifiedProductAlreadyInWarehouseException(String message) {
        super(message);
    }
}