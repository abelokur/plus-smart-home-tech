package ru.practicum.service.mapper;

import ru.yandex.practicum.grpc.telemetry.event.ScenarioConditionProto;
import ru.yandex.practicum.kafka.telemetry.event.ConditionOperationAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;

import java.util.Collections;
import java.util.List;

/**
 * Маппер ScenarioConditionProto --> ScenarioConditionAvro
 * Работает как с одиночными объектами, так и со списком
 */
public class ScenarioConditionMapper {
    /**
     * Маппит ScenarioConditionProto в ScenarioConditionAvro
     *
     * @param condition объект ScenarioConditionProto, может быть null
     * @return ScenarioConditionAvro, или null в случае если в параметры передан null
     */
    public static ScenarioConditionAvro fromProto(ScenarioConditionProto condition) {
        if (condition == null) return null;
        return ScenarioConditionAvro.newBuilder()
                .setOperation(EnumMapper.map(condition.getOperation(), ConditionOperationAvro.class))
                .setSensorId(condition.getSensorId())
                .setType(EnumMapper.map(condition.getType(), ConditionTypeAvro.class))
                .setValue(extractValueFromOneOf(condition))
                .build();
    }

    /**
     * Маппит список ScenarioConditionProto в список ScenarioConditionAvro
     *
     * @param conditions список для маппинга, может быть null
     * @return неизменяемый список ScenarioConditionAvro, никогда не возвращает null
     */
    public static List<ScenarioConditionAvro> fromProto(List<ScenarioConditionProto> conditions) {
        if (conditions == null || conditions.isEmpty()) return Collections.emptyList();
        return conditions.stream().map(ScenarioConditionMapper::fromProto).toList();
    }

    /**
     *
     * @param condition объект у которого извлекается значение Value
     * @return Object одно из поддерживаемых схемой значения, или null
     * @throws IllegalArgumentException если извлекаемый тип не соответствует ни одному из указанных в схеме
     */
    private static Object extractValueFromOneOf(ScenarioConditionProto condition) {
        return switch (condition.getValueCase()) {
            case INT_VALUE -> condition.getIntValue();
            case BOOL_VALUE -> condition.getBoolValue();
            case VALUE_NOT_SET -> null;
            default -> throw new IllegalArgumentException("Unsupported value type: " + condition.getValueCase());
        };
    }
}
