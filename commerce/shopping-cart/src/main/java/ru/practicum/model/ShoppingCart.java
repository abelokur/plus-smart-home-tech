package ru.practicum.model;

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
    private UUID shoppingCartId;

    /**
     * Имя пользователя-владельца корзины.
     */
    @Column(nullable = false)
    private String username;

    /**
     * Активность корзины.
     */
    @Column(nullable = false)
    private Boolean active;

    /**
     * Дата создания корзины.
     */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    /**
     * Дата последнего обновления корзины.
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
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
    private Map<UUID, Long> products = new HashMap<>();
}