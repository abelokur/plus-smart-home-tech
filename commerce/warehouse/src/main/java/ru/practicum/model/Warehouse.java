package ru.practicum.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Сущность склада.
 */
@Entity
@Table(name = "warehouses")
@ToString
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
public class Warehouse {

    /**
     * Уникальный идентификатор склада.
     */
    @Id
    @UuidGenerator
    @Column(name = "warehouse_id", updatable = false, nullable = false)
    private UUID warehouseId;

    /**
     * Название склада.
     */
    private String name;

    /**
     * Адрес склада.
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    /**
     * Дата создания склада.
     */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    /**
     * Товары на складе.
     */
    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    private List<WarehouseProduct> products = new ArrayList<>();
}