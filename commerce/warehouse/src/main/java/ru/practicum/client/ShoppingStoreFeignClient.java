package ru.practicum.client;

import org.springframework.cloud.openfeign.FeignClient;
import ru.practicum.config.ShoppingStoreFeignConfig;

/**
 * Feign клиент для взаимодействия с микросервисом магазина.
 */
@FeignClient(
        name = "shopping-store",
        path = "/api/v1/shopping-store",
        configuration = ShoppingStoreFeignConfig.class,
        fallbackFactory = ShoppingStoreFeignClientFallbackFactory.class)
public interface ShoppingStoreFeignClient extends ShoppingStoreClient {
}