package ru.practicum.service.handler.sensor;

import org.springframework.stereotype.Component;
import ru.practicum.service.KafkaEventProducer;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.grpc.telemetry.event.TemperatureSensorProto;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;

import static ru.yandex.practicum.grpc.telemetry.event.SensorEventProto.PayloadCase.TEMPERATURE_SENSOR;

/**
 * Обработчик событий датчиков температуры.
 * Преобразует TemperatureSensorProto в TemperatureSensorAvro и отправляет в Kafka топик TELEMETRY_SENSORS.
 * Обрабатывает данные о температуре в градусах Цельсия и Фаренгейта с временной меткой.
 *
 * @see BaseSensorEventHandler
 * @see TemperatureSensorProto
 * @see TemperatureSensorAvro
 * @see SensorEventProto.PayloadCase#TEMPERATURE_SENSOR
 */
@Component
public class TemperatureSensorEventHandler extends BaseSensorEventHandler<TemperatureSensorAvro> {

    /**
     * Конструктор обработчика событий датчиков температуры.
     *
     * @param producer Kafka продюсер для отправки событий
     */
    public TemperatureSensorEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    /**
     * Преобразует SensorEventProto в TemperatureSensorAvro.
     * Выполняет маппинг данных датчика температуры: значений в градусах Цельсия и Фаренгейта.
     *
     * @param event событие датчика температуры, должно быть типа TemperatureSensorEvent
     * @return Avro-представление данных датчика температуры
     * @throws IllegalArgumentException если температурные данные некорректны
     */
    @Override
    protected TemperatureSensorAvro mapToAvro(SensorEventProto event) {
        if (event.getPayloadCase() == TEMPERATURE_SENSOR) {
            TemperatureSensorProto temperatureSensor = event.getTemperatureSensor();
            return TemperatureSensorAvro.newBuilder()
                    .setTemperatureC(temperatureSensor.getTemperatureC())
                    .setTemperatureF(temperatureSensor.getTemperatureF())
                    .build();
        } else {
            throw new IllegalArgumentException("Expected TEMPERATURE_SENSOR event type");
        }
    }

    /**
     * Возвращает тип обрабатываемого события датчика температуры.
     *
     * @return тип события TEMPERATURE_SENSOR
     */
    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return TEMPERATURE_SENSOR;
    }
}
