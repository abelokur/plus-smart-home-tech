package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * Утилитный класс для обработки fallback-ошибок в circuit breaker.
 * Обеспечивает централизованную обработку исключений при сбоях внешних сервисов,
 * логируя ошибки и преобразуя их в соответствующие бизнес-исключения.
 */
@Slf4j
public class FallBackUtility {

    /**
     * Быстрая обработка исключений при fallback.
     * Анализирует причину сбоя, логирует её и преобразует в соответствующее бизнес-исключение.
     *
     * @param cause исключение, вызвавшее сбой
     * @throws ResourceNotFoundException            если ресурс не найден (404)
     * @throws BadRequestException                  если некорректный запрос (4xx)
     * @throws ServiceTemporaryUnavailableException для всех остальных ошибок (5xx/network)
     */
    public static void fastFallBack(Throwable cause) {
        if (cause instanceof ResourceNotFoundException) {
            log.warn("Not found (404): ", cause);
            throw (ResourceNotFoundException) cause;
        }

        if (cause instanceof BadRequestException) {
            log.warn("Bad request (4xx): ", cause);
            throw (BadRequestException) cause;
        }

        log.error("Server/network error ", cause);
        throw new ServiceTemporaryUnavailableException(cause.getMessage());
    }
}