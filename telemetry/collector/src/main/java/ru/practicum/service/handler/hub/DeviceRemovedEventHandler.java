package ru.practicum.service.handler.hub;

import org.springframework.stereotype.Component;
import ru.practicum.service.KafkaEventProducer;
import ru.yandex.practicum.grpc.telemetry.event.DeviceRemovedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;

import static ru.yandex.practicum.grpc.telemetry.event.HubEventProto.PayloadCase.DEVICE_REMOVED;

/**
 * Обработчик событий удаления устройств из хаба.
 * Преобразует DeviceRemovedEventProto в Avro-формат и отправляет в Kafka топик TELEMETRY_HUBS.
 *
 * @see BaseHubEventHandler
 * @see DeviceRemovedEventProto
 * @see DeviceRemovedEventAvro
 */
@Component
public class DeviceRemovedEventHandler extends BaseHubEventHandler<DeviceRemovedEventAvro> {
    /**
     * Конструктор обработчика событий добавления устройств.
     *
     * @param producer Kafka продюсер для отправки событий
     */
    public DeviceRemovedEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    /**
     * Преобразует HubEventProto в DeviceRemovedEventAvro.
     *
     * @param event событие удаления устройства, должно быть типа DeviceRemovedEvent
     * @return Avro-представление события удаления устройства
     */
    @Override
    protected DeviceRemovedEventAvro mapToAvro(HubEventProto event) {
        if (event.getPayloadCase() == DEVICE_REMOVED) {
            DeviceRemovedEventProto deviceRemoved = event.getDeviceRemoved();
            return DeviceRemovedEventAvro.newBuilder()
                    .setId(deviceRemoved.getId())
                    .build();
        } else {
            throw new IllegalArgumentException("Expected DEVICE_REMOVED event type");
        }
    }

    /**
     * Возвращает тип обрабатываемого события.
     *
     * @return тип события DEVICE_REMOVED
     */
    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return DEVICE_REMOVED;
    }
}
