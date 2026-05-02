package ru.practicum.dto.cart;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Map;
import java.util.UUID;

/**
 * DTO корзины покупок.
 * Содержит идентификатор корзины и список идентификаторов товаров.
 *
 * @param shoppingCartId идентификатор корзины, не должен быть пустым
 * @param products       список идентификаторов товаров в корзине, не должен быть null
 * @param username       имя пользователя, не должно быть пустым
 */
@Schema(description = "Корзина покупок пользователя")
public record ShoppingCartDto(
        @NotNull
        @Schema(
                description = "Уникальный идентификатор корзины",
                example = "123e4567-e89b-12d3-a456-426614174000",
                requiredMode = Schema.RequiredMode.REQUIRED,
                format = "uuid"
        )
        UUID shoppingCartId,

        @NotNull
        @Schema(
                description = "Товары в корзине (ID товара → количество)",
                example = "{\"f47ac10b-58cc-4372-a567-0e02b2c3d479\": 2, \"550e8400-e29b-41d4-a716-446655440000\": 1}",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        Map<UUID, Long> products,

        @NotBlank
        @Schema(
                description = "Имя пользователя, владельца корзины",
                example = "ivan_ivanov",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String username
) {
}