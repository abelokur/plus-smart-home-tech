package ru.practicum.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.hub.HubEvent;
import ru.practicum.model.hub.HubEventType;
import ru.practicum.model.sensor.SensorEvent;
import ru.practicum.model.sensor.SensorEventType;
import ru.practicum.service.handler.hub.HubEventHandler;
import ru.practicum.service.handler.sensor.SensorEventHandler;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/events", consumes = MediaType.APPLICATION_JSON_VALUE)
public class EventController {
    private final Map<SensorEventType, SensorEventHandler> sensorEventHandlers;
    private final Map<HubEventType, HubEventHandler> hubEventHandlers;

    public EventController(Set<SensorEventHandler> sensorEventHandlers, Set<HubEventHandler> hubEventHandlers) {
        this.sensorEventHandlers = sensorEventHandlers.stream()
                .collect(Collectors.toMap(SensorEventHandler::getMessageType, Function.identity()));
        this.hubEventHandlers = hubEventHandlers.stream()
                .collect(Collectors.toMap(HubEventHandler::getMessageType, Function.identity()));
    }

    @PostMapping("/sensors")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Отправка события сенсора", description = "Принимает события от IoT сенсоров")
    @ApiResponse(responseCode = "200", description = "Событие успешно принято и обработано")
    @ApiResponse(responseCode = "400", description = "Неверный формат данных или неизвестный тип сенсора")
    public void collectSensorEvent(@RequestBody @Valid SensorEvent sensorEvent) {
        log.info("collectSensorEvent = {}", sensorEvent);
        SensorEventHandler sensorEventHandler = sensorEventHandlers.get(sensorEvent.getType());
        if (sensorEventHandler == null) {
            throw new IllegalArgumentException("Couldn't find handler for event" + sensorEvent.getType());
        }
        sensorEventHandler.handle(sensorEvent);
    }

    @PostMapping("/hubs")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Отправка события хаба", description = "Принимает события хаба")
    @ApiResponse(responseCode = "200", description = "Событие успешно принято и обработано")
    @ApiResponse(responseCode = "400", description = "Неверный формат данных или неизвестный тип события")
    public void collectHubEvent(@RequestBody @Valid HubEvent hubEvent) {
        log.info("collectHubEvent = {}", hubEvent);
        HubEventHandler hubEventHandler = hubEventHandlers.get(hubEvent.getType());
        if (hubEventHandler == null) {
            throw new IllegalArgumentException("Couldn't find handler for event" + hubEvent.getType());
        }
        hubEventHandler.handle(hubEvent);
    }
}
