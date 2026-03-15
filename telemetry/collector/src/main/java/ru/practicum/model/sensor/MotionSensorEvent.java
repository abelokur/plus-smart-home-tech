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
@Schema(description = "Событие датчика движения.")
public class MotionSensorEvent extends SensorEvent {

    @Schema(description = "Качество связи.")
    private int linkQuality;

    @Schema(description = "Наличие/отсутствие движения.")
    private boolean motion;

    @Schema(description = "Напряжение.")
    private int voltage;

    @Schema(description = "Перечисление типов событий датчиков. " +
            "Определяет различные типы событий, которые могут быть связаны с датчиками.",
            example = "MOTION_SENSOR_EVENT")
    private final SensorEventType type = SensorEventType.MOTION_SENSOR_EVENT;

    @Override
    public SensorEventType getType() {
        return type;
    }
}
