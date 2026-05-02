package ru.practicum.config;

import feign.RequestInterceptor;
import feign.auth.BasicAuthRequestInterceptor;
import feign.codec.ErrorDecoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import ru.practicum.client.delivery.DeliveryErrorDecoder;

/**
 * Конфигурация для Feign клиента доставки.
 * <p>
 * Настраивает компоненты, специфичные для взаимодействия
 * с микросервисом доставки через Feign.
 * <p>
 * Предоставляет бины для:
 * <ul>
 *   <li>Аутентификации через Basic Auth (учетные данные загружаются из конфигурации)</li>
 *   <li>Кастомной обработки ошибок от сервиса доставки</li>
 * </ul>
 *
 * @see DeliveryErrorDecoder
 * @see BasicAuthRequestInterceptor
 */
public class DeliveryFeignClientConfig {
    @Value("${feign.clients.delivery.username}")
    private String username;
    @Value("${feign.clients.delivery.password}")
    private String password;

    @Bean
    public RequestInterceptor deliveryAuthInterceptor() {
        return new BasicAuthRequestInterceptor(username, password);
    }

    @Bean
    public ErrorDecoder deliveryErrorDecoder() {
        return new DeliveryErrorDecoder();
    }
}
