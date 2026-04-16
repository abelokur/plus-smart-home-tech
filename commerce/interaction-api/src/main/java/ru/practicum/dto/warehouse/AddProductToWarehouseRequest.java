package ru.practicum.dto.warehouse;

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

public record AddProductToWarehouseRequest(
        @NotNull
        UUID productId,
        @NotNull
        @Min(1)
        Long quantity
) {
}
