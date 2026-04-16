package ru.practicum.exception;

/**
 * Исключение при отсутствии товаров в корзине.
 */
public class NoProductsInShoppingCartException extends RuntimeException {
    public NoProductsInShoppingCartException(String message) {
        super(message);
    }
}