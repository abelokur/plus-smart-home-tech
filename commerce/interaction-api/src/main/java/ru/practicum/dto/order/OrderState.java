package ru.practicum.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Статус заказа.
 * Описывает текущее состояние заказа в процессе его обработки.
 */
@Schema(description = "Статус заказа")
public enum OrderState {

    @Schema(description = "Новый заказ")
    NEW,

    @Schema(description = "Ожидает оплаты")
    ON_PAYMENT,

    @Schema(description = "Ожидает доставки")
    ON_DELIVERY,

    @Schema(description = "Выполнен")
    DONE,

    @Schema(description = "Доставлен")
    DELIVERED,

    @Schema(description = "Собран")
    ASSEMBLED,

    @Schema(description = "Оплачен")
    PAID,

    @Schema(description = "Завершен")
    COMPLETED,

    @Schema(description = "Доставка не удалась")
    DELIVERY_FAILED,

    @Schema(description = "Сборка не удалась")
    ASSEMBLY_FAILED,

    @Schema(description = "Оплата не удалась")
    PAYMENT_FAILED,

    @Schema(description = "Товары возвращены")
    PRODUCT_RETURNED,

    @Schema(description = "Отменен")
    CANCELED
}