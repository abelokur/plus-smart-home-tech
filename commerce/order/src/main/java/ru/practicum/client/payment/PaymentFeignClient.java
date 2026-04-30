package ru.practicum.client.payment;

import org.springframework.cloud.openfeign.FeignClient;
import ru.practicum.client.PaymentClient;
import ru.practicum.config.PaymentFeignClientConfig;

/**
 * Feign клиент для взаимодействия с микросервисом оплаты.
 * <p>
 * Определяет базовый URL, конфигурацию и механизм fallback для
 * обработки сбоев при вызовах API оплаты.
 */
@FeignClient(
        name = "payment",
        path = "/api/v1/payment",
        configuration = PaymentFeignClientConfig.class,
        fallbackFactory = PaymentFeignClientFallbackFactory.class)
public interface PaymentFeignClient extends PaymentClient {
}
