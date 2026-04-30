package ru.practicum.dto.delivery;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import ru.practicum.dto.warehouse.AddressDto;

import java.util.UUID;

/**
 * DTO доставки заказа.
 * Содержит информацию о маршруте и статусе доставки.
 */
@Schema(description = "Информация о доставке заказа")
public record DeliveryDto(
        @Schema(
                description = "Уникальный идентификатор доставки",
                example = "123e4567-e89b-12d3-a456-426614174000",
                accessMode = Schema.AccessMode.READ_ONLY
        )
        UUID deliveryId,

        @NotNull
        @Schema(
                description = "Адрес отправления (обычно адрес склада)",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        AddressDto fromAddress,

        @NotNull
        @Schema(
                description = "Адрес доставки (адрес получателя)",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        AddressDto toAddress,

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
                description = "Текущий статус доставки",
                requiredMode = Schema.RequiredMode.REQUIRED,
                defaultValue = "CREATED"
        )
        DeliveryState deliveryState) {

    @Builder(toBuilder = true)
    public DeliveryDto {
        if (deliveryState == null) {
            deliveryState = DeliveryState.CREATED;
        }
    }
}