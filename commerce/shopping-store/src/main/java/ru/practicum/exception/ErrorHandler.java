package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.dto.exception.ErrorResponse;

import java.util.List;

/**
 * Обработчик исключений для контроллера товаров.
 */
@RestControllerAdvice
@Slf4j
public class ErrorHandler extends BaseExceptionHandler {
    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(ProductNotFoundException e) {
        List<ErrorResponse.Issue> issues = List.of(
                ErrorResponse.Issue.builder()
                        .location(e.getClass().getSimpleName())
                        .description(e.getMessage())
                        .build()
        );

        ErrorResponse errorResponse = ErrorResponse.builder()
                .message("Product not found")
                .issues(issues)
                .build();

        log.warn("Product not found: {}", e.getMessage(), e);
        return errorResponse;
    }
}
