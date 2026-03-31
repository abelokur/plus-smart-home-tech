package ru.practicum.service.handler.hub;

import org.springframework.stereotype.Component;
import ru.practicum.service.KafkaEventProducer;
import ru.practicum.service.mapper.EnumMapper;
import ru.yandex.practicum.grpc.telemetry.event.DeviceAddedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;

import static ru.yandex.practicum.grpc.telemetry.event.HubEventProto.PayloadCase.DEVICE_ADDED;

@Component
public class DeviceAddedEventHandler extends BaseHubEventHandler<DeviceAddedEventAvro> {

    public DeviceAddedEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    protected DeviceAddedEventAvro mapToAvro(HubEventProto event) {
        if (event.getPayloadCase() == DEVICE_ADDED) {
            DeviceAddedEventProto deviceAdded = event.getDeviceAdded();
            return DeviceAddedEventAvro.newBuilder()
                    .setId(deviceAdded.getId())
                    .setType(EnumMapper.map(deviceAdded.getType(), DeviceTypeAvro.class))
                    .build();
        } else {
            throw new IllegalArgumentException("Expected DEVICE_ADDED event type");
        }
    }

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return DEVICE_ADDED;
    }
}
