package ru.practicum.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO (Data Transfer Object) для представления информации о товаре.
 * Используется для передачи данных о продукте между слоями приложения.
 * Содержит все необходимые поля для описания товара, включая идентификатор,
 * название, описание, изображение, состояние количества, состояние продукта,
 * категорию и цену. Поля productName, description, quantityState,
 * productState, productCategory и price являются обязательными и проходят
 * валидацию. Поля productId и imageSrc не является обязательными.
 *
 * @param productId       уникальный идентификатор товара
 * @param productName     название товара, не должно быть пустым
 * @param description     описание товара, не должно быть пустым
 * @param imageSrc        ссылка на изображение товара, может быть null
 * @param quantityState   состояние количества товара, не должно быть null
 * @param productState    состояние товара (например, активен, неактивен), не должно быть null
 * @param productCategory категория товара, не должна быть null
 * @param price           цена товара, должна быть не менее 1, не должна быть null
 */
@Schema(description = "Информация о товаре")
public record ProductDto(

        @Schema(
                description = "Уникальный идентификатор товара",
                example = "123e4567-e89b-12d3-a456-426614174000",
                format = "uuid"
        )
        UUID productId,

        @NotBlank
        @Schema(
                description = "Название товара",
                example = "Умная лампа Philips Hue",
                requiredMode = Schema.RequiredMode.REQUIRED,
                minLength = 1,
                maxLength = 255
        )
        String productName,

        @NotBlank
        @Schema(
                description = "Подробное описание товара",
                example = "Умная LED лампа с регулировкой цвета и яркости",
                requiredMode = Schema.RequiredMode.REQUIRED,
                minLength = 1,
                maxLength = 1000
        )
        String description,

        @Schema(
                description = "Ссылка на изображение товара",
                example = "https://example.com/images/smart-lamp.jpg"
        )
        String imageSrc,

        @NotNull
        @Schema(
                description = "Состояние количества товара на складе",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        QuantityState quantityState,

        @NotNull
        @Schema(
                description = "Состояние товара (активен/неактивен)",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        ProductState productState,

        @NotNull
        @Schema(
                description = "Категория товара",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        ProductCategory productCategory,

        @NotNull
        @Min(1)
        @Schema(
                description = "Цена товара",
                example = "2999.99",
                requiredMode = Schema.RequiredMode.REQUIRED,
                minimum = "1"
        )
        BigDecimal price
) {

    @Builder
    public ProductDto {
    }
}