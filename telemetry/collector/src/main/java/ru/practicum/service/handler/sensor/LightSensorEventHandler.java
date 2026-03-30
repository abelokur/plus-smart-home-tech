package ru.practicum.service.handler.sensor;

import org.springframework.stereotype.Component;
import ru.practicum.model.sensor.LightSensorEvent;
import ru.practicum.model.sensor.SensorEvent;
import ru.practicum.model.sensor.SensorEventType;
import ru.practicum.service.KafkaEventProducer;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;

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
     * Преобразует SensorEvent в LightSensorAvro.
     * Выполняет маппинг данных датчика освещенности: уровня освещенности и качества связи.
     *
     * @param event событие датчика освещенности, должно быть типа LightSensorEvent
     * @return Avro-представление данных датчика освещенности
     * @throws ClassCastException       если event не является LightSensorEvent
     * @throws IllegalArgumentException если данные датчика некорректны
     */
    @Override
    protected LightSensorAvro mapToAvro(SensorEvent event) {
        LightSensorEvent _event = (LightSensorEvent) event;
        return LightSensorAvro.newBuilder()
                .setLinkQuality(_event.getLinkQuality())
                .setLuminosity(_event.getLuminosity())
                .build();
    }

    /**
     * Возвращает тип обрабатываемого события датчика освещенности.
     *
     * @return тип события LIGHT_SENSOR_EVENT
     */
    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.LIGHT_SENSOR_EVENT;
    }
}
