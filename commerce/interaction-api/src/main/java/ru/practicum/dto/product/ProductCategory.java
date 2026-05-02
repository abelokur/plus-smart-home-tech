package ru.practicum.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Перечисление категорий товаров.
 * Представляет различные типы категорий товаров, доступных в системе.
 */
@Schema(description = "Категории товаров")
public enum ProductCategory {

    @Schema(description = "Освещение")
    LIGHTING,

    @Schema(description = "Системы управления")
    CONTROL,

    @Schema(description = "Датчики и сенсоры")
    SENSORS
}