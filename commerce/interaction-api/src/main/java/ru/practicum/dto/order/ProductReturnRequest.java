package ru.practicum.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.Map;
import java.util.UUID;

/**
 * Запрос на возврат товаров из заказа.
 * Содержит информацию о заказе и товарах для возврата.
 */
@Schema(description = "Запрос на возврат товаров из заказа")
public record ProductReturnRequest(

        @Schema(
                description = "Идентификатор заказа для возврата",
                example = "123e4567-e89b-12d3-a456-426614174000",
                format = "uuid"
        )
        UUID orderId,

        @NotNull
        @Schema(
                description = "Товары для возврата (ID товара → количество)",
                example = "{\"f47ac10b-58cc-4372-a567-0e02b2c3d479\": 1, \"550e8400-e29b-41d4-a716-446655440000\": 2}",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        Map<UUID, Long> products
) {
}