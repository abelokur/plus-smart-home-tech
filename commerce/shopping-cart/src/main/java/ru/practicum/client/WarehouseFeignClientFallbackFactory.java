package ru.practicum.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import ru.practicum.dto.cart.ShoppingCartDto;
import ru.practicum.dto.warehouse.AddProductToWarehouseRequest;
import ru.practicum.dto.warehouse.AddressDto;
import ru.practicum.dto.warehouse.BookedProductsDto;
import ru.practicum.dto.warehouse.NewProductInWarehouseRequest;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.ResourceNotFoundException;
import ru.practicum.exception.ServiceTemporaryUnavailableException;

/**
 * Фабрика fallback для Feign клиента склада.
 * <p>
 * Создает резервную реализацию {@link WarehouseFeignClient},
 * которая вызывается при сбоях в основном сервисе.
 */
@Component
@Slf4j
public class WarehouseFeignClientFallbackFactory implements FallbackFactory<WarehouseFeignClient> {

    @Override
    public WarehouseFeignClient create(Throwable cause) {
        return new WarehouseFeignClient() {
            @Override
            public void addNewItemToWarehouse(NewProductInWarehouseRequest request) {
                fastFallBack(cause);
            }

            @Override
            public BookedProductsDto checkQuantityInWarehouse(ShoppingCartDto shoppingCart) {
                fastFallBack(cause);
                return null;
            }

            @Override
            public void addItemToWarehouse(AddProductToWarehouseRequest request) {
                fastFallBack(cause);
            }

            @Override
            public AddressDto getWarehouseAddress() {
                fastFallBack(cause);
                return null;
            }

            private void fastFallBack(Throwable cause) {
                if (cause instanceof ResourceNotFoundException) {
                    log.warn("Not found (404): ", cause);
                    throw (ResourceNotFoundException) cause;
                }

                if (cause instanceof BadRequestException) {
                    log.warn("Bad request (4xx): ", cause);
                    throw (BadRequestException) cause;
                }

                log.error("Server/network error ", cause);
                throw new ServiceTemporaryUnavailableException("Service is temporarily unavailable");
            }
        };
    }
}