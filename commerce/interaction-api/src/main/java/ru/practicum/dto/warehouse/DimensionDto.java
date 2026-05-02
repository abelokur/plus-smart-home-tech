package ru.practicum.dto.warehouse;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Габариты товара")
public record DimensionDto(

        @NotNull
        @Min(1)
        @Schema(
                description = "Ширина товара в см",
                example = "15.5",
                requiredMode = Schema.RequiredMode.REQUIRED,
                minimum = "1"
        )
        Double width,

        @NotNull
        @Min(1)
        @Schema(
                description = "Высота товара в см",
                example = "10.2",
                requiredMode = Schema.RequiredMode.REQUIRED,
                minimum = "1"
        )
        Double height,

        @NotNull
        @Min(1)
        @Schema(
                description = "Глубина товара в см",
                example = "8.7",
                requiredMode = Schema.RequiredMode.REQUIRED,
                minimum = "1"
        )
        Double depth
) {
}