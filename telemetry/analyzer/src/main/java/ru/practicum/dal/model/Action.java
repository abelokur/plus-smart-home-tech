package ru.practicum.dal.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Сущность действия, выполняемого сенсором в сценарии.
 * Хранится в таблице "actions" базы данных.
 */
@Entity
@Table(name = "actions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Action {

    /**
     * Уникальный идентификатор действия.
     * Генерируется автоматически при сохранении.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Тип действия (ACTIVATE, DEACTIVATE, INVERSE, SET_VALUE).
     * Обязательное поле.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActionType type;

    /**
     * Значение действия (например, уровень яркости, влажности, температура).
     * Может быть null если менять значение не нужно.
     */
    @Column
    private Integer value;
}
