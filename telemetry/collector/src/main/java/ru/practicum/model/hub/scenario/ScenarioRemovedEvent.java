package ru.practicum.model.hub.scenario;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import ru.practicum.model.hub.HubEvent;
import ru.practicum.model.hub.HubEventType;

@Getter
@Setter
@ToString(callSuper = true)
@Schema(description = "Событие удаления сценария из системы. Содержит информацию о названии удаленного сценария.")

public class ScenarioRemovedEvent extends HubEvent {

    @Length(min = 3)
    @Schema(description = "Название удаленного сценария. Должно содержать не менее 3 символов.")
    String name;

    @Schema(description = "Перечисление типов событий хаба.",
            example = "SCENARIO_REMOVED")
    HubEventType type = HubEventType.SCENARIO_REMOVED;
}
