package ru.practicum.exception;

/**
 * Исключение при отсутствии товара.
 */
public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String message) {
        super(message);
    }
}