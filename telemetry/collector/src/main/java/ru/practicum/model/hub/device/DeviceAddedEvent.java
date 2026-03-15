package ru.practicum.model.hub.device;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.model.hub.HubEvent;
import ru.practicum.model.hub.HubEventType;

@Getter
@Setter
@ToString(callSuper = true)
@Schema(description = "Событие, сигнализирующее о добавлении нового устройства в систему.")
public class DeviceAddedEvent extends HubEvent {

    @NotBlank
    @Schema(description = "Идентификатор добавленного устройства.")
    private String id;

    @NotNull
    @Schema(description = "Перечисление типов устройств, которые могут быть добавлены в систему.",
            example = "MOTION_SENSOR, TEMPERATURE_SENSOR, LIGHT_SENSOR, CLIMATE_SENSOR, SWITCH_SENSOR")
    private DeviceType deviceType;

    @Schema(description = "Перечисление типов событий хаба.",
            example = "DEVICE_ADDED")
    private final HubEventType type = HubEventType.DEVICE_ADDED;
}
