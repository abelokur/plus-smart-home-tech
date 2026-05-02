package ru.practicum.service.mapper;

import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.kafka.telemetry.event.ActionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;

import java.util.Collections;
import java.util.List;

/**
 * Маппер DeviceActionProto --> DeviceActionAvro
 * Работает как с одиночными объектами, так и со списком
 */
public class DeviceActionMapper {

    /**
     * Маппит DeviceActionProto в DeviceActionAvro
     *
     * @param action объект DeviceActionProto, может быть null
     * @return DeviceActionAvro, или null в случае если в параметры передан null
     */
    public static DeviceActionAvro fromProto(DeviceActionProto action) {
        if (action == null) return null;
        return DeviceActionAvro.newBuilder()
                .setSensorId(action.getSensorId())
                .setType(EnumMapper.map(action.getType(), ActionTypeAvro.class))
                .setValue(action.getValue())
                .build();
    }

    /**
     * Маппит список DeviceActionProto в список DeviceActionAvro
     *
     * @param actions список для маппинга, может быть null
     * @return неизменяемый список DeviceActionAvro, никогда не возвращает null
     */
    public static List<DeviceActionAvro> fromProto(List<DeviceActionProto> actions) {
        if (actions == null || actions.isEmpty()) return Collections.emptyList();
        return actions.stream().map(DeviceActionMapper::fromProto).toList();
    }
}
