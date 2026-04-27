package ru.practicum.config;

import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.client.ShoppingStoreErrorDecoder;

/**
 * Конфигурация для Feign клиента магазина.
 */
@Configuration
public class ShoppingStoreFeignConfig {

    /**
     * Создает декодер ошибок для Feign клиента магазина.
     *
     * @return декодер ошибок
     */
    @Bean
    public ErrorDecoder errorDecoder() {
        return new ShoppingStoreErrorDecoder();
    }
}