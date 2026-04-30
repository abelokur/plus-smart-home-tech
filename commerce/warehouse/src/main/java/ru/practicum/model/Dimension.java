package ru.practicum.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

/**
 * Сущность размеров товара.
 * Хранит габаритные размеры товара и предоставляет метод для расчета объема.
 */
@Entity
@Table(name = "dimensions")
@ToString
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
public class Dimension {

    /**
     * Уникальный идентификатор размеров.
     */
    @Id
    @UuidGenerator
    @Column(name = "dimensions_id", updatable = false, nullable = false)
    @Schema(description = "Уникальный идентификатор размеров", accessMode = Schema.AccessMode.READ_ONLY)
    private UUID dimensionId;

    /**
     * Ширина товара в см.
     */
    @Column(nullable = false)
    @Schema(description = "Ширина товара в см", example = "15.5", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double width;

    /**
     * Высота товара в см.
     */
    @Column(nullable = false)
    @Schema(description = "Высота товара в см", example = "10.2", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double height;

    /**
     * Глубина товара в см.
     */
    @Column(nullable = false)
    @Schema(description = "Глубина товара в см", example = "8.7", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double depth;

    /**
     * Рассчитывает объем товара.
     *
     * @return объем в см³ (глубина × ширина × высота)
     */
    @Schema(description = "Объем товара в см³", accessMode = Schema.AccessMode.READ_ONLY)
    public Double getVolume() {
        return depth * width * height;
    }
}