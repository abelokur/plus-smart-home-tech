package ru.practicum.client.delivery;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import ru.practicum.dto.delivery.DeliveryDto;
import ru.practicum.dto.order.OrderDto;

import java.math.BigDecimal;
import java.util.UUID;

import static ru.practicum.exception.FallBackUtility.fastFallBack;

/**
 * Фабрика fallback для Feign клиента доставки.
 * <p>
 * Создает резервную реализацию {@link DeliveryFeignClient},
 * которая вызывается при сбоях в основном сервисе.
 */
@Component
@Slf4j
public class DeliveryFeignClientFallbackFactory implements FallbackFactory<DeliveryFeignClient> {
    @Override
    public DeliveryFeignClient create(Throwable cause) {
        return new DeliveryFeignClient() {
            @Override
            public DeliveryDto planDelivery(DeliveryDto deliveryDto) {
                fastFallBack(cause);
                return null;
            }

            @Override
            public void successfulDelivery(UUID orderId) {
                fastFallBack(cause);
            }

            @Override
            public void pickedDelivery(UUID orderId) {
                fastFallBack(cause);
            }

            @Override
            public void failedDelivery(UUID orderId) {
                fastFallBack(cause);
            }

            @Override
            public BigDecimal deliveryCost(OrderDto orderDto) {
                fastFallBack(cause);
                return null;
            }

            @Override
            public void cancelDelivery(UUID deliveryId) {
                fastFallBack(cause);
            }
        };
    }
}
