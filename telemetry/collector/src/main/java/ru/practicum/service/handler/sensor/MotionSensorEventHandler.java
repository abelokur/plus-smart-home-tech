package ru.practicum.service.handler.sensor;

import org.springframework.stereotype.Component;
import ru.practicum.service.KafkaEventProducer;
import ru.yandex.practicum.grpc.telemetry.event.MotionSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;

import static ru.yandex.practicum.grpc.telemetry.event.SensorEventProto.PayloadCase.MOTION_SENSOR;

/**
 * Обработчик событий датчиков движения.
 * Преобразует MotionSensorProto в MotionSensorAvro и отправляет в Kafka топик TELEMETRY_SENSORS.
 * Обрабатывает данные о наличии движения, качестве связи и напряжении датчика.
 *
 * @see BaseSensorEventHandler
 * @see MotionSensorProto
 * @see MotionSensorAvro
 * @see SensorEventProto.PayloadCase#MOTION_SENSOR
 */
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
     * Преобразует SensorEventProto в MotionSensorAvro.
     * Выполняет маппинг данных датчика движения: статуса обнаружения движения, качества связи и напряжения.
     *
     * @param event событие датчика движения, должно быть типа MotionSensorProto
     * @return Avro-представление данных датчика движения
     * @throws IllegalArgumentException если данные датчика некорректны
     */
    @Override
    protected MotionSensorAvro mapToAvro(SensorEventProto event) {
        if (event.getPayloadCase() == MOTION_SENSOR) {
            MotionSensorProto motionSensor = event.getMotionSensor();
            return MotionSensorAvro.newBuilder()
                    .setMotion(motionSensor.getMotion())
                    .setLinkQuality(motionSensor.getLinkQuality())
                    .setVoltage(motionSensor.getVoltage())
                    .build();
        } else {
            throw new IllegalArgumentException("Expected MOTION_SENSOR event type");
        }
    }

    /**
     * Возвращает тип обрабатываемого события датчика движения.
     *
     * @return тип события MOTION_SENSOR
     */
    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return MOTION_SENSOR;
    }
}
