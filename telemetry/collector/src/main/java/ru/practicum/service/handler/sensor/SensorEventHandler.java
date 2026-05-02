package ru.practicum.service.handler.sensor;

import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;

/**
 * Интерфейс для обработчиков событий сенсоров.
 * Определяет контракт для классов, обрабатывающих различные типы событий, поступающих от сенсоров.
 * Каждая реализация должна обрабатывать конкретный тип события {@link SensorEventProto.PayloadCase} и преобразовывать
 * данные сенсоров в соответствующий Avro-формат для отправки в Kafka.
 *
 * @see SensorEventProto
 * @see SensorEventProto.PayloadCase
 * @see BaseSensorEventHandler
 */
public interface SensorEventHandler {

    /**
     * Возвращает тип события сенсора, который обрабатывает данный обработчик.
     * Используется для маршрутизации событий к соответствующим обработчикам на основе типа сенсора.
     *
     * @return тип события сенсора, не должен быть null
     */
    SensorEventProto.PayloadCase getMessageType();

    /**
     * Обрабатывает событие сенсора.
     * Реализация должна содержать логику преобразования данных сенсора в Avro-формат
     * и отправки в соответствующий Kafka топик.
     *
     * @param event событие сенсора для обработки, не должно быть null
     * @throws IllegalArgumentException если event равен null или содержит некорректные данные
     * @throws RuntimeException         если произошла ошибка при обработке события или отправке в Kafka
     */
    void handle(SensorEventProto event);
}
