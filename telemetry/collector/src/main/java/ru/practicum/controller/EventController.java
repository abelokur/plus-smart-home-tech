package ru.practicum.controller;

import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import ru.practicum.service.handler.hub.HubEventHandler;
import ru.practicum.service.handler.sensor.SensorEventHandler;
import ru.yandex.practicum.grpc.telemetry.collector.CollectorControllerGrpc;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * gRPC сервис для приема и обработки событий телеметрии от IoT устройств.
 * Обрабатывает события от сенсоров (датчиков) и хабов умного дома.
 * <p>
 * Автоматически регистрируется в Spring контексте благодаря аннотации {@link GrpcService}.
 * Использует паттерн "Стратегия" через специализированные обработчики событий.
 *
 * @see SensorEventHandler
 * @see HubEventHandler
 */

@GrpcService
@Slf4j
public class EventController extends CollectorControllerGrpc.CollectorControllerImplBase {
    private final Map<SensorEventProto.PayloadCase, SensorEventHandler> sensorEventHandlers;
    private final Map<HubEventProto.PayloadCase, HubEventHandler> hubEventHandlers;

    /**
     * Конструктор контроллера событий.
     * Инициализирует карты обработчиков для различных типов событий.
     *
     * @param sensorEventHandlers набор обработчиков событий от сенсоров.
     * @param hubEventHandlers    набор обработчиков событий от хабов.
     * @throws IllegalArgumentException если переданы пустые наборы обработчиков.
     */
    public EventController(Set<SensorEventHandler> sensorEventHandlers, Set<HubEventHandler> hubEventHandlers) {
        this.sensorEventHandlers = sensorEventHandlers.stream()
                .collect(Collectors.toMap(
                        SensorEventHandler::getMessageType,
                        Function.identity()
                ));
        this.hubEventHandlers = hubEventHandlers.stream()
                .collect(Collectors.toMap(
                        HubEventHandler::getMessageType,
                        Function.identity()
                ));
    }

    /**
     * Метод для обработки событий от датчиков.
     * Вызывается при получении нового события от gRPC-клиента.
     *
     * @param request          Событие от датчика
     * @param responseObserver Ответ для клиента
     */
    @Override
    public void collectSensorEvent(SensorEventProto request, StreamObserver<Empty> responseObserver) {
        try {
            log.debug("Collecting sensor event for {}", request.getPayloadCase());
            // проверяем, есть ли обработчик для полученного события
            if (sensorEventHandlers.containsKey(request.getPayloadCase())) {
                // если обработчик найден, передаём событие ему на обработку
                sensorEventHandlers.get(request.getPayloadCase()).handle(request);
                log.trace("Collected sensor event for {}", request.getPayloadCase());
            } else {
                log.warn("No sensor event for {}", request.getPayloadCase());
                throw new IllegalArgumentException("Не могу найти обработчик для события " + request.getPayloadCase());
            }

            // после обработки события возвращаем ответ клиенту
            responseObserver.onNext(Empty.getDefaultInstance());
            // и завершаем обработку запроса
            responseObserver.onCompleted();
            log.debug("Response sensor event for {}, was send successfully", request.getPayloadCase());
        } catch (Exception e) {
            // в случае исключения отправляем ошибку клиенту
            log.error("Error collecting sensor event for {}", request.getPayloadCase(), e);
            responseObserver.onError(new StatusRuntimeException(Status.fromThrowable(e)));
        }
    }

    /**
     * Метод для обработки событий хаба.
     * Вызывается при получении нового события от gRPC-клиента.
     *
     * @param request          Событие хаба
     * @param responseObserver Ответ для клиента
     */
    @Override
    public void collectHubEvent(HubEventProto request, StreamObserver<Empty> responseObserver) {
        try {
            log.debug("Collecting hub event for {}", request.getPayloadCase());
            // проверяем, есть ли обработчик для полученного события
            if (hubEventHandlers.containsKey(request.getPayloadCase())) {
                // если обработчик найден, передаём событие ему на обработку
                hubEventHandlers.get(request.getPayloadCase()).handle(request);
                log.trace("Collected hub event for {}", request.getPayloadCase());
            } else {
                log.warn("No hub event for {}", request.getPayloadCase());
                throw new IllegalArgumentException("Не могу найти обработчик для события " + request.getPayloadCase());
            }

            // после обработки события возвращаем ответ клиенту
            responseObserver.onNext(Empty.getDefaultInstance());
            // и завершаем обработку запроса
            responseObserver.onCompleted();
            log.debug("Response hub event for {}, was send successfully", request.getPayloadCase());
        } catch (Exception e) {
            // в случае исключения отправляем ошибку клиенту
            log.error("Error collecting hub event for {}", request.getPayloadCase(), e);
            responseObserver.onError(new StatusRuntimeException(Status.fromThrowable(e)));
        }
    }
}
