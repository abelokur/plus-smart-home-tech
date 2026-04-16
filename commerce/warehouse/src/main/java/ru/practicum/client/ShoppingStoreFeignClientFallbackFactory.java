package ru.practicum.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.practicum.dto.product.ProductCategory;
import ru.practicum.dto.product.ProductDto;
import ru.practicum.dto.product.SetProductQuantityStateRequest;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.ResourceNotFoundException;
import ru.practicum.exception.ServiceTemporaryUnavailableException;

import java.util.UUID;

/**
 * Фабрика fallback для Feign клиента магазина.
 */
@Component
@Slf4j
public class ShoppingStoreFeignClientFallbackFactory implements FallbackFactory<ShoppingStoreFeignClient> {

    @Override
    public ShoppingStoreFeignClient create(Throwable cause) {
        return new ShoppingStoreFeignClient() {
            @Override
            public Page<ProductDto> getProducts(ProductCategory category, Pageable pageable) {
                fastFallBack(cause);
                return null;
            }

            @Override
            public ProductDto createProduct(ProductDto dto) {
                fastFallBack(cause);
                return null;
            }

            @Override
            public ProductDto updateProduct(ProductDto dto) {
                fastFallBack(cause);
                return null;
            }

            @Override
            public Boolean removeProduct(UUID productId) {
                fastFallBack(cause);
                return null;
            }

            @Override
            public Boolean setQuantityState(SetProductQuantityStateRequest request) {
                fastFallBack(cause);
                return null;
            }

            @Override
            public ProductDto getProduct(UUID productId) {
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