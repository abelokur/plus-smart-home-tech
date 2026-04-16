package ru.practicum.dto.product;

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

public record ProductDto(
        UUID productId,
        @NotBlank
        String productName,
        @NotBlank
        String description,
        String imageSrc,
        @NotNull
        QuantityState quantityState,
        @NotNull
        ProductState productState,
        @NotNull
        ProductCategory productCategory,
        @NotNull
        @Min(1)
        BigDecimal price
) {
    @Builder
    public ProductDto {
    }
}
