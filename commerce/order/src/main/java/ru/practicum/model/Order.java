package ru.practicum.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import ru.practicum.dto.order.OrderState;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

/**
 * Сущность заказа.
 * Хранит информацию о заказе пользователя, включая товары,
 * стоимость, статус и связанные идентификаторы.
 */
@Entity
@Table(name = "orders")
@ToString
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
public class Order {

    /**
     * Уникальный идентификатор заказа.
     */
    @Id
    @UuidGenerator
    @Column(name = "order_id", updatable = false, nullable = false)
    @Schema(description = "Уникальный идентификатор заказа", accessMode = Schema.AccessMode.READ_ONLY)
    private UUID orderId;

    /**
     * Идентификатор корзины покупок, из которой создан заказ.
     */
    @Column(name = "shopping_cart_id")
    @Schema(description = "Идентификатор корзины покупок", format = "uuid")
    private UUID shoppingCartId;

    /**
     * Товары в заказе (ID товара → количество).
     */
    @ElementCollection
    @CollectionTable(name = "orders_products",
            joinColumns = @JoinColumn(name = "order_id"))
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    @Schema(description = "Товары в заказе (ID товара → количество)")
    private Map<UUID, Long> products;

    /**
     * Идентификатор платежа.
     */
    @Column(name = "payment_id")
    @Schema(description = "Идентификатор платежа", format = "uuid")
    private UUID paymentId;

    /**
     * Идентификатор доставки.
     */
    @Column(name = "delivery_id")
    @Schema(description = "Идентификатор доставки", format = "uuid")
    private UUID deliveryId;

    /**
     * Текущий статус заказа.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "order_state")
    @Schema(description = "Текущий статус заказа")
    private OrderState state;

    /**
     * Вес доставки в кг.
     */
    @Column(name = "delivery_weight")
    @Schema(description = "Вес доставки в кг", example = "5.2", minimum = "0")
    private Double deliveryWeight;

    /**
     * Объем доставки в м³.
     */
    @Column(name = "delivery_volume")
    @Schema(description = "Объем доставки в м³", example = "0.05", minimum = "0")
    private Double deliveryVolume;

    /**
     * Признак хрупкости груза.
     */
    @Column(name = "fragile")
    @Schema(description = "Признак хрупкости груза", example = "true")
    private Boolean fragile;

    /**
     * Общая стоимость заказа.
     */
    @Column(name = "total_price", precision = 15, scale = 2)
    @Schema(description = "Общая стоимость заказа", example = "1599.99", minimum = "0")
    private BigDecimal totalPrice;

    /**
     * Стоимость доставки.
     */
    @Column(name = "delivery_price", precision = 15, scale = 2)
    @Schema(description = "Стоимость доставки", example = "299.99", minimum = "0")
    private BigDecimal deliveryPrice;

    /**
     * Стоимость товаров (без доставки).
     */
    @Column(name = "product_price", precision = 15, scale = 2)
    @Schema(description = "Стоимость товаров (без доставки)", example = "1300.00", minimum = "0")
    private BigDecimal productPrice;

    /**
     * Имя пользователя, создавшего заказ.
     */
    @Column(name = "username")
    @Schema(description = "Имя пользователя", example = "ivan_ivanov")
    private String username;

    /**
     * Адрес доставки заказа.
     */
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "delivery_address_id")
    @ToString.Exclude
    @Schema(description = "Адрес доставки заказа")
    private Address address;

    /**
     * Дата создания записи.
     * Автоматически устанавливается при сохранении.
     */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    @Schema(description = "Дата и время создания заказа", accessMode = Schema.AccessMode.READ_ONLY)
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