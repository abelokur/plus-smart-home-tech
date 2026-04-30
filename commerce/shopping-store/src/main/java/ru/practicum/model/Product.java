package ru.practicum.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import ru.practicum.dto.product.ProductCategory;
import ru.practicum.dto.product.ProductState;
import ru.practicum.dto.product.QuantityState;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Сущность товара в магазине.
 * Хранит информацию о товаре, включая его характеристики, состояние и цену.
 */
@Entity
@Table(name = "products")
@ToString
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
public class Product {

    /**
     * Уникальный идентификатор товара.
     */
    @Id
    @UuidGenerator
    @Column(name = "product_id", updatable = false, nullable = false)
    @Schema(description = "Уникальный идентификатор товара", accessMode = Schema.AccessMode.READ_ONLY)
    private UUID productId;

    /**
     * Название товара.
     */
    @Column(name = "product_name", nullable = false)
    @Schema(description = "Название товара", example = "Умная лампа Philips Hue", requiredMode = Schema.RequiredMode.REQUIRED)
    private String productName;

    /**
     * Описание товара.
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    @Schema(description = "Подробное описание товара", example = "Умная LED лампа с регулировкой цвета и яркости", requiredMode = Schema.RequiredMode.REQUIRED)
    private String description;

    /**
     * Ссылка на изображение товара.
     */
    @Column(name = "image_src")
    @Schema(description = "Ссылка на изображение товара", example = "https://example.com/images/smart-lamp.jpg")
    private String imageSrc;

    /**
     * Состояние количества (в наличии/нет и т.д.).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "quantity_state", nullable = false)
    @Schema(description = "Состояние количества товара")
    private QuantityState quantityState;

    /**
     * Общее состояние товара.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "product_state", nullable = false)
    @Schema(description = "Общее состояние товара")
    private ProductState productState;

    /**
     * Категория товара.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "product_category", nullable = false)
    @Schema(description = "Категория товара")
    private ProductCategory productCategory;

    /**
     * Цена товара.
     */
    @Column(name = "price", precision = 10, scale = 2, nullable = false)
    @Schema(description = "Цена товара", example = "2999.99", requiredMode = Schema.RequiredMode.REQUIRED, minimum = "1")
    private BigDecimal price;

    /**
     * Дата создания записи.
     * Автоматически устанавливается при сохранении.
     */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    @Schema(description = "Дата и время создания записи", accessMode = Schema.AccessMode.READ_ONLY)
    private Instant createdAt;

    /**
     * Дата последнего обновления.
     * Автоматически обновляется при изменении.
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    @Schema(description = "Дата и время последнего обновления", accessMode = Schema.AccessMode.READ_ONLY)
    private Instant updatedAt;
}