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
@Schema(description = "Событие датчика переключателя, содержащее информацию о текущем состоянии переключателя.")

public class SwitchSensorEvent extends SensorEvent {

    @Schema(description = "Текущее состояние переключателя. true - включен, false - выключен.")
    private boolean state;

    @Schema(description = "Перечисление типов событий датчиков. " +
            "Определяет различные типы событий, которые могут быть связаны с датчиками.",
            example = "SWITCH_SENSOR_EVENT")
    private final SensorEventType type = SensorEventType.SWITCH_SENSOR_EVENT;
}
