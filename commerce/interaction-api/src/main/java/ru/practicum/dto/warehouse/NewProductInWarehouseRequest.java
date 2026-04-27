package ru.practicum.dto.warehouse;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.hibernate.validator.constraints.UUID;

/**
 * Запрос на добавление нового товара на склад.
 *
 * @param productId идентификатор товара в БД, не должен быть пустым
 * @param fragile   признак хрупкости товара
 * @param dimension габариты товара, не должны быть null
 * @param weight    вес товара, не должен быть null и должен быть больше 0
 */
public record NewProductInWarehouseRequest(
        @NotBlank
        @UUID
        String productId,
        Boolean fragile,
        @NotNull
        DimensionDto dimension,
        @NotNull
        @Min(1)
        Double weight
) {
    @Builder
    public NewProductInWarehouseRequest {
    }
}
