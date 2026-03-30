package ru.practicum.service.handler.hub;

import ru.practicum.model.hub.HubEvent;
import ru.practicum.model.hub.HubEventType;

public interface HubEventHandler {

    HubEventType getMessageType();

    void handle(HubEvent event);
}
