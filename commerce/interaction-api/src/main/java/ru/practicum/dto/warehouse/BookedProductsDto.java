package ru.practicum.dto.warehouse;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

/**
 * Объект передачи данных (DTO) для представления забронированных товаров с их характеристиками доставки.
 * Эта запись инкапсулирует информацию о весе, объеме и хрупкости товаров,
 * которые были забронированы для доставки.
 *
 * @param deliveryWeight вес доставки, не должен быть null
 * @param deliveryVolume объем доставки, не должен быть null
 * @param fragile        указывает, является ли доставка хрупкой, не должен быть null
 */
@Schema(description = "Характеристики забронированных товаров для доставки")
public record BookedProductsDto(

        @NotNull
        @Schema(
                description = "Общий вес доставки в кг",
                example = "5.2",
                requiredMode = Schema.RequiredMode.REQUIRED,
                minimum = "0"
        )
        Double deliveryWeight,

        @NotNull
        @Schema(
                description = "Общий объем доставки в м³",
                example = "0.05",
                requiredMode = Schema.RequiredMode.REQUIRED,
                minimum = "0"
        )
        Double deliveryVolume,

        @NotNull
        @Schema(
                description = "Признак хрупкости груза",
                example = "true",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        Boolean fragile
) {

    @Builder
    public BookedProductsDto {
    }
}