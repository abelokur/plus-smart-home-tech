package ru.practicum.service.handler.sensor;

import org.springframework.stereotype.Component;
import ru.practicum.model.sensor.SensorEvent;
import ru.practicum.model.sensor.SensorEventType;
import ru.practicum.model.sensor.SwitchSensorEvent;
import ru.practicum.service.KafkaEventProducer;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;

@Component
public class SwitchSensorEventHandler extends BaseSensorEventHandler<SwitchSensorAvro> {
    /**
     * Конструктор обработчика событий переключателей.
     *
     * @param producer Kafka продюсер для отправки событий
     */
    public SwitchSensorEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    /**
     * Преобразует SensorEvent в SwitchSensorAvro.
     * Выполняет маппинг данных переключателя: текущего состояния (включен/выключен).
     *
     * @param event событие переключателя, должно быть типа SwitchSensorEvent
     * @return Avro-представление данных переключателя
     * @throws ClassCastException       если event не является SwitchSensorEvent
     * @throws IllegalArgumentException если данные переключателя некорректны
     */
    @Override
    protected SwitchSensorAvro mapToAvro(SensorEvent event) {
        SwitchSensorEvent _event = (SwitchSensorEvent) event;
        return SwitchSensorAvro.newBuilder()
                .setState(_event.isState())
                .build();
    }

    /**
     * Возвращает тип обрабатываемого события переключателя.
     *
     * @return тип события SWITCH_SENSOR_EVENT
     */
    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.SWITCH_SENSOR_EVENT;
    }
}
