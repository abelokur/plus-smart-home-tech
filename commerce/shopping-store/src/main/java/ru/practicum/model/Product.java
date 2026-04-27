package ru.practicum.model;

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
    private UUID productId;

    /**
     * Название товара.
     */
    @Column(name = "product_name", nullable = false)
    private String productName;

    /**
     * Описание товара.
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    /**
     * Ссылка на изображение товара.
     */
    @Column(name = "image_src")
    private String imageSrc;

    /**
     * Состояние количества (в наличии/нет и т.д.).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "quantity_state", nullable = false)
    private QuantityState quantityState;

    /**
     * Общее состояние товара.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "product_state", nullable = false)
    private ProductState productState;

    /**
     * Категория товара.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "product_category", nullable = false)
    private ProductCategory productCategory;

    /**
     * Цена товара.
     */
    @Column(name = "price", precision = 10, scale = 2, nullable = false)
    private BigDecimal price;

    /**
     * Дата создания записи.
     */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    /**
     * Дата последнего обновления.
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;
}