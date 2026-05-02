package ru.practicum.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import ru.practicum.dto.delivery.DeliveryState;

import java.time.Instant;
import java.util.UUID;

/**
 * Сущность доставки заказа.
 * Хранит информацию о маршруте доставки и её статусе.
 */
@Entity
@Table(name = "deliveries")
@ToString
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
public class Delivery {

    /**
     * Уникальный идентификатор доставки.
     */
    @Id
    @UuidGenerator
    @Column(name = "delivery_id", updatable = false, nullable = false)
    @Schema(description = "Уникальный идентификатор доставки", accessMode = Schema.AccessMode.READ_ONLY)
    private UUID deliveryId;

    /**
     * Адрес отправления (обычно адрес склада).
     */
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "from_address_id", nullable = false)
    @Schema(description = "Адрес отправления")
    private Address fromAddress;

    /**
     * Адрес доставки (адрес получателя).
     */
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "to_address_id", nullable = false)
    @Schema(description = "Адрес доставки")
    private Address toAddress;

    /**
     * Идентификатор связанного заказа.
     */
    @Column(name = "order_id", nullable = false)
    @Schema(description = "Идентификатор заказа", format = "uuid")
    private UUID orderId;

    /**
     * Текущий статус доставки.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_state", nullable = false, length = 20)
    @Schema(description = "Статус доставки")
    private DeliveryState deliveryState;

    /**
     * Дата создания доставки.
     * Автоматически устанавливается при сохранении.
     */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    @Schema(description = "Дата и время создания доставки", accessMode = Schema.AccessMode.READ_ONLY)
    private Instant createdAt;

    /**
     * Дата последнего обновления информации о доставке.
     * Автоматически обновляется при изменении.
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    @Schema(description = "Дата и время последнего обновления", accessMode = Schema.AccessMode.READ_ONLY)
    private Instant updatedAt;
}