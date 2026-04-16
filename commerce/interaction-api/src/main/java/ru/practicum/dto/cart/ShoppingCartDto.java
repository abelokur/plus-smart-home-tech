package ru.practicum.dto.cart;

import jakarta.validation.constraints.NotNull;

import java.util.Map;
import java.util.UUID;

/**
 * DTO корзины покупок.
 * Содержит идентификатор корзины и список идентификаторов товаров.
 *
 * @param shoppingCartId идентификатор корзины, не должен быть пустым
 * @param products       список идентификаторов товаров в корзине, не должен быть null
 */
public record ShoppingCartDto(
        @NotNull
        UUID shoppingCartId,
        @NotNull
        Map<UUID, Long> products
) {
}
