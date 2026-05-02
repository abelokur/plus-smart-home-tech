package ru.practicum.dto.warehouse;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * Запрос на отметку товаров как отгруженных для доставки.
 * Содержит идентификаторы заказа и доставки.
 */
@Schema(description = "Запрос на отметку товаров как отгруженных для доставки")
public record ShippedToDeliveryRequest(

        @NotNull
        @Schema(
                description = "Идентификатор заказа",
                example = "123e4567-e89b-12d3-a456-426614174000",
                requiredMode = Schema.RequiredMode.REQUIRED,
                format = "uuid"
        )
        UUID orderId,

        @NotNull
        @Schema(
                description = "Идентификатор доставки",
                example = "123e4567-e89b-12d3-a456-426614174000",
                requiredMode = Schema.RequiredMode.REQUIRED,
                format = "uuid"
        )
        UUID deliveryId
) {
}