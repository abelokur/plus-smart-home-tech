package ru.practicum.service;

import ru.practicum.dto.delivery.DeliveryDto;
import ru.practicum.dto.order.OrderDto;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Сервис для управления операциями доставки.
 * Определяет контракт для работы с доставками заказов.
 */
public interface DeliveryService {

    /**
     * Создает или планирует новую доставку для заказа.
     *
     * @param deliveryDto данные доставки
     * @return созданная доставка в формате DTO
     */
    DeliveryDto planDelivery(DeliveryDto deliveryDto);

    /**
     * Отмечает доставку как успешно завершенную.
     * Уведомляет связанные сервисы об успешной доставке.
     *
     * @param orderId идентификатор заказа
     */
    void successfulDelivery(UUID orderId);

    /**
     * Отмечает, что доставка забрана курьером.
     * Инициирует процессы сборки заказа и уведомления склада.
     *
     * @param orderId идентификатор заказа
     */
    void pickedDelivery(UUID orderId);

    /**
     * Отмечает доставку как неудачную.
     * Уведомляет связанные сервисы о неудачной доставке.
     *
     * @param orderId идентификатор заказа
     */
    void failedDelivery(UUID orderId);

    /**
     * Рассчитывает стоимость доставки для заказа.
     * Учитывает вес, объем, хрупкость и адреса доставки.
     *
     * @param orderDto данные заказа
     * @return рассчитанная стоимость доставки
     */
    BigDecimal deliveryCost(OrderDto orderDto);

    /**
     * Отменяет запланированную доставку.
     * Обновляет статус доставки на "отменена".
     *
     * @param deliveryId идентификатор доставки
     */
    void cancelDelivery(UUID deliveryId);
}