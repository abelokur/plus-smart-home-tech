package ru.practicum.model.hub;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.model.UnknownHubEvent;
import ru.practicum.model.hub.device.DeviceAddedEvent;
import ru.practicum.model.hub.device.DeviceRemovedEvent;
import ru.practicum.model.hub.scenario.ScenarioAddedEvent;
import ru.practicum.model.hub.scenario.ScenarioRemovedEvent;

import java.time.Instant;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        defaultImpl = UnknownHubEvent.class
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = DeviceAddedEvent.class, name = "DEVICE_ADDED"),
        @JsonSubTypes.Type(value = DeviceRemovedEvent.class, name = "DEVICE_REMOVED"),
        @JsonSubTypes.Type(value = ScenarioAddedEvent.class, name = "SCENARIO_ADDED"),
        @JsonSubTypes.Type(value = ScenarioRemovedEvent.class, name = "SCENARIO_REMOVED"),
})

@Getter
@Setter
@ToString
@Schema(description = "Базовый класс для реализации событий хаба.")
public abstract class HubEvent {

    @NotBlank
    @Schema(description = "Идентификатор хаба, связанный с событием.")
    private String hubId;

    @NotNull
    @Schema(description = "Временная метка события. По умолчанию устанавливается текущее время.")
    private Instant timestamp = Instant.now();

    @NotNull
    @Schema(description = "Перечисление типов событий хаба.")
    public abstract HubEventType getType();
}
