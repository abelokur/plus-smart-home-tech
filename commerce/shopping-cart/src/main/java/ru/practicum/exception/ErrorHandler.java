package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.dto.exception.ErrorResponse;

/**
 * Глобальный обработчик исключений для приложения.
 */
@RestControllerAdvice
@Slf4j
public class ErrorHandler extends BaseExceptionHandler {
    @ExceptionHandler(NoProductsInShoppingCartException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNoProductsInShoppingCartException(NoProductsInShoppingCartException e) {
        return handleGenericException(e, "No products in shopping cart");
    }
}