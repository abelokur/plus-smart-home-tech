package ru.practicum.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import ru.practicum.dto.cart.ShoppingCartDto;
import ru.practicum.dto.warehouse.*;

import java.util.Map;
import java.util.UUID;

import static ru.practicum.exception.FallBackUtility.fastFallBack;

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

            @Override
            public void shippedToDelivery(ShippedToDeliveryRequest request) {
                fastFallBack(cause);
            }

            @Override
            public void returnToWarehouse(Map<UUID, Long> products) {
                fastFallBack(cause);
            }

            @Override
            public BookedProductsDto assemblyProductForOrderFromShoppingCart(AssemblyProductsForOrderRequest request) {
                fastFallBack(cause);
                return null;
            }

            @Override
            public void cancelAssemblyProductForOrder(UUID orderId) {
                fastFallBack(cause);
            }

        };
    }
}