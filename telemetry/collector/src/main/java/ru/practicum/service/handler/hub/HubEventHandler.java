package ru.practicum.service.handler.hub;

import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;

/**
 * Интерфейс для обработчиков событий хаба.
 * Определяет контракт для классов, обрабатывающих различные типы событий, происходящих в хабе.
 * Каждая реализация должна обрабатывать конкретный тип события {@link HubEventProto.PayloadCase}.
 *
 * @see HubEventProto
 * @see HubEventProto.PayloadCase
 */
public interface HubEventHandler {

    /**
     * Возвращает тип события, который обрабатывает данный обработчик.
     * Используется для маршрутизации событий к соответствующим обработчикам.
     *
     * @return HubEventType, не должен быть null
     */
    HubEventProto.PayloadCase getMessageType();

    /**
     * Обрабатывает событие хаба.
     * Реализация должна содержать логику преобразования и обработки конкретного типа события.
     *
     * @param event событие для обработки, не должно быть null
     * @throws IllegalArgumentException если event равен null
     * @throws RuntimeException         если произошла ошибка при обработке события
     */
    void handle(HubEventProto event);
}
