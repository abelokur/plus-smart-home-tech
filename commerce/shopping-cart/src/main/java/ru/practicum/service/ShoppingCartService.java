package ru.practicum.service;

import ru.practicum.dto.cart.ChangeProductQuantityRequest;
import ru.practicum.dto.cart.ShoppingCartDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Сервис для управления корзиной покупок.
 */
public interface ShoppingCartService {

    /**
     * Получает корзину пользователя.
     *
     * @param username имя пользователя
     * @return корзина пользователя
     */
    ShoppingCartDto getShoppingCartByUsername(String username);

    /**
     * Добавляет товары в корзину.
     *
     * @param username имя пользователя
     * @param products товары для добавления
     * @return обновленная корзина
     */
    ShoppingCartDto addItemToShoppingCart(String username, Map<UUID, Long> products);

    /**
     * Деактивирует корзину пользователя.
     *
     * @param username имя пользователя
     */
    void deactivateShoppingCartByUsername(String username);

    /**
     * Удаляет товары из корзины.
     *
     * @param username имя пользователя
     * @param products товары для удаления
     * @return обновленная корзина
     */
    ShoppingCartDto removeItemFromShoppingCart(String username, List<UUID> products);

    /**
     * Изменяет количество товара в корзине.
     *
     * @param username имя пользователя
     * @param request  запрос на изменение количества
     * @return обновленная корзина
     */
    ShoppingCartDto changeItemQuantity(String username, ChangeProductQuantityRequest request);
}