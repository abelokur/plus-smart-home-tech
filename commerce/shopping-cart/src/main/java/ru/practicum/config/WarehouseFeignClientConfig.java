package ru.practicum.config;

import feign.RequestInterceptor;
import feign.auth.BasicAuthRequestInterceptor;
import feign.codec.ErrorDecoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import ru.practicum.client.WarehouseErrorDecoder;

/**
 * Конфигурация для Feign клиента склада.
 * <p>
 * Настраивает компоненты, специфичные для взаимодействия
 * с микросервисом склада через Feign.
 * <p>
 * Предоставляет бины для:
 * <ul>
 *   <li>Аутентификации через Basic Auth (учетные данные загружаются из конфигурации)</li>
 *   <li>Кастомной обработки ошибок от сервиса склада</li>
 * </ul>
 *
 * @see WarehouseErrorDecoder
 * @see BasicAuthRequestInterceptor
 */
public class WarehouseFeignClientConfig {

    @Value("${feign.clients.warehouse.username}")
    private String username;
    @Value("${feign.clients.warehouse.password}")
    private String password;

    @Bean
    public RequestInterceptor warehouseAuthInterceptor() {
        return new BasicAuthRequestInterceptor(username, password);
    }

    @Bean
    public ErrorDecoder warehouseErrorDecoder() {
        return new WarehouseErrorDecoder();
    }
}