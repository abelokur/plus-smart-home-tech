package ru.practicum.exception;

/**
 * Исключение, выбрасываемое когда платеж не найден в системе.
 * Используется для обработки случаев, когда запрашиваемый платеж
 * не существует или недоступен.
 */
public class NoPaymentFoundException extends RuntimeException {
    public NoPaymentFoundException(String message) {
        super(message);
    }
}
