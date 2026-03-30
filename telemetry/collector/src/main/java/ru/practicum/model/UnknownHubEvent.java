package ru.practicum.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.model.hub.HubEvent;
import ru.practicum.model.hub.HubEventType;

@Getter
@Setter
@ToString(callSuper = true)
public class UnknownHubEvent extends HubEvent  {
    @NotNull
    private final HubEventType type = HubEventType.UNKNOWN;

    @JsonIgnore
    private String originalType;

    @JsonCreator
    public UnknownHubEvent(@JsonProperty("type") String originalType) {
        this.originalType = originalType;
    }

    @Override
    public HubEventType getType() {
        return type;
    }
}
