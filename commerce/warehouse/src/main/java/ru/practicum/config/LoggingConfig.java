package ru.practicum.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.annotation.LogAllMethodsAspect;

/**
 * Конфигурация для аспектно-ориентированного логирования.
 */
@Configuration
public class LoggingConfig {

    /**
     * Создает аспект для логирования методов.
     *
     * @return аспект логирования
     */
    @Bean
    public LogAllMethodsAspect loggingAspect() {
        return new LogAllMethodsAspect();
    }
}