package ru.practicum.model.sensor;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@ToString(callSuper = true)
public class UnknownSensorEvent extends SensorEvent{

    @NotNull
    private final SensorEventType type = SensorEventType.UNKNOWN;

    @JsonIgnore
    private String originalType;

    @JsonCreator
    public UnknownSensorEvent(@JsonProperty("type") String originalType) {
        this.originalType = originalType;
    }
}
