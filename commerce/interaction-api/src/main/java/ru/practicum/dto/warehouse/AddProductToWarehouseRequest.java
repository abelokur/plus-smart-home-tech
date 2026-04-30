package ru.practicum.dto.warehouse;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * Запрос на добавление товара на склад.
 * Содержит идентификатор товара и количество для добавления.
 *
 * @param productId идентификатор товара, не должен быть пустым
 * @param quantity  количество товара для добавления, должно быть больше 0
 */
@Schema(description = "Запрос на добавление товара на склад")
public record AddProductToWarehouseRequest(

        @NotNull
        @Schema(
                description = "Идентификатор товара",
                example = "123e4567-e89b-12d3-a456-426614174000",
                requiredMode = Schema.RequiredMode.REQUIRED,
                format = "uuid"
        )
        UUID productId,

        @NotNull
        @Min(1)
        @Schema(
                description = "Количество товара для добавления",
                example = "100",
                requiredMode = Schema.RequiredMode.REQUIRED,
                minimum = "1"
        )
        Long quantity
) {
}