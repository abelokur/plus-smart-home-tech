package ru.practicum.service.mapper;

import ru.practicum.model.hub.device.DeviceAction;
import ru.yandex.practicum.kafka.telemetry.event.ActionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;

import java.util.Collections;
import java.util.List;

public class DeviceActionMapper {
    public static DeviceActionAvro map(DeviceAction action) {
        if (action == null) return null;
        return DeviceActionAvro.newBuilder()
                .setSensorId(action.getSensorId())
                .setType(EnumMapper.map(action.getType(), ActionTypeAvro.class))
                .setValue(action.getValue())
                .build();
    }

    public static List<DeviceActionAvro> map(List<DeviceAction> actions) {
        if (actions == null || actions.isEmpty()) Collections.emptyList();
        return actions.stream().map(DeviceActionMapper::map).toList();
    }
}
