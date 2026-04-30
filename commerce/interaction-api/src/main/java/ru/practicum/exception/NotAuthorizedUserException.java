package ru.practicum.exception;

/**
 * Исключение при попытке доступа неавторизованным пользователем.
 */
public class NotAuthorizedUserException extends RuntimeException {
    public NotAuthorizedUserException(String message) {
        super(message);
    }
}