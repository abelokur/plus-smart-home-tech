package ru.practicum.model.hub.scenario;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Schema(description = "Условие сценария, которое содержит информацию о датчике, типе условия, операции и значении.")
public class ScenarioCondition {
    @NotBlank
    @Schema(description = "Идентификатор датчика, связанного с условием.")
    String sensorId;

    /**
     * Тип условия, определяющий какую характеристику датчика проверять.
     * Должен быть совместим с типом указанного датчика.
     */
    @NotNull
    @Schema(description = "Типы условий, которые могут использоваться в сценариях.",
            example = "MOTION, LUMINOSITY, SWITCH, TEMPERATURE, CO2LEVEL, HUMIDITY")
    ScenarioType type;

    /**
     * Операция сравнения для проверки условия.
     * Определяет как сравнивать текущее значение датчика с целевым значением.
     */
    @NotNull
    @Schema(description = "Операции, которые могут быть использованы в условиях.",
            example = "EQUALS, GREATER_THAN, LOWER_THAN")
    ScenarioOperation operation;

    /**
     * Целевое значение для сравнения в условии.
     * Используется вместе с операцией для определения выполнения условия.
     * Может быть null для некоторых типов условий (например, проверка наличия движения).
     */
    @Schema(description = "Значение, используемое в условии.")
    Integer value;
}
