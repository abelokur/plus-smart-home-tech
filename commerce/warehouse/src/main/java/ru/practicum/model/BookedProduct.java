package ru.practicum.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

/**
 * Сущность забронированного товара.
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
     * Уникальный идентификатор заказа.
     */
    @Id
    @UuidGenerator
    @Column(name = "order_id")
    private UUID orderId;

    /**
     * ID корзины покупок.
     */
    @Column(name = "shopping_cart_id", nullable = false)
    private UUID shoppingCartId;

    /**
     * Забронированный товар на складе.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_product_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private WarehouseProduct warehouseProduct;

    /**
     * Количество забронированного товара.
     */
    @Column(name = "quantity", nullable = false)
    private Long quantity;

    /**
     * Дата и время бронирования.
     */
    @CreationTimestamp
    @Column(name = "booked_at", updatable = false)
    private Instant bookedAt;
}