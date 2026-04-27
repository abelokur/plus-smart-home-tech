package ru.practicum.service;

import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc.HubRouterControllerBlockingStub;

/**
 * Сервис для взаимодействия с HubRouter через gRPC.
 * Отправляет запросы на выполнение действий устройствам.
 */
@Service
@Slf4j
public class HubRouterProcessor {

    @GrpcClient("hub-router")
    private HubRouterControllerBlockingStub hubRouterClient;

    /**
     * Отправляет запрос на выполнение действия через gRPC клиент.
     * Обрабатывает различные ошибки gRPC соединения.
     *
     * @param deviceActionRequest запрос на выполнение действия устройства
     */
    public void handleAction(DeviceActionRequest deviceActionRequest) {
        if (hubRouterClient == null) {
            log.error("GRPC client is not initialized");
            return;
        }

        if (deviceActionRequest == null) {
            log.warn("Device action request is null");
            return;
        }

        try {
            hubRouterClient.handleDeviceAction(deviceActionRequest);
        } catch (StatusRuntimeException e) {
            switch (e.getStatus().getCode()) {
                case UNAVAILABLE -> log.warn("Server hub-router unavailable: {}", e.getMessage());
                case DEADLINE_EXCEEDED -> log.warn("Timeout call handleDeviceAction");
                case INVALID_ARGUMENT -> log.warn("Invalid argument: {}", e.getMessage());
                default -> log.warn("GRPC error, couldn't call handleDeviceAction: {}", e.getStatus(), e);
            }

        }
    }
}
