package ru.practicum.service.handler.hub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecord;
import ru.practicum.config.TopicType;
import ru.practicum.model.hub.HubEvent;
import ru.practicum.service.KafkaEventProducer;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import static ru.practicum.util.Converter.timestampToInstant;

@Slf4j
@RequiredArgsConstructor
public abstract class BaseHubEventHandler<T extends SpecificRecord> implements HubEventHandler {
    private final KafkaEventProducer producer;
    private static final TopicType TOPIC_TYPE = TopicType.TELEMETRY_HUBS;

    protected abstract T mapToAvro(HubEventProto event);

    @Override
    public void handle(HubEventProto event) {
        if (event == null) {
            throw new IllegalArgumentException("HubEvent cannot be null");
        }

        T avroEvent = mapToAvro(event);

        HubEventAvro hubEventAvro = HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(timestampToInstant(event.getTimestamp()))
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
