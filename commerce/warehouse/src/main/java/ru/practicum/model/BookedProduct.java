package ru.practicum.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

/**
 * Сущность забронированного товара.
 * Хранит информацию о товарах, зарезервированных для конкретного заказа.
 */
@Entity
@Table(name = "booked_products")
@ToString
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
public class BookedProduct {

    /**
     * Уникальный идентификатор забронированного товара.
     */
    @Id
    @UuidGenerator
    @Column(name = "booked_product_id", updatable = false, nullable = false)
    @Schema(description = "Уникальный идентификатор забронированного товара", accessMode = Schema.AccessMode.READ_ONLY)
    private UUID id;

    /**
     * Уникальный идентификатор заказа.
     */
    @Column(name = "order_id", nullable = false)
    @Schema(description = "Идентификатор заказа", format = "uuid")
    private UUID orderId;

    /**
     * Уникальный идентификатор доставки.
     */
    @Column(name = "delivery_id")
    @Schema(description = "Идентификатор доставки", format = "uuid")
    private UUID deliveryId;

    /**
     * Забронированный товар на складе.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_product_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Schema(description = "Забронированный товар на складе")
    private WarehouseProduct warehouseProduct;

    /**
     * Количество забронированного товара.
     */
    @Column(name = "quantity", nullable = false)
    @Schema(description = "Количество забронированного товара", example = "5", minimum = "1")
    private Long quantity;

    /**
     * Дата и время бронирования.
     * Автоматически устанавливается при сохранении.
     */
    @CreationTimestamp
    @Column(name = "booked_at", updatable = false)
    @Schema(description = "Дата и время бронирования", accessMode = Schema.AccessMode.READ_ONLY)
    private Instant bookedAt;
}