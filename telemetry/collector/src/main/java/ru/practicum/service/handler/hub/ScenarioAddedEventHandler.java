package ru.practicum.service.handler.hub;

import org.springframework.stereotype.Component;
import ru.practicum.model.hub.HubEvent;
import ru.practicum.model.hub.HubEventType;
import ru.practicum.model.hub.scenario.ScenarioAddedEvent;
import ru.practicum.service.KafkaEventProducer;
import ru.practicum.service.mapper.DeviceActionMapper;
import ru.practicum.service.mapper.ScenarioConditionMapper;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;

@Component
public class ScenarioAddedEventHandler extends BaseHubEventHandler<ScenarioAddedEventAvro> {
    public ScenarioAddedEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    /**
     * Преобразует HubEvent в ScenarioAddedEventAvro.
     * Выполняет маппинг действий устройств и условий сценария с использованием соответствующих мапперов.
     *
     * @param event событие добавления сценария, должно быть типа ScenarioAddedEvent
     * @return Avro-представление события добавления сценария
     * @throws ClassCastException если event не является ScenarioAddedEvent
     */
    @Override
    protected ScenarioAddedEventAvro mapToAvro(HubEvent event) {
        ScenarioAddedEvent _event = (ScenarioAddedEvent) event;
        return ScenarioAddedEventAvro.newBuilder()
                .setActions(DeviceActionMapper.map(_event.getActions()))
                .setConditions(ScenarioConditionMapper.map(_event.getConditions()))
                .setName(_event.getName())
                .build();
    }

    /**
     * Возвращает тип обрабатываемого события.
     *
     * @return тип события SCENARIO_ADDED
     */
    @Override
    public HubEventType getMessageType() {
        return HubEventType.SCENARIO_ADDED;
    }
}
