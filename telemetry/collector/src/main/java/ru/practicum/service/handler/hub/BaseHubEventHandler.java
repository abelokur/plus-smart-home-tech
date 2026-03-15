package ru.practicum.service.handler.hub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecord;
import ru.practicum.config.TopicType;
import ru.practicum.model.hub.HubEvent;
import ru.practicum.service.KafkaEventProducer;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

@Slf4j
@RequiredArgsConstructor
public abstract class BaseHubEventHandler<T extends SpecificRecord> implements HubEventHandler {
    private final KafkaEventProducer producer;
    private static final TopicType TOPIC_TYPE = TopicType.TELEMETRY_HUBS;

    protected abstract T mapToAvro(HubEvent event);

    @Override
    public void handle(HubEvent event) {
        if (event == null) {
            throw new IllegalArgumentException("HubEvent cannot be null");
        }

        T avroEvent = mapToAvro(event);

        HubEventAvro hubEventAvro = HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(avroEvent)
                .build();

        try {
            producer.sendEvent(TOPIC_TYPE, event.getHubId(), hubEventAvro);
        } catch (Exception e) {
            log.error("Error processing HubEvent. HubEventAvro: {}", hubEventAvro, e);
            throw new RuntimeException("Failed to process HubEvent", e);
        }
    }
}
