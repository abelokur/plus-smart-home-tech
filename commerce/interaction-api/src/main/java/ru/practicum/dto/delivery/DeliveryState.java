package ru.practicum.dto.delivery;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Статус доставки заказа.
 * Описывает текущее состояние процесса доставки.
 */
@Schema(description = "Статус доставки заказа")
public enum DeliveryState {

    @Schema(description = "Доставка создана, ожидает обработки")
    CREATED,

    @Schema(description = "Доставка в процессе выполнения")
    IN_PROGRESS,

    @Schema(description = "Доставка успешно завершена")
    DELIVERED,

    @Schema(description = "Доставка не удалась")
    FAILED,

    @Schema(description = "Доставка отменена")
    CANCELLED
}