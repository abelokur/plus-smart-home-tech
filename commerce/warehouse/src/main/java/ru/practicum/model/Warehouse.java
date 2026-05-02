package ru.practicum.model;

import io.swagger.v3.oas.annotations.media.Schema;
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
 * Хранит информацию о складе, его адресе и товарах на складе.
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
    @Schema(description = "Уникальный идентификатор склада", accessMode = Schema.AccessMode.READ_ONLY)
    private UUID warehouseId;

    /**
     * Название склада.
     */
    @Schema(description = "Название склада", example = "Основной склад Москва")
    private String name;

    /**
     * Адрес склада.
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    @Schema(description = "Адрес склада")
    private Address address;

    /**
     * Дата создания склада.
     * Автоматически устанавливается при сохранении.
     */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    @Schema(description = "Дата и время создания склада", accessMode = Schema.AccessMode.READ_ONLY)
    private Instant createdAt;

    /**
     * Товары на складе.
     */
    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    @Schema(description = "Товары на складе")
    private List<WarehouseProduct> products = new ArrayList<>();
}