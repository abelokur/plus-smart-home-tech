package ru.practicum.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import ru.practicum.dto.payment.PaymentStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Сущность платежа.
 * Хранит информацию о платежной транзакции для заказа,
 * включая суммы и статус оплаты.
 */
@Entity
@Table(name = "payments")
@ToString
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
public class Payment {

    /**
     * Уникальный идентификатор платежа.
     */
    @Id
    @UuidGenerator
    @Column(name = "payment_id", updatable = false, nullable = false)
    @Schema(description = "Уникальный идентификатор платежа", accessMode = Schema.AccessMode.READ_ONLY)
    private UUID paymentId;

    /**
     * Идентификатор связанного заказа.
     */
    @Column(name = "order_id", nullable = false)
    @Schema(description = "Идентификатор заказа", format = "uuid")
    private UUID orderId;

    /**
     * Общая сумма платежа.
     */
    @Column(name = "total_payment", precision = 15, scale = 2)
    @Schema(description = "Общая сумма платежа", example = "1599.99", minimum = "0")
    private BigDecimal totalPayment;

    /**
     * Стоимость доставки в платеже.
     */
    @Column(name = "delivery_total", precision = 15, scale = 2)
    @Schema(description = "Стоимость доставки в платеже", example = "299.99", minimum = "0")
    private BigDecimal deliveryTotal;

    /**
     * Общая сумма налога в платеже.
     */
    @Column(name = "tax_total", precision = 15, scale = 2)
    @Schema(description = "Общая сумма налога в платеже", example = "47.99", minimum = "0")
    private BigDecimal taxTotal;

    /**
     * Стоимость товаров в платеже.
     */
    @Column(name = "product_total", precision = 15, scale = 2)
    @Schema(description = "Стоимость товаров в платеже", example = "1099.99", minimum = "0")
    private BigDecimal productTotal;

    /**
     * Текущий статус платежа.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    @Schema(description = "Текущий статус платежа")
    private PaymentStatus paymentStatus;

    /**
     * Дата создания записи.
     * Автоматически устанавливается при сохранении.
     */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    @Schema(description = "Дата и время создания платежа", accessMode = Schema.AccessMode.READ_ONLY)
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