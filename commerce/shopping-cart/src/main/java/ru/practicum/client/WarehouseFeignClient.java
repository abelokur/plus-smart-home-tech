package ru.practicum.client;

import org.springframework.cloud.openfeign.FeignClient;
import ru.practicum.config.WarehouseFeignClientConfig;

/**
 * Feign клиент для взаимодействия с микросервисом склада.
 * <p>
 * Определяет базовый URL, конфигурацию и механизм fallback для
 * обработки сбоев при вызовах API склада.
 */
@FeignClient(
        name = "warehouse",
        path = "/api/v1/warehouse",
        configuration = WarehouseFeignClientConfig.class,
        fallbackFactory = WarehouseFeignClientFallbackFactory.class)
public interface WarehouseFeignClient extends WarehouseClient {
}