package ru.practicum.dal.model.mapper;

import com.google.common.collect.Maps;
import com.google.protobuf.Timestamp;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.dal.model.*;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Маппер для преобразования Avro-событий в доменные объекты сценариев.
 */
@Slf4j
public class ScenarioMapper {

    /**
     * Преобразует Avro-событие в доменный объект сценария.
     *
     * @param hubId        ID хаба
     * @param scenarioAvro Avro-событие сценария
     * @return доменный объект сценария или null если scenarioAvro null
     */
    public static Scenario mapScenario(String hubId, ScenarioAddedEventAvro scenarioAvro) {
        if (scenarioAvro == null) {
            return null;
        }

        Map<String, Action> sensorActions = mapActions(scenarioAvro.getActions());
        Map<String, Condition> sensorConditions = mapConditions(scenarioAvro.getConditions());

        return Scenario.builder()
                .hubId(hubId)
                .name(scenarioAvro.getName())
                .sensorActions(sensorActions)
                .sensorConditions(sensorConditions)
                .build();
    }

    /**
     * Преобразует сценарий в список DeviceActionRequest - по одному запросу на каждое действие.
     *
     * @param scenario сценарий для преобразования
     * @return список DeviceActionRequest или пустой список, если сценарий null или не содержит действий
     */
    public static List<DeviceActionRequest> toDeviceActionRequest(Scenario scenario) {
        if (scenario == null || scenario.getSensorActions() == null || scenario.getSensorActions().isEmpty()) {
            return List.of();
        }

        // Преобразуем все действия сценария в DeviceActionProto
        List<DeviceActionProto> actionProtos = ActionMapper.toDeviceActionProto(scenario.getSensorActions());

        // Создаем отдельный DeviceActionRequest для каждого действия
        return actionProtos.stream()
                .filter(Objects::nonNull) // фильтруем null (если какое-то действие не сконвертировалось)
                .map(actionProto -> buildDeviceActionRequest(scenario, actionProto))
                .toList();
    }

    /**
     * Преобразует список Avro-условий в мапу сенсор->условие.
     *
     * @param conditionAvros список Avro-условий
     * @return мапа условий по ID сенсора или пустая мапа, если список условий null или пуст
     */
    private static Map<String, Condition> mapConditions(List<ScenarioConditionAvro> conditionAvros) {
        if (conditionAvros == null || conditionAvros.isEmpty()) {
            return Maps.newHashMap();
        }

        return conditionAvros.stream()
                .collect(Collectors.toMap(ScenarioConditionAvro::getSensorId,
                        ScenarioMapper::createCondition));
    }

    /**
     * Преобразует список Avro-действий в мапу сенсор->действие.
     *
     * @param actionAvros список Avro-действий
     * @return мапа действий по ID сенсора или пустая мапа, если список действий null или пуст
     */
    private static Map<String, Action> mapActions(List<DeviceActionAvro> actionAvros) {
        if (actionAvros == null || actionAvros.isEmpty()) {
            return Maps.newHashMap();
        }

        return actionAvros.stream()
                .collect(Collectors.toMap(DeviceActionAvro::getSensorId,
                        ScenarioMapper::createAction));
    }

    /**
     * Создает доменное условие из Avro-объекта.
     *
     * @param conditionAvro Avro-объект условия
     * @return доменное условие или null, если conditionAvro null
     */
    private static Condition createCondition(ScenarioConditionAvro conditionAvro) {
        if (conditionAvro == null) {
            return null;
        }

        return Condition.builder()
                .type(ConditionType.valueOf(conditionAvro.getType().name()))
                .operation(ConditionOperation.valueOf(conditionAvro.getOperation().name()))
                .value(extractIntegerValue(conditionAvro.getValue()))
                .build();
    }

    /**
     * Создает доменное действие из Avro-объекта.
     *
     * @param actionAvro Avro-объект действия
     * @return доменное действие или null, если actionAvro null
     */
    private static Action createAction(DeviceActionAvro actionAvro) {
        if (actionAvro == null) {
            return null;
        }

        return Action.builder()
                .type(ActionType.valueOf(actionAvro.getType().name()))
                .value(actionAvro.getValue())
                .build();
    }

    /**
     * Извлекает целочисленное значение из Avro-объекта.
     * Поддерживает Integer, Boolean, Long. Для остальных типов возвращает null.
     *
     * @param avroValue значение из Avro-объекта
     * @return целочисленное значение или null, если значение null или тип не поддерживается
     */
    private static Integer extractIntegerValue(Object avroValue) {
        if (avroValue == null) {
            return null;
        }

        try {
            return switch (avroValue) {
                case Integer i -> i;
                case Boolean b -> b ? 1 : 0;
                case Long l -> l.intValue();
                default -> {
                    log.warn("Unsupported value type: {}, value: {}", avroValue.getClass(), avroValue);
                    yield null;
                }
            };
        } catch (ClassCastException e) {
            log.error("Error extracting integer value from: {}", avroValue, e);
            return null;
        }
    }

    /**
     * Создает DeviceActionRequest из сценария и действия.
     *
     * @param scenario    сценарий
     * @param actionProto gRPC-прототип действия
     * @return построенный DeviceActionRequest с текущей меткой времени
     */
    private static DeviceActionRequest buildDeviceActionRequest(Scenario scenario, DeviceActionProto actionProto) {
        Instant now = Instant.now();
        return DeviceActionRequest.newBuilder()
                .setHubId(scenario.getHubId())
                .setScenarioName(scenario.getName())
                .setAction(actionProto)
                .setTimestamp(Timestamp.newBuilder()
                        .setSeconds(now.getEpochSecond())
                        .setNanos(now.getNano())
                        .build())
                .build();
    }
}
