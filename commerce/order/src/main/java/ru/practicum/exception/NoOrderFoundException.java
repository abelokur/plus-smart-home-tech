package ru.practicum.exception;

/**
 * Исключение, выбрасываемое когда заказ не найден в системе.
 * Используется для обработки случаев, когда запрашиваемый заказ
 * не существует или недоступен.
 */
public class NoOrderFoundException extends RuntimeException {
    public NoOrderFoundException(String message) {
        super(message);
    }
}
