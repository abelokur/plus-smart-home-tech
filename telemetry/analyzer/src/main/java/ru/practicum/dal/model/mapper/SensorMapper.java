package ru.practicum.dal.model.mapper;

import ru.practicum.dal.model.Sensor;

/**
 * Маппер для создания объектов Sensor.
 */
public class SensorMapper {

    /**
     * Создает объект Sensor по ID хаба и сенсора.
     */
    public static Sensor mapSensor(String hubId, String sensorId) {
        return Sensor.builder()
                .id(sensorId)
                .hubId(hubId)
                .build();
    }
}
