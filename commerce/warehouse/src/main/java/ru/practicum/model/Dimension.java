package ru.practicum.model;

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
    private UUID dimensionId;

    /**
     * Ширина.
     */
    @Column(nullable = false)
    private Double width;

    /**
     * Высота.
     */
    @Column(nullable = false)
    private Double height;

    /**
     * Глубина.
     */
    @Column(nullable = false)
    private Double depth;

    /**
     * Рассчитывает объем товара.
     *
     * @return объем (глубина × ширина × высота)
     */
    public Double getVolume() {
        return depth * width * height;
    }
}