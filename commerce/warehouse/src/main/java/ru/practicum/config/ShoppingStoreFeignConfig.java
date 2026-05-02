package ru.practicum.config;

import feign.RequestInterceptor;
import feign.auth.BasicAuthRequestInterceptor;
import feign.codec.ErrorDecoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import ru.practicum.client.ShoppingStoreErrorDecoder;

/**
 * Конфигурация для Feign клиента магазина.
 * <p>
 * Настраивает компоненты, специфичные для взаимодействия
 * с микросервисом магазина через Feign.
 * <p>
 * Предоставляет бины для:
 * <ul>
 *   <li>Аутентификации через Basic Auth (учетные данные загружаются из конфигурации)</li>
 *   <li>Кастомной обработки ошибок от сервиса магазина</li>
 * </ul>
 *
 * @see ShoppingStoreErrorDecoder
 * @see BasicAuthRequestInterceptor
 */
public class ShoppingStoreFeignConfig {
    @Value("${feign.clients.shopping-store.username}")
    private String username;
    @Value("${feign.clients.shopping-store.password}")
    private String password;

    @Bean
    public RequestInterceptor shoppingStoreAuthInterceptor() {
        return new BasicAuthRequestInterceptor(username, password);
    }

    @Bean
    public ErrorDecoder shoppingStoreErrorDecoder() {
        return new ShoppingStoreErrorDecoder();
    }
}