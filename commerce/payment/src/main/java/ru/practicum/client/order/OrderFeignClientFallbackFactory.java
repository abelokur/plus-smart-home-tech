package ru.practicum.client.order;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.practicum.dto.order.CreateNewOrderRequest;
import ru.practicum.dto.order.OrderDto;
import ru.practicum.dto.order.ProductReturnRequest;

import java.util.UUID;

import static ru.practicum.exception.FallBackUtility.fastFallBack;

/**
 * Фабрика fallback для Feign клиента доставки.
 * <p>
 * Создает резервную реализацию {@link OrderFeignClient},
 * которая вызывается при сбоях в основном сервисе.
 */
@Component
@Slf4j
public class OrderFeignClientFallbackFactory implements FallbackFactory<OrderFeignClient> {
    @Override
    public OrderFeignClient create(Throwable cause) {
        return new OrderFeignClient() {
            @Override
            public Page<OrderDto> getOrders(String username, Pageable pageable) {
                fastFallBack(cause);
                return null;
            }

            @Override
            public OrderDto createOrder(CreateNewOrderRequest request) {
                fastFallBack(cause);
                return null;
            }

            @Override
            public OrderDto returnOrder(ProductReturnRequest request) {
                fastFallBack(cause);
                return null;
            }

            @Override
            public OrderDto paymentSuccess(UUID productId) {
                fastFallBack(cause);
                return null;
            }

            @Override
            public OrderDto paymentFailed(UUID productId) {
                fastFallBack(cause);
                return null;
            }

            @Override
            public OrderDto delivery(UUID productId) {
                fastFallBack(cause);
                return null;
            }

            @Override
            public OrderDto deliveryFailed(UUID productId) {
                fastFallBack(cause);
                return null;
            }

            @Override
            public OrderDto completedOrder(UUID productId) {
                fastFallBack(cause);
                return null;
            }

            @Override
            public OrderDto calculateTotal(UUID productId) {
                fastFallBack(cause);
                return null;
            }

            @Override
            public OrderDto calculateDelivery(UUID productId) {
                fastFallBack(cause);
                return null;
            }

            @Override
            public OrderDto assemblyOrder(UUID productId) {
                fastFallBack(cause);
                return null;
            }

            @Override
            public OrderDto assemblyFailed(UUID productId) {
                fastFallBack(cause);
                return null;
            }
        };
    }
}
