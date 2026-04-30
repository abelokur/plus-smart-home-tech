package ru.practicum.client.order;

import org.springframework.cloud.openfeign.FeignClient;
import ru.practicum.client.OrderClient;
import ru.practicum.config.OrderFeignClientConfig;

/**
 * Feign клиент для взаимодействия с микросервисом заказов.
 * <p>
 * Определяет базовый URL, конфигурацию и механизм fallback для
 * обработки сбоев при вызовах API заказов.
 */
@FeignClient(
        name = "order",
        path = "/api/v1/order",
        configuration = OrderFeignClientConfig.class,
        fallbackFactory = OrderFeignClientFallbackFactory.class)
public interface OrderFeignClient extends OrderClient {
}
