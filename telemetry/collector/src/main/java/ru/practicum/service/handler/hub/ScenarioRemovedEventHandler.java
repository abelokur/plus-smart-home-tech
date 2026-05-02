package ru.practicum.service.handler.hub;

import org.springframework.stereotype.Component;
import ru.practicum.service.KafkaEventProducer;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioRemovedEventProto;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;

import static ru.yandex.practicum.grpc.telemetry.event.HubEventProto.PayloadCase.SCENARIO_REMOVED;

/**
 * Обработчик событий удаления сценариев из хаба.
 * Преобразует ScenarioRemovedEventProto в Avro-формат и отправляет в Kafka топик TELEMETRY_HUBS.
 * Событие удаления сценария содержит информацию об имени удаляемого сценария.
 *
 * @see BaseHubEventHandler
 * @see ScenarioRemovedEventProto
 * @see ScenarioRemovedEventAvro
 */
@Component
public class ScenarioRemovedEventHandler extends BaseHubEventHandler<ScenarioRemovedEventAvro> {

    /**
     * Конструктор обработчика событий удаления сценариев.
     *
     * @param producer Kafka продюсер для отправки событий
     */
    public ScenarioRemovedEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    /**
     * Преобразует HubEventProto в ScenarioRemovedEventAvro.
     * Создает Avro-представление события удаления сценария на основе имени сценария.
     *
     * @param event событие удаления сценария, должно быть типа ScenarioRemovedEventProto
     * @return Avro-представление события удаления сценария
     */
    @Override
    protected ScenarioRemovedEventAvro mapToAvro(HubEventProto event) {
        if (event.getPayloadCase() == SCENARIO_REMOVED) {
            ScenarioRemovedEventProto scenarioRemoved = event.getScenarioRemoved();
            return ScenarioRemovedEventAvro.newBuilder()
                    .setName(scenarioRemoved.getName())
                    .build();
        } else {
            throw new IllegalArgumentException("Expected SCENARIO_REMOVED event type");
        }
    }

    /**
     * Возвращает тип обрабатываемого события.
     *
     * @return тип события SCENARIO_REMOVED
     */
    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return SCENARIO_REMOVED;
    }
}
