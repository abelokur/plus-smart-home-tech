package ru.practicum.client.shopping_store;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.practicum.dto.product.ProductCategory;
import ru.practicum.dto.product.ProductDto;
import ru.practicum.dto.product.SetProductQuantityStateRequest;

import java.util.UUID;

import static ru.practicum.exception.FallBackUtility.fastFallBack;

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
        };
    }
}