package ru.practicum.service.handler.sensor;

import org.springframework.stereotype.Component;
import ru.practicum.service.KafkaEventProducer;
import ru.yandex.practicum.grpc.telemetry.event.ClimateSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;

import static ru.yandex.practicum.grpc.telemetry.event.SensorEventProto.PayloadCase.CLIMATE_SENSOR;


/**
 * Обработчик событий климатических сенсоров.
 * Преобразует ClimateSensorProto в ClimateSensorAvro и отправляет в Kafka топик TELEMETRY_SENSORS.
 * Обрабатывает данные о климатических показателях: уровень CO2, влажность и температуру.
 *
 * @see BaseSensorEventHandler
 * @see ClimateSensorProto
 * @see ClimateSensorAvro
 * @see SensorEventProto.PayloadCase#CLIMATE_SENSOR
 */
@Component
public class ClimateSensorEventHandler extends BaseSensorEventHandler<ClimateSensorAvro> {

    /**
     * Конструктор обработчика событий климатических сенсоров.
     *
     * @param producer Kafka продюсер для отправки событий
     */
    public ClimateSensorEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    /**
     * Преобразует ClimateSensorProto в ClimateSensorAvro.
     * Выполняет маппинг климатических данных: уровня CO2, влажности и температуры.
     *
     * @param event событие климатического сенсора, должно быть типа ClimateSensorEvent
     * @return Avro-представление климатических данных сенсора
     * @throws IllegalArgumentException если климатические данные некорректны
     */
    @Override
    protected ClimateSensorAvro mapToAvro(SensorEventProto event) {
        if (event.getPayloadCase() == CLIMATE_SENSOR) {
            ClimateSensorProto climateSensor = event.getClimateSensor();
            return ClimateSensorAvro.newBuilder()
                    .setCo2Level(climateSensor.getCo2Level())
                    .setHumidity(climateSensor.getHumidity())
                    .setTemperatureC(climateSensor.getTemperatureC())
                    .build();
        } else {
            throw new IllegalArgumentException("Expected CLIMATE_SENSOR event type");
        }

    }

    /**
     * Возвращает тип обрабатываемого события климатического сенсора.
     *
     * @return тип события CLIMATE_SENSOR
     */
    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return CLIMATE_SENSOR;
    }
}
