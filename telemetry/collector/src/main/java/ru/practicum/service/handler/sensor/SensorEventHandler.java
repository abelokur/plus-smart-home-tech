package ru.practicum.service.handler.sensor;

import ru.practicum.model.sensor.SensorEvent;
import ru.practicum.model.sensor.SensorEventType;

public interface SensorEventHandler {
    SensorEventType getMessageType();


    void handle(SensorEvent event);
}
