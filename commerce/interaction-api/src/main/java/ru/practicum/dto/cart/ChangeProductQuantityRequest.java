package ru.practicum.dto.cart;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * Запрос на изменение количества товара в корзине.
 *
 * @param productId   идентификатор товара, количество которого нужно изменить, не должен быть пустым
 * @param newQuantity новое количество товара, не должно быть null
 */
public record ChangeProductQuantityRequest(
        @NotNull
        UUID productId,
        @NotNull
        Long newQuantity
) {
}
