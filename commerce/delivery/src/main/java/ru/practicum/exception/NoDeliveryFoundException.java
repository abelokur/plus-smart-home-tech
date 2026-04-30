package ru.practicum.exception;

/**
 * Исключение, выбрасываемое когда не найдена информация о доставке.
 * Используется для обработки случаев, когда запрашиваемая доставка
 * не существует в системе.
 */
public class NoDeliveryFoundException extends RuntimeException {
    public NoDeliveryFoundException(String message) {
        super(message);
    }
}