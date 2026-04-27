package ru.practicum.dal.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Сущность условия для триггера сценария.
 * Хранится в таблице "conditions" базы данных.
 */
@Entity
@Table(name = "conditions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Condition {

    /**
     * Уникальный идентификатор условия.
     * Генерируется автоматически при сохранении.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Тип условия (MOTION, LUMINOSITY, SWITCH, TEMPERATURE, CO2LEVEL, HUMIDITY).
     * Обязательное поле.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConditionType type;

    /**
     * Операция сравнения для условия (EQUALS, GREATER_THAN, LOWER_THAN).
     * Обязательное поле.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConditionOperation operation;

    /**
     * Пороговое значение для сравнения.
     * Может быть null для некоторых типов условий.
     */
    @Column
    private Integer value;
}
