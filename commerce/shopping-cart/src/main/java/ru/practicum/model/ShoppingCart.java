package ru.practicum.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Сущность корзины покупок пользователя.
 * Хранит информацию о товарах, добавленных пользователем для покупки.
 */
@Entity
@Table(name = "shopping_carts")
@ToString
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
public class ShoppingCart {

    /**
     * Уникальный идентификатор корзины.
     */
    @Id
    @UuidGenerator
    @Column(name = "shopping_cart_id", updatable = false, nullable = false)
    @Schema(description = "Уникальный идентификатор корзины", accessMode = Schema.AccessMode.READ_ONLY)
    private UUID shoppingCartId;

    /**
     * Имя пользователя-владельца корзины.
     */
    @Column(nullable = false)
    @Schema(description = "Имя пользователя", example = "ivan_ivanov", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    /**
     * Активность корзины.
     * true - активная корзина, false - неактивная/закрытая.
     */
    @Column(nullable = false)
    @Schema(description = "Активность корзины", example = "true", defaultValue = "true")
    private Boolean active;

    /**
     * Дата создания корзины.
     * Автоматически устанавливается при сохранении.
     */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    @Schema(description = "Дата и время создания корзины", accessMode = Schema.AccessMode.READ_ONLY)
    private Instant createdAt;

    /**
     * Дата последнего обновления корзины.
     * Автоматически обновляется при изменении.
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    @Schema(description = "Дата и время последнего обновления", accessMode = Schema.AccessMode.READ_ONLY)
    private Instant updatedAt;

    /**
     * Товары в корзине (ID товара → количество).
     */
    @ElementCollection
    @CollectionTable(name = "shopping_cart_items", joinColumns = @JoinColumn(name = "shopping_cart_id"))
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity", nullable = false)
    @BatchSize(size = 10)
    @ToString.Exclude
    @Builder.Default
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Schema(description = "Товары в корзине (ID товара → количество)")
    private Map<UUID, Long> products = new HashMap<>();
}