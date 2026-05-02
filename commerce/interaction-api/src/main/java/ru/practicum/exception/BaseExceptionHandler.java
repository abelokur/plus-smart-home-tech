package ru.practicum.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.practicum.dto.exception.ErrorResponse;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Базовый обработчик исключений для REST контроллеров.
 */
@Slf4j
public abstract class BaseExceptionHandler {
    @ExceptionHandler(NotAuthorizedUserException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleNotAuthorizedUserException(NotAuthorizedUserException ex) {
        log.warn("Unauthorized access", ex);
        return handleGenericException(ex, "Unauthorized access");
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleResourceNotFoundException(
            ResourceNotFoundException ex) {
        log.warn("Resource not found", ex);
        return handleGenericException(ex, "Resource not found");
    }

    @ExceptionHandler(ServiceTemporaryUnavailableException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ErrorResponse handleServiceTemporaryUnavailable(
            ServiceTemporaryUnavailableException ex) {
        log.warn("Service temporary unavailable", ex);
        return handleGenericException(ex, "Service temporary unavailable");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        List<ErrorResponse.Issue> issues = ex.getBindingResult().getFieldErrors().stream()
                .map(this::mapFieldErrorToIssue)
                .collect(Collectors.toList());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .message("Validation failed")
                .issues(issues)
                .build();

        log.warn("Validation failed with {} errors", issues.size(), ex);
        return errorResponse;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolationException(
            ConstraintViolationException ex) {

        List<ErrorResponse.Issue> issues = ex.getConstraintViolations().stream()
                .map(this::mapConstraintViolationToIssue)
                .collect(Collectors.toList());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .message("Constraint violation")
                .issues(issues)
                .build();

        log.warn("Constraint violation with {} errors", issues.size(), ex);
        return errorResponse;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgumentException(
            IllegalArgumentException ex) {

        List<ErrorResponse.Issue> issues = List.of(
                ErrorResponse.Issue.builder()
                        .location("request")
                        .description(ex.getMessage())
                        .build()
        );

        ErrorResponse errorResponse = ErrorResponse.builder()
                .message("Invalid request parameter")
                .issues(issues)
                .build();

        log.warn("Illegal argument: {}", ex.getMessage(), ex);
        return errorResponse;
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleRuntimeException(RuntimeException ex) {
        log.error("Internal server error", ex);
        return handleGenericException(ex, "Internal server error");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception ex) {
        log.error("Internal server error", ex);
        return handleGenericException(ex, "Internal server error");
    }

    protected ErrorResponse handleGenericException(Exception ex, String message) {
        List<ErrorResponse.Issue> issues = List.of(
                ErrorResponse.Issue.builder()
                        .location(ex.getClass().getSimpleName())
                        .description(ex.getMessage() != null ? ex.getMessage() : "Unknown error")
                        .build()
        );

        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(message)
                .issues(issues)
                .build();

        log.error("{}: {}", message, ex.getMessage(), ex);
        return errorResponse;
    }

    private ErrorResponse.Issue mapFieldErrorToIssue(FieldError fieldError) {
        return ErrorResponse.Issue.builder()
                .location(fieldError.getField())
                .description(fieldError.getDefaultMessage())
                .build();
    }

    private ErrorResponse.Issue mapConstraintViolationToIssue(ConstraintViolation<?> violation) {
        String propertyPath = violation.getPropertyPath().toString();
        // Извлекаем только имя поля из полного пути
        String fieldName = propertyPath.contains(".")
                ? propertyPath.substring(propertyPath.lastIndexOf('.') + 1)
                : propertyPath;

        return ErrorResponse.Issue.builder()
                .location(fieldName)
                .description(violation.getMessage())
                .build();
    }
}