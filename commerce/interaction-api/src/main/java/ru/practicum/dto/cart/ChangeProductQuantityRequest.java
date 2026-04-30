package ru.practicum.dto.cart;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * Запрос на изменение количества товара в корзине.
 *
 * @param productId   идентификатор товара, количество которого нужно изменить, не должен быть пустым
 * @param newQuantity новое количество товара, не должно быть null
 */
@Schema(description = "Запрос на изменение количества товара в корзине")
public record ChangeProductQuantityRequest(
        @NotNull
        @Schema(
                description = "Идентификатор товара",
                example = "123e4567-e89b-12d3-a456-426614174000",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        UUID productId,

        @NotNull
        @Schema(
                description = "Новое количество товара",
                example = "5",
                requiredMode = Schema.RequiredMode.REQUIRED,
                minimum = "1"
        )
        Long newQuantity
) {
}