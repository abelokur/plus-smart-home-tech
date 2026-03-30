package ru.practicum.model.sensor;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
@NotNull
@Schema(description = "Событие датчика температуры, " +
        "содержащее информацию о температуре в градусах Цельсия и Фаренгейта.")
public class TemperatureSensorEvent extends SensorEvent {

    @Schema(description = "Температура в градусах Цельсия.")
    int temperatureC;

    @Schema(description = "Температура в градусах Фаренгейта.")
    int temperatureF;

    @Schema(description = "Перечисление типов событий датчиков. " +
            "Определяет различные типы событий, которые могут быть связаны с датчиками.",
            example = "TEMPERATURE_SENSOR_EVENT")
    private final SensorEventType type = SensorEventType.TEMPERATURE_SENSOR_EVENT;
}
