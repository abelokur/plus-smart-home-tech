package ru.practicum.model.hub.device;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Schema(description = "Представляет действие, которое должно быть выполнено устройством.")
public class DeviceAction {
    @NotBlank
    @Schema(description = "Идентификатор датчика, связанного с действием.")
    String sensorId;

    @NotNull
    @Schema(description = "Перечисление возможных типов действий при срабатывании условия активации сценария.",
            example = "ACTIVATE, DEACTIVATE, INVERSE, SET_VALUE")
    DeviceActionType type;

    @Schema(description = "Необязательное значение, связанное с действием.")
    Integer value;
}
