package ru.practicum.service.mapper;

import ru.practicum.model.hub.scenario.ScenarioCondition;
import ru.yandex.practicum.kafka.telemetry.event.ConditionOperationAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;

import java.util.Collections;
import java.util.List;

public class ScenarioConditionMapper {
    public static ScenarioConditionAvro map(ScenarioCondition condition) {
        if (condition == null) return null;
        return ScenarioConditionAvro.newBuilder()
                .setOperation(EnumMapper.map(condition.getOperation(), ConditionOperationAvro.class))
                .setSensorId(condition.getSensorId())
                .setType(EnumMapper.map(condition.getType(), ConditionTypeAvro.class))
                .setValue(condition.getValue())
                .build();
    }

    public static List<ScenarioConditionAvro> map(List<ScenarioCondition> conditions) {
        if (conditions == null || conditions.isEmpty()) Collections.emptyList();
        return conditions.stream().map(ScenarioConditionMapper::map).toList();
    }
}
