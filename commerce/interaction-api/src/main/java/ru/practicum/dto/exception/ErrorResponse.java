package ru.practicum.dto.exception;

import lombok.Builder;

import java.util.List;

/**
 * Ответ с информацией об ошибке API.
 *
 * @param message общее описание ошибки
 * @param issues  список конкретных проблем
 */
public record ErrorResponse(
        String message,
        List<Issue> issues
) {
    @Builder
    public ErrorResponse {
    }

    /**
     * Описание конкретной проблемы.
     *
     * @param location    место возникновения ошибки
     * @param description детальное описание ошибки
     */
    public record Issue(
            String location,
            String description
    ) {
        @Builder
        public Issue {
        }
    }
}