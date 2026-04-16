package ru.practicum.dto.warehouse;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * DTO для представления габаритов товара.
 * Каждый параметр (ширина, высота, глубина) должен быть положительным числом не менее 1.
 *
 * @param width  ширина товара, минимальное значение 1
 * @param height высота товара, минимальное значение 1
 * @param depth  глубина товара, минимальное значение 1
 */
public record DimensionDto(
        @NotNull
        @Min(1)
        Double width,
        @NotNull
        @Min(1)
        Double height,
        @NotNull
        @Min(1)
        Double depth
) {
}
