package ru.practicum.dal.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.util.HashMap;
import java.util.Map;

/**
 * Сущность сценария автоматизации умного дома.
 * Хранится в таблице "scenarios" базы данных.
 * Сценарий связывает условия (триггеры) с действиями через сенсоры.
 */
@Entity
@Table(name = "scenarios")
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Scenario {

    /**
     * Уникальный идентификатор сценария.
     * Генерируется автоматически при сохранении.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Идентификатор хаба, к которому привязан сценарий.
     * Обязательное поле, не может быть пустым.
     */
    @Column(name = "hub_id", nullable = false)
    @NotBlank
    private String hubId;

    /**
     * Название сценария.
     * Обязательное поле, не может быть пустым.
     */
    @Column(nullable = false)
    @NotBlank
    private String name;

    /**
     * Условия сценария, сгруппированные по Id сенсоров.
     * Каждый сенсор может иметь одно условие для активации сценария.
     * Хранится в связующей таблице "scenario_conditions".
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @MapKeyColumn(
            table = "scenario_conditions",
            name = "sensor_id")
    @JoinTable(
            name = "scenario_conditions",
            joinColumns = @JoinColumn(name = "scenario_id"),
            inverseJoinColumns = @JoinColumn(name = "condition_id")
    )
    @BatchSize(size = 30)
    @ToString.Exclude
    @Builder.Default
    private Map<String, Condition> sensorConditions = new HashMap<>();

    /**
     * Действия сценария, сгруппированные по Id сенсоров.
     * Каждый сенсор может выполнять одно действие при активации сценария.
     * Хранится в связующей таблице "scenario_actions".
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @MapKeyColumn(
            table = "scenario_actions",
            name = "sensor_id")
    @JoinTable(
            name = "scenario_actions",
            joinColumns = @JoinColumn(name = "scenario_id"),
            inverseJoinColumns = @JoinColumn(name = "action_id")
    )
    @BatchSize(size = 15)
    @ToString.Exclude
    @Builder.Default
    private Map<String, Action> sensorActions = new HashMap<>();
}
