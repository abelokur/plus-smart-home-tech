package ru.practicum.client.delivery;

import org.springframework.cloud.openfeign.FeignClient;
import ru.practicum.client.DeliveryClient;
import ru.practicum.config.DeliveryFeignClientConfig;

/**
 * Feign клиент для взаимодействия с микросервисом доставки.
 * <p>
 * Определяет базовый URL, конфигурацию и механизм fallback для
 * обработки сбоев при вызовах API доставки.
 */
@FeignClient(
        name = "delivery",
        path = "/api/v1/delivery",
        configuration = DeliveryFeignClientConfig.class,
        fallbackFactory = DeliveryFeignClientFallbackFactory.class)
public interface DeliveryFeignClient extends DeliveryClient {
}
