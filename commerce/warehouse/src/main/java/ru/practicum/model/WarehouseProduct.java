package ru.practicum.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

/**
 * Сущность товара на складе.
 * Хранит информацию о конкретном экземпляре товара на складе,
 * включая количество, характеристики и местоположение.
 */
@Entity
@Table(name = "warehouse_products")
@ToString
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
public class WarehouseProduct {

    /**
     * Уникальный идентификатор записи товара на складе.
     */
    @Id
    @UuidGenerator
    @Column(name = "warehouse_product_id")
    @Schema(description = "Уникальный идентификатор записи товара на складе", accessMode = Schema.AccessMode.READ_ONLY)
    private UUID id;

    /**
     * Склад, на котором находится товар.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    @ToString.Exclude
    @Schema(description = "Склад, на котором находится товар")
    private Warehouse warehouse;

    /**
     * ID товара (ссылка на основной каталог товаров).
     */
    @Column(name = "product_id", nullable = false)
    @Schema(description = "Идентификатор товара в каталоге", format = "uuid")
    private UUID productId;

    /**
     * Количество товара на складе.
     */
    @Column(name = "quantity", nullable = false)
    @Schema(description = "Количество товара на складе", example = "100", minimum = "0")
    private Long quantity;

    /**
     * Хрупкость товара.
     */
    @Column(name = "fragile", nullable = false)
    @Builder.Default
    @Schema(description = "Признак хрупкости товара", example = "true", defaultValue = "false")
    private Boolean fragile = false;

    /**
     * Размеры товара.
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "dimensions_id", nullable = false)
    @ToString.Exclude
    @Schema(description = "Габаритные размеры товара")
    private Dimension dimensions;

    /**
     * Вес товара.
     */
    @Column(name = "weight", nullable = false)
    @Schema(description = "Вес товара в кг", example = "2.5", minimum = "0")
    private Double weight;

    /**
     * Дата поступления товара на склад.
     * Автоматически устанавливается при сохранении.
     */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    @Schema(description = "Дата и время поступления товара на склад", accessMode = Schema.AccessMode.READ_ONLY)
    private Instant createdAt;

    /**
     * Дата последнего обновления информации о товаре.
     * Автоматически обновляется при изменении.
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    @Schema(description = "Дата и время последнего обновления", accessMode = Schema.AccessMode.READ_ONLY)
    private Instant updatedAt;
}