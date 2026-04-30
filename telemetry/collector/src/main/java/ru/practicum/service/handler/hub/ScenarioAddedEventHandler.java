package ru.practicum.service.handler.hub;

import org.springframework.stereotype.Component;
import ru.practicum.service.KafkaEventProducer;
import ru.practicum.service.mapper.DeviceActionMapper;
import ru.practicum.service.mapper.ScenarioConditionMapper;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioAddedEventProto;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;

import static ru.yandex.practicum.grpc.telemetry.event.HubEventProto.PayloadCase.SCENARIO_ADDED;

/**
 * Обработчик событий добавления новых сценариев в хаб.
 * Преобразует ScenarioAddedEventProto в Avro-формат и отправляет в Kafka топик TELEMETRY_HUBS.
 * Сценарий включает в себя набор действий устройств и условий их выполнения.
 *
 * @see BaseHubEventHandler
 * @see ScenarioAddedEventProto
 * @see ScenarioAddedEventAvro
 * @see DeviceActionMapper
 * @see ScenarioConditionMapper
 */
@Component
public class ScenarioAddedEventHandler extends BaseHubEventHandler<ScenarioAddedEventAvro> {

    /**
     * Конструктор обработчика событий добавления сценариев.
     *
     * @param producer Kafka продюсер для отправки событий
     */
    public ScenarioAddedEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    /**
     * Преобразует HubEventProto в ScenarioAddedEventAvro.
     * Выполняет маппинг действий устройств и условий сценария с использованием соответствующих мапперов.
     *
     * @param event событие добавления сценария, должно быть типа ScenarioAddedEventProto
     * @return Avro-представление события добавления сценария
     */
    @Override
    protected ScenarioAddedEventAvro mapToAvro(HubEventProto event) {
        if (event.getPayloadCase() == SCENARIO_ADDED) {
            ScenarioAddedEventProto scenarioAdded = event.getScenarioAdded();
            return ScenarioAddedEventAvro.newBuilder()
                    .setActions(DeviceActionMapper.fromProto(scenarioAdded.getActionList()))
                    .setConditions(ScenarioConditionMapper.fromProto(scenarioAdded.getConditionList()))
                    .setName(scenarioAdded.getName())
                    .build();
        } else {
            throw new IllegalArgumentException("Expected SCENARIO_ADDED event type");
        }
    }

    /**
     * Возвращает тип обрабатываемого события.
     *
     * @return тип события SCENARIO_ADDED
     */
    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return SCENARIO_ADDED;
    }
}
