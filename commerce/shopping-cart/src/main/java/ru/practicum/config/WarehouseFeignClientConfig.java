package ru.practicum.config;

import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.client.WarehouseErrorDecoder;

/**
 * Конфигурация для Feign клиента склада.
 * <p>
 * Настраивает компоненты, специфичные для взаимодействия
 * с микросервисом склада через Feign.
 */
@Configuration
public class WarehouseFeignClientConfig {

    /**
     * Создает декодер ошибок для Feign клиента склада.
     * <p>
     * Декодер обрабатывает HTTP ответы от сервиса склада
     * и преобразует их в соответствующие исключения.
     *
     * @return декодер ошибок
     */
    @Bean
    public ErrorDecoder errorDecoder() {
        return new WarehouseErrorDecoder();
    }
}