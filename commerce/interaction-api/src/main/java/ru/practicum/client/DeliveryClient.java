package ru.practicum.client;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.dto.delivery.DeliveryDto;
import ru.practicum.dto.order.OrderDto;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Контракт для работы с доставкой.
 * Используется как для контроллера, так и для Feign-клиента.
 */
public interface DeliveryClient {

    /**
     * Запланировать доставку.
     *
     * @param deliveryDto данные доставки
     * @return созданная/обновленная информация о доставке
     */
    @PutMapping
    DeliveryDto planDelivery(@RequestBody @Valid DeliveryDto deliveryDto);

    /**
     * Отметить доставку как успешную.
     *
     * @param orderId идентификатор заказа
     */
    @PostMapping("/successful")
    void successfulDelivery(@RequestBody UUID orderId);

    /**
     * Отметить, что доставка забрана курьером.
     *
     * @param orderId идентификатор заказа
     */
    @PostMapping("/picked")
    void pickedDelivery(@RequestBody UUID orderId);

    /**
     * Отметить доставку как неудачную.
     *
     * @param orderId идентификатор заказа
     */
    @PostMapping("/failed")
    void failedDelivery(@RequestBody UUID orderId);

    /**
     * Рассчитать стоимость доставки.
     *
     * @param orderDto данные заказа
     * @return стоимость доставки
     */
    @PostMapping("/cost")
    BigDecimal deliveryCost(@RequestBody @Valid OrderDto orderDto);

    /**
     * Отменить доставку.
     *
     * @param deliveryId идентификатор доставки
     */
    @PostMapping("/cancel")
    void cancelDelivery(@RequestBody UUID deliveryId);
}