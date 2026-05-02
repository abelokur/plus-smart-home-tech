package ru.practicum.client.payment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import ru.practicum.dto.order.OrderDto;
import ru.practicum.dto.payment.PaymentDto;

import java.math.BigDecimal;
import java.util.UUID;

import static ru.practicum.exception.FallBackUtility.fastFallBack;

/**
 * Фабрика fallback для Feign клиента оплаты.
 * <p>
 * Создает резервную реализацию {@link PaymentFeignClient},
 * которая вызывается при сбоях в основном сервисе.
 */
@Component
@Slf4j
public class PaymentFeignClientFallbackFactory implements FallbackFactory<PaymentFeignClient> {
    @Override
    public PaymentFeignClient create(Throwable cause) {
        return new PaymentFeignClient() {
            @Override
            public PaymentDto payment(OrderDto orderDto) {
                fastFallBack(cause);
                return null;
            }

            @Override
            public BigDecimal getTotalCost(OrderDto orderDto) {
                fastFallBack(cause);
                return null;
            }

            @Override
            public void refund(UUID paymentId) {
                fastFallBack(cause);
            }

            @Override
            public BigDecimal productCost(OrderDto orderDto) {
                fastFallBack(cause);
                return null;
            }

            @Override
            public void failed(UUID paymentId) {
                fastFallBack(cause);
            }
        };
    }
}
