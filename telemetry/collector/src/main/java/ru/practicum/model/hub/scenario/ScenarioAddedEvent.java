package ru.practicum.model.hub.scenario;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.model.hub.HubEvent;
import ru.practicum.model.hub.HubEventType;
import org.hibernate.validator.constraints.Length;
import ru.practicum.model.hub.device.DeviceAction;

import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
@Schema(description = "Событие добавления сценария в систему. " +
        "Содержит информацию о названии сценария, условиях и действиях.")
public class ScenarioAddedEvent extends HubEvent {

    @Length(min = 3)
    @Schema(description = "Название добавленного сценария. " +
            "Должно содержать не менее 3 символов.")
    String name;

    @NotEmpty
    @Schema(description = "Список условий, которые связаны со сценарием. Не может быть пустым.")
    List<ScenarioCondition> conditions;

    @NotEmpty
    @Schema(description = "Список действий, которые должны быть выполнены в рамках сценария. Не может быть пустым.")
    List<DeviceAction> actions;

    @Schema(description = "Перечисление типов событий хаба.",
            example = "SCENARIO_ADDED")
    HubEventType type = HubEventType.SCENARIO_ADDED;
}
