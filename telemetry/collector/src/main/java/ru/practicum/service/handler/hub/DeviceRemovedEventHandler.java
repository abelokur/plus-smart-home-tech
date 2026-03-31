package ru.practicum.service.handler.hub;

import org.springframework.stereotype.Component;
import ru.practicum.service.KafkaEventProducer;
import ru.yandex.practicum.grpc.telemetry.event.DeviceRemovedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;

import static ru.yandex.practicum.grpc.telemetry.event.HubEventProto.PayloadCase.DEVICE_REMOVED;

@Component
public class DeviceRemovedEventHandler extends BaseHubEventHandler<DeviceRemovedEventAvro> {

    public DeviceRemovedEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

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

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return DEVICE_REMOVED;
    }
}
