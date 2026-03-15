package ru.practicum.service.handler.sensor;

import org.springframework.stereotype.Component;
import ru.practicum.model.sensor.MotionSensorEvent;
import ru.practicum.model.sensor.SensorEvent;
import ru.practicum.model.sensor.SensorEventType;
import ru.practicum.service.KafkaEventProducer;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;

@Component
public class MotionSensorEventHandler extends BaseSensorEventHandler<MotionSensorAvro> {
    /**
     * Конструктор обработчика событий датчиков движения.
     *
     * @param producer Kafka продюсер для отправки событий
     */
    public MotionSensorEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    /**
     * Преобразует SensorEvent в MotionSensorAvro.
     * Выполняет маппинг данных датчика движения: статуса обнаружения движения, качества связи и напряжения.
     *
     * @param event событие датчика движения, должно быть типа MotionSensorEvent
     * @return Avro-представление данных датчика движения
     * @throws ClassCastException       если event не является MotionSensorEvent
     * @throws IllegalArgumentException если данные датчика некорректны
     */
    @Override
    protected MotionSensorAvro mapToAvro(SensorEvent event) {
        MotionSensorEvent _event = (MotionSensorEvent) event;
        return MotionSensorAvro.newBuilder()
                .setMotion(_event.isMotion())
                .setLinkQuality(_event.getLinkQuality())
                .setVoltage(_event.getVoltage())
                .build();
    }

    /**
     * Возвращает тип обрабатываемого события датчика движения.
     *
     * @return тип события MOTION_SENSOR_EVENT
     */
    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.MOTION_SENSOR_EVENT;
    }
}
