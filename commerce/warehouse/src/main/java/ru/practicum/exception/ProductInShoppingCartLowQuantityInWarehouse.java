package ru.practicum.exception;

/**
 * Исключение при недостаточном количестве товара на складе.
 */
public class ProductInShoppingCartLowQuantityInWarehouse extends RuntimeException {
    public ProductInShoppingCartLowQuantityInWarehouse(String message) {
        super(message);
    }
}