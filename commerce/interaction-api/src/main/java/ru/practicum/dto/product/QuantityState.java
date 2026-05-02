package ru.practicum.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Перечисление, представляющее состояние количества товара.
 * Возможные значения: ЗАКОНЧИЛСЯ, МАЛО, ДОСТАТОЧНО, МНОГО.
 */
@Schema(description = "Состояние количества товара на складе")
public enum QuantityState {

    @Schema(description = "Товар закончился")
    ENDED,

    @Schema(description = "Товара мало (низкий остаток)")
    FEW,

    @Schema(description = "Товара достаточно")
    ENOUGH,

    @Schema(description = "Товара много")
    MANY
}