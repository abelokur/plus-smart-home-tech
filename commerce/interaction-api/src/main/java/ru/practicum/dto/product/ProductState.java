package ru.practicum.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Перечисление, представляющее состояние товара.
 * ACTIVE: Товар активен и доступен для продажи.
 * DEACTIVATE: Товар деактивирован и не доступен для продажи.
 */
@Schema(description = "Состояние товара")
public enum ProductState {

    @Schema(description = "Товар активен и доступен для продажи")
    ACTIVE,

    @Schema(description = "Товар деактивирован и не доступен для продажи")
    DEACTIVATE
}