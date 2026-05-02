package ru.practicum.service.handler.hub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecord;
import ru.practicum.config.TopicType;
import ru.practicum.service.KafkaEventProducer;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import static ru.practicum.util.Converter.timestampToInstant;

/**
 * Абстрактный базовый класс для обработчиков событий хаба.
 * Предоставляет общую логику преобразования HubEvent в Avro-формат и отправки в Kafka.
 *
 * @param <T> тип Avro-события, реализующий {@link SpecificRecord}, который будет отправлен как payload
 * @see HubEventHandler
 * @see KafkaEventProducer
 * @see HubEventAvro
 */
@Slf4j
@RequiredArgsConstructor
public abstract class BaseHubEventHandler<T extends SpecificRecord> implements HubEventHandler {

    private final KafkaEventProducer producer;
    private static final TopicType TOPIC_TYPE = TopicType.TELEMETRY_HUBS;

    /**
     * Преобразует HubEventProto в соответствующий Avro-объект.
     * Реализация должна быть предоставлена конкретными классами-наследниками.
     *
     * @param event исходное событие хаба для преобразования
     * @return Avro-представление события, не должно быть null
     * @throws IllegalArgumentException если event содержит некорректные данные
     *                                  для преобразования в целевой Avro-тип
     */
    protected abstract T mapToAvro(HubEventProto event);

    /**
     * Обрабатывает событие хаба: преобразует в Avro-формат и отправляет в Kafka.
     * <p>
     * Создает обертку {@link HubEventAvro} с основными метаданными события и payload,
     * полученным из {@link #mapToAvro(HubEventProto)}.
     * Отправляет событие в топик {@link TopicType#TELEMETRY_HUBS}.
     * </p>
     *
     * @param event событие хаба для обработки
     * @throws RuntimeException         если произошла ошибка при отправке события в Kafka
     * @throws IllegalArgumentException если event равен null
     * @see #mapToAvro(HubEventProto)
     */
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
