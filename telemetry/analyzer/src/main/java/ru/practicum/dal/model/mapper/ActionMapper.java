package ru.practicum.dal.model.mapper;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.dal.model.Action;
import ru.yandex.practicum.grpc.telemetry.event.ActionTypeProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Маппер для конвертации Action в gRPC-сообщения.
 */
@Slf4j
public class ActionMapper {
    /**
     * Конвертирует Action в DeviceActionProto.
     *
     * @param sensorId ID сенсора (не null)
     * @param action   действие для конвертации (не null)
     * @return gRPC-сообщение или null при ошибке конвертации
     */
    public static DeviceActionProto toDeviceActionProto(String sensorId, Action action) {
        if (action == null || sensorId == null) {
            return null;
        }

        try {
            return DeviceActionProto.newBuilder()
                    .setSensorId(sensorId)
                    .setType(
                            ActionTypeProto.valueOf(action.getType().toString())
                    )
                    .setValue(action.getValue())
                    .build();
        } catch (IllegalArgumentException e) {
            log.error("Unknown action type: {}. Available types: {}. Action: {}",
                    action.getType(),
                    Arrays.toString(ActionTypeProto.values()),
                    action);
            return null;
        }
    }

    /**
     * Конвертирует Map сенсоров и действий в список DeviceActionProto.
     *
     * @param stringActionMap Map, где ключ - ID сенсора, значение - действие
     * @return список gRPC-сообщений DeviceActionProto
     */
    public static List<DeviceActionProto> toDeviceActionProto(Map<String, Action> stringActionMap) {
        return stringActionMap.entrySet().stream()
                .map(entry -> toDeviceActionProto(entry.getKey(), entry.getValue()))
                .toList();
    }
}
