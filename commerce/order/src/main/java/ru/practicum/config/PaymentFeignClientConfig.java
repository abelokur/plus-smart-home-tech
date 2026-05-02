package ru.practicum.config;

import feign.RequestInterceptor;
import feign.auth.BasicAuthRequestInterceptor;
import feign.codec.ErrorDecoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import ru.practicum.client.payment.PaymentErrorDecoder;

/**
 * Конфигурация для Feign клиента оплаты.
 * <p>
 * Настраивает компоненты, специфичные для взаимодействия
 * с микросервисом оплаты через Feign.
 * <p>
 * Предоставляет бины для:
 * <ul>
 *   <li>Аутентификации через Basic Auth (учетные данные загружаются из конфигурации)</li>
 *   <li>Кастомной обработки ошибок от сервиса оплаты</li>
 * </ul>
 *
 * @see PaymentErrorDecoder
 * @see BasicAuthRequestInterceptor
 */
public class PaymentFeignClientConfig {
    @Value("${feign.clients.payment.username}")
    private String username;
    @Value("${feign.clients.payment.password}")
    private String password;

    @Bean
    public RequestInterceptor paymentAuthInterceptor() {
        return new BasicAuthRequestInterceptor(username, password);
    }

    @Bean
    public ErrorDecoder paymentErrorDecoder() {
        return new PaymentErrorDecoder();
    }
}
