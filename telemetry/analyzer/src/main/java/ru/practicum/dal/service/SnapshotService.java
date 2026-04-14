package ru.practicum.dal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dal.model.Condition;
import ru.practicum.dal.model.ConditionType;
import ru.practicum.dal.model.Scenario;
import ru.practicum.dal.model.mapper.ScenarioMapper;
import ru.practicum.dal.repository.ScenarioRepository;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.util.List;

/**
 * Сервис для обработки снимков состояния сенсоров и активации сценариев.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SnapshotService {
    private final ScenarioRepository scenarioRepository;

    /**
     * Обрабатывает снимок состояния сенсоров и возвращает список действий для выполнения.
     * Находит все сценарии хаба, проверяет условия и возвращает действия для удовлетворенных сценариев.
     *
     * @param sensorsSnapshotAvro снимок состояния сенсоров
     * @return список запросов на выполнение действий
     */
    @Transactional(readOnly = true)
    public List<DeviceActionRequest> handleSnapshot(SensorsSnapshotAvro sensorsSnapshotAvro) {

        //находим все сценарии к хабу
        List<Scenario> scenarios = scenarioRepository.findByHubId(sensorsSnapshotAvro.getHubId());

        //переводим все сценарии в действия которые нужно исполнить в Proto схему DeviceActionProto
        return scenarios.stream()
                .filter(scenario -> isAllConditionSatisfied(sensorsSnapshotAvro, scenario))  // выбираем только те сценарии которые удовлетворяют всем условиям
                .map(ScenarioMapper::toDeviceActionRequest)
                .flatMap(List::stream)
                .toList();
    }

    /**
     * Проверяет выполнение условия для одного сенсора.
     *
     * @param sensorData данные сенсора
     * @param condition  условие для проверки
     * @return true если условие выполнено
     */
    private boolean isConditionSatisfied(SpecificRecordBase sensorData, Condition condition) {
        Integer sensorValue = extractSensorValue(sensorData, condition.getType());
        return sensorValue != null && compareWithOperation(sensorValue, condition);
    }

    /**
     * Проверяет выполнение всех условий сценария.
     *
     * @param sensorsSnapshotAvro снимок состояния всех сенсоров
     * @param scenario            сценарий для проверки
     * @return true если все условия сценария выполнены
     */
    private boolean isAllConditionSatisfied(SensorsSnapshotAvro sensorsSnapshotAvro, Scenario scenario) {
        return scenario.getSensorConditions().entrySet().stream()
                .allMatch(entry -> {
                    SensorStateAvro state = sensorsSnapshotAvro.getSensorsState().get(entry.getKey());
                    return state != null && isConditionSatisfied((SpecificRecordBase) state.getData(), entry.getValue());
                });
    }

    /**
     * Извлекает значение сенсора в зависимости от его типа.
     *
     * @param sensorData данные сенсора
     * @param type       тип условия
     * @return числовое значение сенсора или null если тип не поддерживается
     */
    private Integer extractSensorValue(SpecificRecordBase sensorData, ConditionType type) {
        return switch (type) {
            case TEMPERATURE -> {
                if (sensorData instanceof ClimateSensorAvro climate) yield climate.getTemperatureC();
                if (sensorData instanceof TemperatureSensorAvro temp) yield temp.getTemperatureC();
                yield null;
            }
            case HUMIDITY -> {
                if (sensorData instanceof ClimateSensorAvro climate) yield climate.getHumidity();
                yield null;
            }
            case CO2LEVEL -> {
                if (sensorData instanceof ClimateSensorAvro climate) yield climate.getCo2Level();
                yield null;
            }
            case LUMINOSITY -> {
                if (sensorData instanceof LightSensorAvro light) yield light.getLuminosity();
                yield null;
            }
            case MOTION -> {
                if (sensorData instanceof MotionSensorAvro motion) yield motion.getMotion() ? 1 : 0;
                yield null;
            }
            case SWITCH -> {
                if (sensorData instanceof SwitchSensorAvro switchSensor) yield switchSensor.getState() ? 1 : 0;
                yield null;
            }
        };
    }

    /**
     * Сравнивает значение сенсора с условием по указанной операции.
     *
     * @param sensorValue значение сенсора
     * @param condition   условие сравнения
     * @return true если сравнение успешно
     */
    private boolean compareWithOperation(Integer sensorValue, Condition condition) {
        return switch (condition.getOperation()) {
            case EQUALS -> sensorValue.equals(condition.getValue());
            case GREATER_THAN -> sensorValue > condition.getValue();
            case LOWER_THAN -> sensorValue < condition.getValue();
        };
    }
}
