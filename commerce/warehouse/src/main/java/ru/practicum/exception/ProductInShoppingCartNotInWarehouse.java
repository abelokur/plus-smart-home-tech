package ru.practicum.exception;

/**
 * Исключение, выбрасываемое когда товар из корзины не найден на складе.
 * Используется при попытке заказать товар, который отсутствует
 * или недоступен в нужном количестве на складе.
 */
public class ProductInShoppingCartNotInWarehouse extends RuntimeException {
    public ProductInShoppingCartNotInWarehouse(String message) {
        super(message);
    }
}
