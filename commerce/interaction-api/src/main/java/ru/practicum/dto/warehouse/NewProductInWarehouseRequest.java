package ru.practicum.dto.warehouse;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Запрос на добавление нового товара на склад")
public record NewProductInWarehouseRequest(

        @NotBlank
        @UUID
        @Schema(
                description = "Идентификатор товара в системе",
                example = "123e4567-e89b-12d3-a456-426614174000",
                requiredMode = Schema.RequiredMode.REQUIRED,
                format = "uuid"
        )
        String productId,

        @Schema(
                description = "Признак хрупкости товара",
                example = "true",
                defaultValue = "false"
        )
        Boolean fragile,

        @NotNull
        @Schema(
                description = "Габариты товара",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        DimensionDto dimension,

        @NotNull
        @Min(1)
        @Schema(
                description = "Вес товара в кг",
                example = "2.5",
                requiredMode = Schema.RequiredMode.REQUIRED,
                minimum = "1"
        )
        Double weight
) {

    @Builder
    public NewProductInWarehouseRequest {
    }
}