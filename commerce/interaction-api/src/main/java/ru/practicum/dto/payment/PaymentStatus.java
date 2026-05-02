package ru.practicum.dto.payment;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Статус платежа.
 * Определяет текущее состояние платежной транзакции.
 */
@Schema(description = "Статус платежа")
public enum PaymentStatus {
    @Schema(description = "Ожидает обработки")
    PENDING,

    @Schema(description = "Успешно выполнен")
    SUCCESS,

    @Schema(description = "Не удался")
    FAILED
}