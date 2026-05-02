package ru.practicum.dto.payment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO платежа.
 * Содержит информацию о платежной транзакции и ее составляющих.
 */
@Schema(description = "Информация о платеже")
public record PaymentDto(

        @Schema(
                description = "Уникальный идентификатор платежа",
                example = "123e4567-e89b-12d3-a456-426614174000",
                format = "uuid"
        )
        UUID paymentId,

        @Schema(
                description = "Общая сумма платежа",
                example = "1599.99",
                minimum = "0"
        )
        BigDecimal totalPayment,

        @Schema(
                description = "Стоимость доставки в платеже",
                example = "299.99",
                minimum = "0"
        )
        BigDecimal deliveryTotal,

        @Schema(
                description = "Налоги в платеже",
                example = "47.99",
                minimum = "0"
        )
        BigDecimal taxTotal,

        @Schema(
                description = "Стоимость товаров в платеже",
                example = "1099.99",
                minimum = "0"
        )
        BigDecimal productTotal,

        @Schema(
                description = "Статус платежа",
                example = "SUCCESS"
        )
        PaymentStatus paymentStatus
) {

    @Builder
    public PaymentDto {
    }
}