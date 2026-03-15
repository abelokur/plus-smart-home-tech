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
@Schema(description = "Событие датчика освещенности, содержащее информацию о качестве связи и уровне освещенности.")

public class LightSensorEvent extends SensorEvent {

    @Schema(description = "Качество связи.")
    private int linkQuality;

    @Schema(description = "Уровень освещенности.")
    private int luminosity;

    @Schema(description = "Перечисление типов событий датчиков. " +
            "Определяет различные типы событий, которые могут быть связаны с датчиками.",
            example = "LIGHT_SENSOR_EVENT")
    private final SensorEventType type = SensorEventType.LIGHT_SENSOR_EVENT;
}
