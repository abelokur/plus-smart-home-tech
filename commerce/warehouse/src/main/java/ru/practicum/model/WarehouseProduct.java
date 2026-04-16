package ru.practicum.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Сущность товара на складе.
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
    private UUID id;

    /**
     * Склад, на котором находится товар.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    @ToString.Exclude
    private Warehouse warehouse;

    /**
     * ID товара (ссылка на основной каталог товаров).
     */
    @Column(name = "product_id", nullable = false)
    private UUID productId;

    /**
     * Количество товара на складе.
     */
    @Column(name = "quantity", nullable = false)
    private Long quantity;

    /**
     * Хрупкость товара.
     */
    @Column(name = "fragile", nullable = false)
    @Builder.Default
    private Boolean fragile = false;

    /**
     * Размеры товара.
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "dimensions_id", nullable = false)
    private Dimension dimensions;

    /**
     * Вес товара.
     */
    @Column(name = "weight", nullable = false)
    private Double weight;

    /**
     * Дата поступления товара на склад.
     */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    /**
     * Дата последнего обновления информации о товаре.
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    /**
     * Бронирования этого товара.
     */
    @OneToMany(mappedBy = "warehouseProduct", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    private List<BookedProduct> bookings = new ArrayList<>();
}