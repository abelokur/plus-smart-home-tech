package ru.practicum.service.handler.sensor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecord;
import ru.practicum.config.TopicType;
import ru.practicum.service.KafkaEventProducer;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import static ru.practicum.util.Converter.timestampToInstant;

/**
 * Абстрактный базовый класс для обработчиков событий сенсоров.
 * Предоставляет общую логику преобразования SensorEvent в Avro-формат и отправки в Kafka.
 *
 * @param <T> тип Avro-события, реализующий {@link SpecificRecord}, который будет отправлен как payload
 * @see SensorEventHandler
 * @see KafkaEventProducer
 * @see SensorEventProto
 * @see SensorEventAvro
 */
@Slf4j
@RequiredArgsConstructor
public abstract class BaseSensorEventHandler<T extends SpecificRecord> implements SensorEventHandler {

    private final KafkaEventProducer producer;
    private static final TopicType TOPIC_TYPE = TopicType.TELEMETRY_SENSORS;

    /**
     * Преобразует SensorEventProto в соответствующий Avro-объект.
     * Реализация должна быть предоставлена конкретными классами-наследниками.
     *
     * @param event исходное событие сенсора для преобразования
     * @return Avro-представление события, не должно быть null
     * @throws IllegalArgumentException если event содержит некорректные данные
     *                                  для преобразования в целевой Avro-тип
     */
    protected abstract T mapToAvro(SensorEventProto event);

    /**
     * Обрабатывает событие сенсора: преобразует в Avro-формат и отправляет в Kafka.
     * <p>
     * Создает обертку {@link SensorEventAvro} с основными метаданными события и payload,
     * полученным из {@link #mapToAvro(SensorEventProto)}.
     * Отправляет событие в топик {@link TopicType#TELEMETRY_SENSORS}.
     * </p>
     *
     * @param event событие сенсора для обработки
     * @throws RuntimeException         если произошла ошибка при отправке события в Kafka
     * @throws IllegalArgumentException если event равен null
     * @see #mapToAvro(SensorEventProto)
     */
    @Override
    public void handle(SensorEventProto event) {
        if (event == null) {
            throw new IllegalArgumentException("SensorEvent cannot be null");
        }

        T avroEvent = mapToAvro(event);

        SensorEventAvro sensorEventAvro = SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(timestampToInstant(event.getTimestamp()))
                .setPayload(avroEvent)
                .build();

        try {
            producer.sendEvent(TOPIC_TYPE, event.getHubId(), sensorEventAvro);
        } catch (Exception e) {
            log.error("Error processing SensorEvent. SensorEventAvro: {}", sensorEventAvro, e);
            throw new RuntimeException("Failed to process SensorEvent", e);
        }
    }


}
