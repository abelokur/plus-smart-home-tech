package ru.practicum.service.mapper;

import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.kafka.telemetry.event.ActionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;

import java.util.Collections;
import java.util.List;

public class DeviceActionMapper {
    public static DeviceActionAvro fromProto(DeviceActionProto action) {
        if (action == null) return null;
        return DeviceActionAvro.newBuilder()
                .setSensorId(action.getSensorId())
                .setType(EnumMapper.map(action.getType(), ActionTypeAvro.class))
                .setValue(action.getValue())
                .build();
    }

    public static List<DeviceActionAvro> fromProto(List<DeviceActionProto> actions) {
        if (actions == null || actions.isEmpty()) return Collections.emptyList();
        return actions.stream().map(DeviceActionMapper::fromProto).toList();
    }
}
