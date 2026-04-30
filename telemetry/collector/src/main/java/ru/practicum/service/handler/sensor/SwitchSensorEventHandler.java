package ru.practicum.service.handler.sensor;

import org.springframework.stereotype.Component;
import ru.practicum.service.KafkaEventProducer;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SwitchSensorProto;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;

import static ru.yandex.practicum.grpc.telemetry.event.SensorEventProto.PayloadCase.SWITCH_SENSOR;

/**
 * Обработчик событий переключателей (Switch сенсоров).
 * Преобразует SwitchSensorProto в SwitchSensorAvro и отправляет в Kafka топик TELEMETRY_SENSORS.
 * Обрабатывает данные о состоянии переключателя (включен/выключен).
 *
 * @see BaseSensorEventHandler
 * @see SwitchSensorProto
 * @see SwitchSensorAvro
 * @see SensorEventProto.PayloadCase#SWITCH_SENSOR
 */
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
     * Преобразует SensorEventProto в SwitchSensorAvro.
     * Выполняет маппинг данных переключателя: текущего состояния (включен/выключен).
     *
     * @param event событие переключателя, должно быть типа SwitchSensorProto
     * @return Avro-представление данных переключателя
     * @throws IllegalArgumentException если данные переключателя некорректны
     */
    @Override
    protected SwitchSensorAvro mapToAvro(SensorEventProto event) {
        if (event.getPayloadCase() == SWITCH_SENSOR) {
            SwitchSensorProto switchSensor = event.getSwitchSensor();
            return SwitchSensorAvro.newBuilder()
                    .setState(switchSensor.getState())
                    .build();
        } else {
            throw new IllegalArgumentException("Expected SWITCH_SENSOR event type");
        }
    }

    /**
     * Возвращает тип обрабатываемого события переключателя.
     *
     * @return тип события SWITCH_SENSOR
     */
    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SWITCH_SENSOR;
    }
}
