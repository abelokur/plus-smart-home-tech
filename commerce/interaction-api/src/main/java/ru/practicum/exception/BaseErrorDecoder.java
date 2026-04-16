package ru.practicum.exception;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

/**
 * Базовый декодер ошибок Feign клиента.
 */
@Slf4j
public abstract class BaseErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        HttpStatus status = HttpStatus.valueOf(response.status());

        if (status == HttpStatus.NOT_FOUND) {
            log.debug("Not found error {} for {}", status, methodKey);
            return new ResourceNotFoundException("Resource not found: " + methodKey);
        }

        if (status.is5xxServerError()) {
            log.warn("Server error {} for {}", status, methodKey);
            return new ServiceTemporaryUnavailableException("Service temporary unavailable: " + methodKey);
        }

        if (status.is4xxClientError()) {
            log.debug("Client error {} for {}", status, methodKey);
            return new BadRequestException("Service error: " + methodKey + ", status: " + status);
        }

        return defaultErrorDecoder.decode(methodKey, response);
    }
}