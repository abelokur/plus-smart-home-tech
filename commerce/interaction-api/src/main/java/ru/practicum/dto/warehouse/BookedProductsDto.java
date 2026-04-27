package ru.practicum.dto.warehouse;

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

public record BookedProductsDto(
        @NotNull
        Double deliveryWeight,
        @NotNull
        Double deliveryVolume,
        @NotNull
        Boolean fragile
) {
    @Builder
    public BookedProductsDto {
    }
}
