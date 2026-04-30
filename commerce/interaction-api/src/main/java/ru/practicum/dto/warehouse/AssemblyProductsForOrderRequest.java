package ru.practicum.dto.warehouse;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.Map;
import java.util.UUID;

/**
 * Запрос на сборку товаров для заказа.
 * Содержит информацию о товарах и заказе для сборки.
 */
@Schema(description = "Запрос на сборку товаров для заказа")
public record AssemblyProductsForOrderRequest(

        @NotNull
        @Schema(
                description = "Товары для сборки (ID товара → количество)",
                example = "{\"f47ac10b-58cc-4372-a567-0e02b2c3d479\": 2, \"550e8400-e29b-41d4-a716-446655440000\": 1}",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        Map<UUID, Long> products,

        @NotNull
        @Schema(
                description = "Идентификатор заказа",
                example = "123e4567-e89b-12d3-a456-426614174000",
                requiredMode = Schema.RequiredMode.REQUIRED,
                format = "uuid"
        )
        UUID orderId
) {
}