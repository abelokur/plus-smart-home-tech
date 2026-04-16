package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.dto.exception.ErrorResponse;

import java.util.List;

/**
 * Глобальный обработчик исключений для приложения.
 */
@RestControllerAdvice
@Slf4j
public class ErrorHandler extends BaseExceptionHandler {

    @ExceptionHandler(NotAuthorizedUserException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleNotAuthorizedUserException(NotAuthorizedUserException e) {
        return createShoppingCartErrorResponse("Unauthorized access", e);
    }

    @ExceptionHandler(NoProductsInShoppingCartException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNoProductsInShoppingCartException(NoProductsInShoppingCartException e) {
        return createShoppingCartErrorResponse("Shopping cart error", e);
    }

    private ErrorResponse createShoppingCartErrorResponse(String message, Exception e) {
        List<ErrorResponse.Issue> issues = List.of(
                ErrorResponse.Issue.builder()
                        .location(e.getClass().getSimpleName())
                        .description(e.getMessage())
                        .build()
        );

        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(message)
                .issues(issues)
                .build();

        log.warn("{}: {}", message, e.getMessage(), e);
        return errorResponse;
    }
}