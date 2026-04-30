package ru.practicum.service;

import ru.practicum.dto.order.OrderDto;
import ru.practicum.dto.payment.PaymentDto;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Сервис для управления операциями платежей.
 * Определяет контракт для работы с платежами, включая создание,
 * расчет стоимости и обработку статусов платежей.
 */
public interface PaymentService {

    /**
     * Создает платеж для заказа.
     * Проверяет данные заказа и инициирует платежную транзакцию.
     *
     * @param orderDto данные заказа
     * @return созданный платеж
     */
    PaymentDto payment(OrderDto orderDto);

    /**
     * Рассчитывает общую стоимость заказа.
     * Включает стоимость товаров, доставки и налоги.
     *
     * @param orderDto данные заказа
     * @return общая стоимость заказа
     */
    BigDecimal getTotalCost(OrderDto orderDto);

    /**
     * Обрабатывает успешный платеж.
     * Обновляет статус платежа на SUCCESS и уведомляет связанные сервисы.
     *
     * @param paymentId идентификатор платежа
     */
    void refund(UUID paymentId);

    /**
     * Рассчитывает стоимость товаров в заказе.
     * Учитывает цены и количество каждого товара.
     *
     * @param orderDto данные заказа
     * @return стоимость товаров
     */
    BigDecimal productCost(OrderDto orderDto);

    /**
     * Обрабатывает неудачный платеж.
     * Обновляет статус платежа на FAIL и уведомляет связанные сервисы.
     *
     * @param paymentId идентификатор платежа
     */
    void failed(UUID paymentId);
}