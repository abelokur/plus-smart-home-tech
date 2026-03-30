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
@Schema(description = "Событие климатического датчика, содержащее информацию о температуре, влажности и уровне CO2.")

public class ClimateSensorEvent extends SensorEvent {

    @Schema(description = "Уровень температуры по шкале Цельсия.")
    private int temperatureC;

    @Schema(description = "Влажность.")
    private int humidity;

    @Schema(description = "Уровень CO2.")
    private int co2Level;

    @Schema(description = "Перечисление типов событий датчиков. " +
            "Определяет различные типы событий, которые могут быть связаны с датчиками.",
            example = "CLIMATE_SENSOR_EVENT")
    private final SensorEventType type = SensorEventType.CLIMATE_SENSOR_EVENT;

    @Override
    public SensorEventType getType() {
        return type;
    }
}
