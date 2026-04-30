package ru.practicum.client;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.practicum.annotation.validator.ValidCartItems;
import ru.practicum.dto.cart.ChangeProductQuantityRequest;
import ru.practicum.dto.cart.ShoppingCartDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Контракт для работы с корзиной покупок.
 * Используется как для контроллера, так и для Feign-клиента.
 */
public interface ShoppingCartClient {

    /**
     * Получает корзину пользователя.
     *
     * @param username имя пользователя
     * @return корзина с товарами
     */
    @GetMapping
    ShoppingCartDto getShoppingCart(@RequestParam String username);

    /**
     * Добавляет товары в корзину.
     *
     * @param username имя пользователя
     * @param items    товары для добавления (UUID → количество)
     * @return обновленная корзина
     */
    @PutMapping
    ShoppingCartDto addItemToShoppingCart(
            @RequestParam String username,
            @RequestBody @ValidCartItems Map<UUID, Long> items);

    /**
     * Очищает корзину пользователя.
     *
     * @param username имя пользователя
     */
    @DeleteMapping
    void deactivateShoppingCart(@RequestParam String username);

    /**
     * Удаляет товары из корзины.
     *
     * @param username имя пользователя
     * @param items    список ID товаров для удаления
     * @return обновленная корзина
     */
    @PostMapping("/remove")
    ShoppingCartDto removeItemFromShoppingCart(@RequestParam String username, @RequestBody List<UUID> items);

    /**
     * Изменяет количество товара в корзине.
     *
     * @param username имя пользователя
     * @param request  запрос с ID товара и новым количеством
     * @return обновленная корзина
     */
    @PostMapping("/change-quantity")
    ShoppingCartDto changeItemQuantity(
            @RequestParam String username,
            @RequestBody @Valid ChangeProductQuantityRequest request);
}