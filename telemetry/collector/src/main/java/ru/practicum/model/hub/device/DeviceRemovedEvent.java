package ru.practicum.model.hub.device;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.model.hub.HubEvent;
import ru.practicum.model.hub.HubEventType;

@Getter
@Setter
@ToString(callSuper = true)
@Schema(description = "Событие, сигнализирующее об удалении устройства из системы.")
public class DeviceRemovedEvent extends HubEvent {

    @NotBlank
    @Schema(description = "Идентификатор удаленного устройства.")
    private String id;

    @Schema(description = "Перечисление типов событий хаба.",
            example = "DEVICE_REMOVED")
    private final HubEventType type = HubEventType.DEVICE_REMOVED;
}
