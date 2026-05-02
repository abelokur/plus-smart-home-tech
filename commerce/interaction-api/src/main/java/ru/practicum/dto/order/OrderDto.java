package ru.practicum.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

/**
 * DTO заказа.
 * Содержит полную информацию о заказе, включая статус, стоимость и связанные идентификаторы.
 */
@Schema(description = "Информация о заказе")
public record OrderDto(

        @NotNull
        @Schema(
                description = "Уникальный идентификатор заказа",
                example = "123e4567-e89b-12d3-a456-426614174000",
                requiredMode = Schema.RequiredMode.REQUIRED,
                format = "uuid"
        )
        UUID orderId,

        @Schema(
                description = "Идентификатор корзины, из которой создан заказ",
                example = "123e4567-e89b-12d3-a456-426614174000",
                format = "uuid"
        )
        UUID shoppingCartId,

        @NotNull
        @Schema(
                description = "Товары в заказе (ID товара → количество)",
                example = "{\"f47ac10b-58cc-4372-a567-0e02b2c3d479\": 2}",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        Map<UUID, Long> products,

        @Schema(
                description = "Идентификатор платежа",
                example = "123e4567-e89b-12d3-a456-426614174000",
                format = "uuid"
        )
        UUID paymentId,

        @Schema(
                description = "Идентификатор доставки",
                example = "123e4567-e89b-12d3-a456-426614174000",
                format = "uuid"
        )
        UUID deliveryId,

        @Schema(
                description = "Текущий статус заказа",
                defaultValue = "NEW"
        )
        OrderState state,

        @Schema(
                description = "Вес доставки в кг",
                example = "5.2",
                minimum = "0"
        )
        Double deliveryWeight,

        @Schema(
                description = "Объем доставки в м³",
                example = "0.05",
                minimum = "0"
        )
        Double deliveryVolume,

        @Schema(
                description = "Признак хрупкого груза",
                example = "true"
        )
        Boolean fragile,

        @Schema(
                description = "Общая стоимость заказа",
                example = "1599.99",
                minimum = "0"
        )
        BigDecimal totalPrice,

        @Schema(
                description = "Стоимость доставки",
                example = "299.99",
                minimum = "0"
        )
        BigDecimal deliveryPrice,

        @Schema(
                description = "Стоимость товаров (без доставки)",
                example = "1300.00",
                minimum = "0"
        )
        BigDecimal productPrice
) {

    @Builder
    public OrderDto {
    }
}