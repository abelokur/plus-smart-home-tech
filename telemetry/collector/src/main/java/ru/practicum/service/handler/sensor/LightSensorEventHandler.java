package ru.practicum.service.handler.sensor;

import org.springframework.stereotype.Component;
import ru.practicum.service.KafkaEventProducer;
import ru.yandex.practicum.grpc.telemetry.event.LightSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;

import static ru.yandex.practicum.grpc.telemetry.event.SensorEventProto.PayloadCase.LIGHT_SENSOR;

/**
 * Обработчик событий датчиков освещенности.
 * Преобразует LightSensorProto в LightSensorAvro и отправляет в Kafka топик TELEMETRY_SENSORS.
 * Обрабатывает данные об уровне освещенности и качестве связи датчика.
 *
 * @see BaseSensorEventHandler
 * @see LightSensorProto
 * @see LightSensorAvro
 * @see SensorEventProto.PayloadCase#LIGHT_SENSOR
 */
@Component
public class LightSensorEventHandler extends BaseSensorEventHandler<LightSensorAvro> {

    /**
     * Конструктор обработчика событий датчиков освещенности.
     *
     * @param producer Kafka продюсер для отправки событий
     */
    public LightSensorEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    /**
     * Преобразует LightSensorProto в LightSensorAvro.
     * Выполняет маппинг данных датчика освещенности: уровня освещенности и качества связи.
     *
     * @param event событие датчика освещенности, должно быть типа LightSensorProto
     * @return Avro-представление данных датчика освещенности
     * @throws IllegalArgumentException если данные датчика некорректны
     */
    @Override
    protected LightSensorAvro mapToAvro(SensorEventProto event) {
        if (event.getPayloadCase() == LIGHT_SENSOR) {
            LightSensorProto lightSensor = event.getLightSensor();
            return LightSensorAvro.newBuilder()
                    .setLinkQuality(lightSensor.getLinkQuality())
                    .setLuminosity(lightSensor.getLuminosity())
                    .build();
        } else {
            throw new IllegalArgumentException("Expected LIGHT_SENSOR event type");
        }
    }

    /**
     * Возвращает тип обрабатываемого события датчика освещенности.
     *
     * @return тип события LIGHT_SENSOR
     */
    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return LIGHT_SENSOR;
    }
}
