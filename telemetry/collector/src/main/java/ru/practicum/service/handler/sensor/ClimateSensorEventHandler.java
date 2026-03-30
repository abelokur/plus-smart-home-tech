package ru.practicum.service.handler.sensor;

import org.springframework.stereotype.Component;
import ru.practicum.model.sensor.ClimateSensorEvent;
import ru.practicum.model.sensor.SensorEvent;
import ru.practicum.model.sensor.SensorEventType;
import ru.practicum.service.KafkaEventProducer;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;

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
     * Преобразует SensorEvent в ClimateSensorAvro.
     * Выполняет маппинг климатических данных: уровня CO2, влажности и температуры.
     *
     * @param event событие климатического сенсора, должно быть типа ClimateSensorEvent
     * @return Avro-представление климатических данных сенсора
     * @throws ClassCastException       если event не является ClimateSensorEvent
     * @throws IllegalArgumentException если климатические данные некорректны
     */
    @Override
    protected ClimateSensorAvro mapToAvro(SensorEvent event) {
        ClimateSensorEvent _event = (ClimateSensorEvent) event;
        return ClimateSensorAvro.newBuilder()
                .setCo2Level(_event.getCo2Level())
                .setHumidity(_event.getHumidity())
                .setTemperatureC(_event.getTemperatureC())
                .build();
    }

    /**
     * Возвращает тип обрабатываемого события климатического сенсора.
     *
     * @return тип события CLIMATE_SENSOR_EVENT
     */
    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.CLIMATE_SENSOR_EVENT;
    }
}
